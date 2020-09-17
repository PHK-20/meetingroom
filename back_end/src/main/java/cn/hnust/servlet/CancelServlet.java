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
        name = "CancelServlet",
        urlPatterns = {"/cancel"},
        loadOnStartup = 1
)
public class CancelServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String room = req.getParameter("room");
        String conferenceBegin = req.getParameter("conference_begin");
        System.out.println(room+"-"+conferenceBegin);
        try {
            conferenceBegin = Long.toString(FormatUtils.format(conferenceBegin).getTime()/1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(room+"-"+conferenceBegin);
        MysqlConnection connection = MysqlConnection.getMySQLConnection();
        ConferenceBean conferenceBean = null;
        try {
            conferenceBean = (ConferenceBean) connection.query("conference", "address", "逸夫楼" + room);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            connection.close();
        }

        //Delete a record
        boolean b = false;
        if (conferenceBean != null) {
            String cid = conferenceBean.getCid();
            b = new MongoDBHelper().delOneRecord(cid + "-" + conferenceBegin);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", b ? "success" : "error");
        resp.getWriter().println(jsonObject.toJSONString());
    }
}
