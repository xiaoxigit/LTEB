package server;

import entity.DeliverySessionCreationType;
import forTest.ForTest;
import nettyServer.BootStrapAdmin;
import sessionServer.SessionAdmin;

import java.util.List;

public class ServerBoot {
    public static void main(String[] args) {
        SessionAdmin sessionAdmin = new SessionAdmin();
        // 启动服务
        sessionAdmin.start();
        // 构建
        List<DeliverySessionCreationType> session = ForTest.forTestAdmin();
        for(DeliverySessionCreationType se:session){
            sessionAdmin.createSession(se);
        }
    }
}
