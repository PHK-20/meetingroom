package cn.hnust.jdbc;

import cn.hnust.bean.RecordBean;
import cn.hnust.room.MeetingRoomBean;

/**
 * dbAPI
 */
@Deprecated
interface dbAPI {
    /**
     * 把会议室信息加入到数据库
     * 
     * @param room
     * @return 返回是否加入成功
     */
    public boolean addMeetingRoom(MeetingRoomBean room);

    /**
     * 从数据库中删除某个id数据库
     * @param roomId
     * @return返回是否删除成功
     */
    public boolean removeMeetingRoom(String roomId);

    /**
     * 删除指定用户
     *
     * @param uid
     * @return
     */
    public boolean removeUser(String uid);

    public boolean insertUser(String uid,
                              String username,
                              String userPortraitUrl,
                              String phoneNum,
                              String isManager);

    public String retrievePhoneNumById(String uid);
    /**
     * 根据uid更新电话号码
     *
     * @param uid
     * @return
     */
    public boolean updatePhoneNumById(String uid, String phoneNum);

    /**
     * 修改预约状态,修改会议结束的记录，
     * @param state 状态信息
     * @param newState  修改的新状态
     * @return  是否修改成功
     */
    public boolean modifySubscriptionState(String state, String newState);

    /**
     * 更新状态为占用或者审核未通过
     * @param state 更新的状态
     * @param uid   对应的uid
     * @return
     */
    public boolean modifySubscriptionStateByUid(String state, String uid);
    /**
     * 获取占用信息
     * @return 占用信息json字符串
     */
    public String retrieveRoomRecord();

    /**
     * 获取某个uid对应用户的预约记录
     * @param uid
     * @return
     */
    String subscriptionRecord(String uid);

    /**
     * 获取审核记录,或去的数据是根据状态进行排序
     * 0（审核中） 1（审核未通过） 2（占用） 3（会议结束）
     * @return 返回审核记录json字符串
     */
    public String trialRecord();

    /**
     * 用户请求占用会议室信息插入相应的数据库
     *
     * @param uid
     * @param cid
     * @param num
     * @param state
     * @param start
     * @param end
     * @param purpose
     * @return
     */
    public boolean insertSubscription(String uid, String cid, String num, String state, String start, String end, String purpose);
    public boolean insertSubscription(RecordBean record);

    public String retrieveAllElement(String collectionName);
    public String retrieveAllUser();
    public String retrieveAllConference();
    public String retrieveAllSubscription();



}