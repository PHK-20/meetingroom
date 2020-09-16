package cn.hnust.bean;

import java.io.Serializable;

/**
 * UserBean
 */
public class UserBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private String uId;
    private String userName;
    private String phoneNumber;
    private boolean isManager;
    private String userUrl;
    private String workID;


    public UserBean() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getWorkID() {
        return workID;
    }

    public void setWorkID(String workID) {
        this.workID = workID;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "uId='" + uId + '\'' +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isManager=" + isManager +
                ", userUrl='" + userUrl + '\'' +
                ", workID='" + workID + '\'' +
                '}';
    }
}