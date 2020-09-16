package cn.hnust.jdbc;

import cn.hnust.bean.RecordBean;
import cn.hnust.room.MeetingRoomBean;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;

public class MongoDBHelper {
    private static final String HOSTIP = "101.132.172.24";
    private static final int MONGODBPORT = 27017;
    private static final String DBNAME = "meeting";
    private static final String USER_COLLECTION = "user";
    private static final String CONFERENCE_COLLECTION = "conference";
    private static final String SUBSCRIBE_COLLECTION = "subscription";

    MongoDatabase mongoDatabase = null;
    MongoClient mongoClient = null;

    public boolean connect(){

        try{
            mongoClient = new MongoClient(HOSTIP, MONGODBPORT);
            mongoDatabase = mongoClient.getDatabase(DBNAME);
            System.out.println("连接成功");
        }catch (Exception e){
            return false;
        }
        return  true;
    }
    public boolean close(){
        try {
            if (mongoClient != null) {
                mongoClient.close();
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String retrieveAllElement(String collectionName){
        connect();
        StringBuffer json = new StringBuffer("[");
        Gson gson = new Gson();
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()){
                Document document = cursor.next();
                System.out.println(document.get("username"));
                json.append(gson.toJson(document));
            }
        } catch (Exception e) {
            return null;
        }
        json.append("]");
        close();
        return json.toString();
    }


    public String retrieveAllUser() {
        return retrieveAllElement(USER_COLLECTION);
    }


    public String retrieveAllConference() {
        return retrieveAllElement(CONFERENCE_COLLECTION);
    }


    public String retrieveAllSubscription() {
        return retrieveAllElement(SUBSCRIBE_COLLECTION);
    }


    public boolean removeUser(String uid) {
        connect();
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(USER_COLLECTION);
            collection.deleteOne(Filters.eq("_id", uid));
        } catch (Exception e) {
            return false;
        }finally {
            close();
        }

        return true;
    }

    public boolean insertUser(String uid,
                              String username,
                              String userPortraitUrl,
                              String phoneNum,
                              String isManager){
        connect();
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(USER_COLLECTION);

            Document document = new Document()
                    .append("_id", uid)
                    .append("username", username)
                    .append("userPortraitUrl", userPortraitUrl)
                    .append("phoneNum", phoneNum)
                    .append("isManager", isManager);

            collection.insertOne(document);
        } catch (Exception e) {
            return false;
        }finally {
            close();
        }

        return true;
    }

    public boolean updatePhoneNumById(String uid, String phoneNum) {
        connect();
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(USER_COLLECTION);
            collection.updateMany(Filters.eq("_id", uid),new Document("$set", new Document("phoneNum", phoneNum)));
        } catch (Exception e) {
            return false;
        }finally {
            close();
        }

        return true;
    }
    /**
     * 根据用户id获取电话号码
     * @param uid 用户id
     * @return
     */

    public String retrievePhoneNumById(String uid){
        connect();
        MongoCollection<Document> user = mongoDatabase.getCollection(USER_COLLECTION);
        MongoCursor<Document> cursor;
        String phoneNum;
        try {
            cursor = user.find(Filters.eq("_id", uid)).iterator();
            phoneNum = cursor.next().get("phoneNum").toString();
        } catch (Exception e) {
            System.out.println("There is not such element!");
            return null;
        }finally {
            close();
        }
        //System.out.println(phoneNum);
        return phoneNum;
    }



    public boolean addMeetingRoom(MeetingRoomBean room) {
        connect();

        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(CONFERENCE_COLLECTION);
            Document document = new Document()
                    .append("_id", room.getId())
                    .append("address", room.getAddress())
                    .append("start", room.getStart())
                    .append("end", room.getEnd())
                    .append("volume", room.getSize())
                    .append("state", room.isStatus());
            collection.insertOne(document);
        } catch (Exception e) {
            return false;
        } finally {
            close();
        }
        return false;
    }


    public boolean removeMeetingRoom(String id) {
        connect();
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(CONFERENCE_COLLECTION);
            collection.deleteOne(Filters.eq("_id", id));
        } catch (Exception e) {
            return false;
        }finally {
            close();
        }

        return true;
    }


    public boolean modifySubscriptionState(String state, String newState) {
        connect();
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(SUBSCRIBE_COLLECTION);
            collection.updateMany(Filters.eq("state", state), new Document("$set",new Document("state", newState)));
        } catch (Exception e) {
            return false;
        }finally {
            close();
        }

        return true;
    }

    public boolean modifySubscriptionStateByUid(String state, String uid) {

        connect();
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(SUBSCRIBE_COLLECTION);
            collection.updateMany(Filters.eq("uid", uid), new Document("$set",new Document("state", state)));
        } catch (Exception e) {
            return false;
        }finally {
            close();
        }

        return true;
    }

    public String retrieveRoomRecord() {

        connect();
        Gson gson = new Gson();
        StringBuffer json = new StringBuffer("[");

        MongoCollection<Document> collection = mongoDatabase.getCollection(SUBSCRIBE_COLLECTION);
        ArrayList<Document> total = new ArrayList<>();
        Document subMatch = new Document();
        Document condition = new Document();
        condition.append("$lte", "0");
        condition.append("$lte", "1");
        subMatch.append("state", condition);
        //subMatch.append("state","1");

        total.add(new Document("$match", subMatch));

        Document subLookupUser = new Document();
        subLookupUser.append("from",USER_COLLECTION);
        subLookupUser.append("localField","uid");
        subLookupUser.append("foreignField","_id");
        subLookupUser.append("as","user");
        //左连接
        total.add(new Document("$lookup",subLookupUser));
        Document subLookUpConference = new Document();
        subLookUpConference.append("from",CONFERENCE_COLLECTION);
        subLookUpConference.append("localField","cid");
        subLookUpConference.append("foreignField","_id");
        subLookUpConference.append("as", "room");
        total.add(new Document("$lookup",subLookUpConference));
        AggregateIterable<Document> aggregate = collection.aggregate(total);

        MongoCursor<Document> cursor = aggregate.iterator();
        while (cursor.hasNext()){
            Document document = cursor.next();
            json.append(gson.toJson(document));
        }
        json.append("]");
        System.out.println(json.toString());
        close();
        return json.toString();
    }

    public String subscriptionRecord(String uid) {
        connect();
        Gson gson = new Gson();
        StringBuffer json = new StringBuffer("[");
        MongoCollection<Document> collection = mongoDatabase.getCollection(SUBSCRIBE_COLLECTION);
        MongoCursor<Document> cursor = collection.find(Filters.eq("uid", uid)).iterator();
        while (cursor.hasNext()){
            Document document = cursor.next();
            json.append(gson.toJson(document));
        }
        json.append("]");
        close();
        return json.toString();
    }

    /**
     * 2020-06-03
     * 查询某个用户对应的预约记录
     * @param uid
     * @return
     */
    public String retrieveRecordByUid(String uid) {

        connect();
        Gson gson = new Gson();
        StringBuffer json = new StringBuffer("[");
        MongoCollection<Document> collection = mongoDatabase.getCollection(SUBSCRIBE_COLLECTION);
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("state", 1);
        dbObject.put("start", 1);
        MongoCursor<Document> cursor = collection.find().sort(dbObject).iterator();
        while (cursor.hasNext()){
            Document document = cursor.next();
            json.append(gson.toJson(document)).append(",");
        }
        json.append("]");
        close();
        return json.toString();
    }

    /**
     * 该方法改于2020-06-03，适于最新设计
     * @param uid
     * @param cid
     * @param num
     * @param state
     * @param start
     * @param end
     * @param purpose
     * @param phoneNum
     * @param othrContact
     * @return 返回插入是否成功，true or false
     */
    public boolean insertSubscription(String uid,
                                      String cid,
                                      String num,
                                      String state,
                                      String start,
                                      String end,
                                      String purpose,
                                      String phoneNum,
                                      String othrContact) {
        connect();
        try {
            MongoCollection<Document> collection = mongoDatabase.getCollection(SUBSCRIBE_COLLECTION);

            Document document = new Document()
                    .append("_id", cid + start)
                    .append("uid", uid)
                    .append("cid", cid)
                    .append("num", num)
                    .append("state", state)
                    .append("start", start)
                    .append("end", end)
                    .append("purpose", purpose)
                    .append("phoneNum", phoneNum)
                    .append("othrcontact", othrContact);

            collection.insertOne(document);
        } catch (Exception e) {
            return false;
        }finally {
            close();
        }

        return true;

    }

    /**
     * 2020-06-03版
     * @param record
     * @return
     */
    public boolean insertSubscription(RecordBean record) {
        return insertSubscription(record.getUid(), record.getCid(), record.getNum(), record.getState(), record.getStart(), record.getEnd(), record.getPurpose(), record.getPhoneNum(), record.getOthrcontact());
    }

    /**
     * This is useless
     * @param args
     */
    public static void main(String[] args) {

        MongoDBHelper mongoDBHelper = new MongoDBHelper();
        //System.out.println(mongoDBHelper.retrieveAllUser());
        ///System.out.println(mongoDBHelper.subscriptionRecord("as234d4rtgfs"));
        //mongoDBHelper.insertUser("1s1w34dsdsf", "小王", "http://xxxxxx.png","12312312312","0");
        //System.out.println(mongoDBHelper.retrievePhoneNumById("1s1w34dsdsf"));
        mongoDBHelper.retrieveRoomRecord();
    }
}