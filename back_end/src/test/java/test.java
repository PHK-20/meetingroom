import cn.hnust.bean.ConferenceBean;
import cn.hnust.jdbc.MongoDBHelper;
import cn.hnust.jdbc.MysqlConnection;
import org.json.JSONArray;
import org.junit.Test;

import java.sql.SQLException;

/**
 * test
 */
public class test {
    @Test
    public void test01() {
        MongoDBHelper mongoDBHelper = new MongoDBHelper();
        String json = mongoDBHelper.retrieveRecordByUid("oshZxwqq80NiVnet2PBrStSs3kmg");
        System.out.println(json);
        JSONArray objects = new JSONArray(json);
        for (Object object : objects) {
            System.out.println(object.toString());
        }
    }
    @Test
    public void test02(){
        ConferenceBean conferenceBean = null;
        try {
            conferenceBean = (ConferenceBean) MysqlConnection.getMySQLConnection().query("conference", "cid", "001");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(conferenceBean);
    }
}