package cn.hnust.servlet;


import cn.hnust.bean.ConferenceBean;
import cn.hnust.bean.UserBean;
import cn.hnust.jdbc.MongoDBHelper;
import cn.hnust.jdbc.MysqlConnection;
import cn.hnust.utils.FormatUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(
        name = "RecordServlet",
        urlPatterns = {"/record"},
        loadOnStartup = 1
)
public class RecordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uid = req.getParameter("uid");
        JSONArray result = new JSONArray();
        MysqlConnection connection = MysqlConnection.getMySQLConnection();
        // search all record by uid, sorted by (start,state)
        String json = new MongoDBHelper().retrieveRecordByUid(uid);
        JSONArray objects = JSON.parseArray(json);
        for (Object object : objects) {
            System.out.println(object.toString());
            JSONObject jsonObject = JSON.parseObject(object.toString());
            String cid = (String) jsonObject.get("cid");
            ConferenceBean conferenceBean = null;
            UserBean userBean = null;
            try {
                conferenceBean = (ConferenceBean) connection.query("conference", "cid", cid);
                userBean = (UserBean) connection.query("user", "uid", uid);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JSONObject oneRecord = new JSONObject();
            oneRecord.put("room", conferenceBean.getAddress());
            oneRecord.put("appoint_date", FormatUtils.parseDate(jsonObject.getString("start")));
            oneRecord.put("conference_begin", FormatUtils.parseDate(jsonObject.getString("start")));
            oneRecord.put("conference_end", FormatUtils.parseDate(jsonObject.getString("end")));
            oneRecord.put("room_purpose", jsonObject.get("state"));
            oneRecord.put("conference_num", jsonObject.get("num"));
            oneRecord.put("headurl", userBean.getUserUrl());
            result.add(oneRecord);
        }
        resp.getWriter().println(result.toJSONString());
    }

}
