package cn.hnust.room;

/**
 * MeetingRoom 会议室信息
 */
public class MeetingRoomBean {
    private String id;
    private String address;
    private int size; // 容量
    private int start; // 开放时间
    private int end; // 结束时间
    private boolean status;// 会议室状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public MeetingRoomBean() {
    }

}