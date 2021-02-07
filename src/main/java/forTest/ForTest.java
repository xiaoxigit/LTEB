package forTest;

import entity.*;

import java.util.ArrayList;
import java.util.List;

public class ForTest {
    public static List<DeliverySessionCreationType> forTestAdmin() {
        XMLObjectFactory factory = new XMLObjectFactory();
        List<DeliverySessionCreationType> result = new ArrayList<>();
        for(int i = 0;i<10;i++) {
            DeliverySessionCreationType data = factory.createDeliverySessionCreationType();
            data.setStartTime(System.currentTimeMillis()+10*(i+1)*1000);
            data.setStopTime(System.currentTimeMillis()+20*(i+1)*1000);
            data.setDeliverySessionId(i);
            data.setVersion("xxx");
            TMGIPool tmgi = new TMGIPool();
            tmgi.setValue("xxx");
            ActionType actionType = ActionType.fromValue(ActionType.START.value());
            data.setAction(actionType);
            data.setTmgiPoolOrtTmgi(tmgi);
           //  System.out.println(factory.getXml(data));
            result.add(data);
        }
        return result;
    }
}
