package entity;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlType(name = "DeliverySessionCreationType", propOrder = {
    "deliverySessionId",
    "action",
    "tmgiPoolOrtTmgi",
    "startTime",
    "stopTime"
})
public class DeliverySessionCreationType {

    @XmlElement(name = "DeliverySessionId")
    @XmlSchemaType(name = "unsignedInt")
    protected long deliverySessionId;
    @XmlElement(name = "Action", required = true)
    @XmlSchemaType(name = "string")
    protected ActionType action;
    @XmlElements({
        @XmlElement(name ="TMGIPool",type = TMGIPool.class),
        @XmlElement(name ="TMGI",type = TMGI.class)
    })
    protected Object tmgiPoolOrtTmgi;

    @XmlElement(name = "StartTime")
    @XmlSchemaType(name = "unsignedInt")
    protected Long startTime;
    @XmlElement(name = "StopTime")
    @XmlSchemaType(name = "unsignedInt")
    protected Long stopTime;
    @XmlAttribute(name = "Version", required = true)
    protected String version;


    public long getDeliverySessionId() {
        return deliverySessionId;
    }

    public void setDeliverySessionId(long value) {
        this.deliverySessionId = value;
    }

    public ActionType getAction() {
        return action;
    }


    public void setAction(ActionType value) {
        this.action = value;
    }

    public Object getTmgiPoolOrtTmgi() {
        return tmgiPoolOrtTmgi;
    }

    public void setTmgiPoolOrtTmgi(Object tmgiPoolOrtTmgi) {
        this.tmgiPoolOrtTmgi = tmgiPoolOrtTmgi;
    }

    public void setTmgiPoolOrtTmgi(String tmgiPoolOrtTmgi) {
        this.tmgiPoolOrtTmgi = tmgiPoolOrtTmgi;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long value) {
        this.startTime = value;
    }

    public Long getStopTime() {
        return stopTime;
    }

    public void setStopTime(Long value) {
        this.stopTime = value;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String value) {
        this.version = value;
    }

    @Override
    public String toString() {
        return "DeliverySessionCreationType{" +
                "deliverySessionId=" + deliverySessionId +
                ", action=" + action +
                ", tmgiPoolOrtTmgi=" + tmgiPoolOrtTmgi +
                ", startTime=" + startTime +
                ", stopTime=" + stopTime +
                ", version='" + version + '\'' +
                '}';
    }
}
