package entity;

import nettyServer.MainHandler;
import nettyServer.SessionRequest;
import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.net.URI;

public class XMLObjectFactory {
    private final Logger logger = LoggerFactory.getLogger(XMLObjectFactory.class);

    public DeliverySessionCreationType createDeliverySessionCreationType() {
        return new DeliverySessionCreationType();
    }

    public String getXml(DeliverySessionCreationType data) {
        try {
            JAXBContext context = JAXBContext.newInstance(DeliverySessionCreationType.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            StringWriter writer = new StringWriter();
            marshaller.marshal(data, writer);
            return writer.toString().replace("standalone=\"yes\"", "");
        } catch (Exception e) {
            logger.info("when get data xml error is {}", e);
            throw new RuntimeException(e);
        }
    }

    public SessionRequest createReqPost(DeliverySessionCreationType data) {
        try {
            String xmlContent = getXml(data);
            byte[] body = xmlContent.getBytes(Charsets.UTF_8);
            // 这里固定编码 指定服务器地址和uri
            String server = "127.0.0.1:8080";
            URI uri = new URI("/nbi/deliverysession?id=" + data.deliverySessionId);
            SessionRequest request = new SessionRequest(new MainHandler(body,"POST",uri.toString()));
            return request;
        } catch (Exception e) {
            logger.info("when create request error is {}", e);
            throw new RuntimeException(e);
        }
    }
}
