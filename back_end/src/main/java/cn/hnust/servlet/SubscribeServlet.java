package cn.hnust.servlet;


import cn.hnust.bean.ConferenceBean;
import cn.hnust.jdbc.MongoDBHelper;
import cn.hnust.jdbc.MysqlConnection;
import cn.hnust.utils.FormatUtils;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@WebServlet(
        name = "SubscribeServlet1",
        urlPatterns = {"/commitRecord"},
        loadOnStartup = 1
)
public class SubscribeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String uid = req.getParameter("uid");
        String conferenceDurance = req.getParameter("conference_durance");
        String conferenceNum = req.getParameter("conference_num");
        String conferenceBegin = req.getParameter("conference_begin");
        String conferenceEnd = req.getParameter("conference_end");
        String room = "逸夫楼" + req.getParameter("room");
        String roomPurpose = req.getParameter("room_purpose");
        String phoneNum = req.getParameter("phone_num");
        String connectMethod = req.getParameter("connect_method");
        //find conferenceId by address, here is called as room
        String cid = null;
        MysqlConnection connection = MysqlConnection.getMySQLConnection();
        System.out.println(uid + room);
        try {
            cid = ((ConferenceBean) connection.query("conference", "address", room)).getCid();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            connection.close();
        }

        try {
            conferenceBegin = Long.toString(FormatUtils.format(conferenceBegin).getTime());
            conferenceEnd = Long.toString(FormatUtils.format(conferenceEnd).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean status = new MongoDBHelper().insertSubscription(uid, cid, conferenceNum, "0", conferenceBegin, conferenceEnd, roomPurpose, phoneNum, connectMethod);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", status ? "success" : "error");
        resp.getWriter().println(jsonObject.toJSONString());
    }
}
