package sessionServer;

import entity.ActionType;
import entity.DeliverySessionCreationType;
import entity.XMLObjectFactory;
import nettyServer.BootStrapAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class SessionAdmin {
    private final Logger logger = LoggerFactory.getLogger(SessionAdmin.class);
    XMLObjectFactory factory = new XMLObjectFactory();
    private Thread startThread;
    private Thread stopThread;
    private volatile boolean status; // false 关闭 ture 开启
    private Map<String, DeliverySessionCreationType> store = new ConcurrentHashMap<>();
    BootStrapAdmin bootStrapAdmin;
    // 开始时间和session id的映射关系
    private Map<String, Set<String>> startIdRepl = new ConcurrentHashMap<>();
    // 结束时间和session id的映射关系
    private Map<String, Set<String>> stopIdRepl = new ConcurrentHashMap<>();
    // 优先级队列 帮助发送start请求
    Queue<Long> timeStartQue = new PriorityBlockingQueue<>();
    // 优先级队列 帮助发送stop请求
    Queue<Long> timeStopQue = new PriorityBlockingQueue<>();


    public void start() {
        status = true;
        bootStrapAdmin = new BootStrapAdmin();
        bootStrapAdmin.start();
        startThread = new Thread(() -> {
            logger.info("startThread is start");
            while (status) {
                Long time = timeStartQue.peek();
                if (time == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                    continue;
                }
                if (time > System.currentTimeMillis()) {
                    try {
                        Thread.sleep(time - System.currentTimeMillis());
                    } catch (InterruptedException e) { // 打破
                        // 关闭服务了
                        if (!status) { //所有未发送start请求的会话直接舍弃
                            break;
                        }
                    }
                } else { // 可以发送start请求
                    timeStartQue.poll();
                    sendRequest(time, "start");
                }
            }
        });
        stopThread = new Thread(() -> {
            while (status) {
                Long time = timeStopQue.peek();
                if (time == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                    continue;
                }
                if (time > System.currentTimeMillis()) {
                    try {
                        Thread.sleep(time - System.currentTimeMillis());
                    } catch (InterruptedException e) { // 打破
                        // 关闭服务了
                        logger.info("recv interrupt");
                        if (!status) { //所有未发送stop的会话全部发送stop请求，防止有些会话生命周期太长
                            while (timeStopQue.size() > 0) {
                                time = timeStopQue.poll();
                                sendRequest(time, "stop");
                            }
                        }
                    }
                } else { // 可以发送stop请求了
                    time = timeStopQue.poll();
                    sendRequest(time, "stop");
                }
            }
        });
        startThread.start();
        stopThread.start();
        logger.info("session admin is start");
    }

    public void stop() {
        status = false;
        startThread.interrupt();
        stopThread.interrupt();
        logger.info("session admin is stop");
    }

    public void createSession(DeliverySessionCreationType data) {
        store.put(String.valueOf(data.getDeliverySessionId()), data);
        timeStartQue.add(data.getStartTime());
        timeStopQue.add(data.getStopTime());
        if (startIdRepl.get(String.valueOf(data.getStartTime())) == null) {
            startIdRepl.put(String.valueOf(data.getStartTime()), new HashSet<>());
        }
        startIdRepl.get(String.valueOf(data.getStartTime())).add(String.valueOf(data.getDeliverySessionId()));
        if (stopIdRepl.get(String.valueOf(data.getStopTime())) == null) {
            stopIdRepl.put(String.valueOf(data.getStopTime()), new HashSet<>());
        }
        stopIdRepl.get(String.valueOf(data.getStopTime())).add(String.valueOf(data.getDeliverySessionId()));
    }

    public void putSession(String sessionId, DeliverySessionCreationType data) {
        DeliverySessionCreationType oldData = store.get(sessionId);
        if (oldData == null) {
            logger.info("this session is not exist");
            return;
        }
        if (data.getStartTime() < timeStartQue.peek()) {
            logger.info("this session is started can not update start time");
            return;
        }
        if (data.getStopTime() < timeStopQue.peek()) {
            logger.info("this session is end can not update end time");
            return;
        }
        // 移除以前的 然后添加
        startIdRepl.remove(String.valueOf(oldData.getStartTime()));
        if (startIdRepl.get(String.valueOf(data.getStartTime())) == null) {
            startIdRepl.put(String.valueOf(data.getStartTime()), new HashSet<>());
        }
        startIdRepl.get(String.valueOf(data.getStartTime())).add(String.valueOf(data.getDeliverySessionId()));

        stopIdRepl.remove(String.valueOf(oldData.getStartTime()));
        if (stopIdRepl.get(String.valueOf(data.getStopTime())) == null) {
            stopIdRepl.put(String.valueOf(data.getStopTime()), new HashSet<>());
        }
        stopIdRepl.get(String.valueOf(data.getStopTime())).add(String.valueOf(data.getDeliverySessionId()));
    }

    private void sendRequest(long time, String type) {
        Set<String> ids = null;
        ActionType actionType = ActionType.fromValue("Start");
        if (type.equals("start")) {
            ids = startIdRepl.get(String.valueOf(time));
        }
        if (type.equals("stop")) {
            ids = stopIdRepl.get(String.valueOf(time));
            actionType = ActionType.fromValue("Stop");
        }
        // 根据ids获取会话内容 构建发送请求
        for (String id : ids) {
            DeliverySessionCreationType creationType = store.get(id);
            creationType.setAction(actionType);
            System.out.println(creationType.toString());
            bootStrapAdmin.sendRequest(factory.createReqPost(creationType));
        }
    }

    public void test() {
        for (Map.Entry<String, Set<String>> entry : startIdRepl.entrySet()) {
            System.out.println("the key is " + entry.getKey());
            for (String str : entry.getValue()) {
                System.out.print(str + "  ");
            }
            System.out.println();
        }
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

        for (Map.Entry<String, Set<String>> entry : stopIdRepl.entrySet()) {
            System.out.println("the key is " + entry.getKey());
            for (String str : entry.getValue()) {
                System.out.print(str + "  ");
            }
            System.out.println();
        }
    }

    public void test2() {
        while (timeStartQue.size() > 0) {
            System.out.println(timeStartQue.poll());
        }
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
        while (timeStopQue.size() > 0) {
            System.out.println(timeStopQue.poll());
        }
    }
}
