package cn.hnust.bean;

import java.io.Serializable;

public class RecordBean implements Serializable {
    private static final long serialVersionUID = 2L;
    private String id;
    private String uid;
    private String cid;
    private String num;
    private String state;
    private String start;
    private String end;
    private String purpose;
    private String othrcontact;
    private String phoneNum;

    public RecordBean() {
    }

    public RecordBean(String id, String uid, String cid, String num, String state, String start, String end, String purpose, String othrcontact, String phoneNum) {
        this.id = id;
        this.uid = uid;
        this.cid = cid;
        this.num = num;
        this.state = state;
        this.start = start;
        this.end = end;
        this.purpose = purpose;
        this.othrcontact = othrcontact;
        this.phoneNum = phoneNum;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getOthrcontact() {
        return othrcontact;
    }

    public void setOthrcontact(String othrcontact) {
        this.othrcontact = othrcontact;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "RecordBean{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", cid='" + cid + '\'' +
                ", num='" + num + '\'' +
                ", state='" + state + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", purpose='" + purpose + '\'' +
                ", othrcontact='" + othrcontact + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}
