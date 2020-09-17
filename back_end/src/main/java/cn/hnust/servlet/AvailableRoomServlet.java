package cn.hnust.servlet;

import cn.hnust.bean.ConferenceBean;
import cn.hnust.jdbc.MongoDBHelper;
import cn.hnust.jdbc.MysqlConnection;
import cn.hnust.utils.FormatUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.annotations.JsonAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@WebServlet(
        name = "AvailableRoomServlet",
        urlPatterns = {"/available"},
        loadOnStartup = 1
)
public class AvailableRoomServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // nothing
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String conferenceNum = req.getParameter("conference_num");
        String conferenceBegin = req.getParameter("conference_begin");
        if (conferenceBegin == null) {
            //默认开始时间
            conferenceBegin = "8";
        }
        Date startDate = addHourInDate(Integer.parseInt(conferenceBegin));
        String conferenceDurance = req.getParameter("conference_durance");
        Date endDate = addEndurance(startDate, Integer.parseInt(conferenceDurance));
        String conferenceEnd = req.getParameter("conference_end");
        if (conferenceEnd == null) {
            //默认期限
            conferenceEnd = "23";
        }
        String limit = FormatUtils.date2String(addHourInDate(Integer.parseInt(conferenceEnd)));
        String room = req.getParameter("room");
        JSONArray jsonArr = new JSONArray();
        MongoDBHelper mongoDBHelper = new MongoDBHelper();
        System.out.println(conferenceBegin);
        if (room != null&&!"".equals(room)) {
            MysqlConnection connection = MysqlConnection.getMySQLConnection();
            ConferenceBean bean = null;
            try {
                bean = (ConferenceBean) connection.query("conference", "address", "逸夫楼" + room);
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                connection.close();
            }
            if (bean != null) {
                JSONObject dateJson = new MongoDBHelper().checkRoomAvailable(bean.getCid(), startDate, endDate, limit);
                JSONObject jsonObject = packJson(dateJson, bean);
                jsonArr.add(jsonObject);
            }
        }else{
            MysqlConnection connection = MysqlConnection.getMySQLConnection();
            List<Object> conferenceTble = null;
            try {
                conferenceTble = connection.queryAll("conference");
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                if (connection != null) {
                    connection.close();
                }
            }
            for (Object o : conferenceTble) {
                ConferenceBean conferenceBean = (ConferenceBean) o;
                JSONObject dateJson = mongoDBHelper.checkRoomAvailable(conferenceBean.getCid(), startDate,endDate, limit);
                JSONObject jsonObject = packJson(dateJson, conferenceBean);
                if (jsonObject != null) {
                    jsonArr.add(jsonObject);
                }

            }

        }
        resp.getWriter().println(jsonArr.toJSONString());
    }

    /**
     * 为json数组打包json对象
     * @param dateJson
     * @param conferenceBean
     * @return
     */
    private JSONObject packJson(JSONObject dateJson,ConferenceBean conferenceBean) {
        JSONObject json = null;
        if (dateJson.getString("start") != null) {
            json = new JSONObject();
            json.put("room", conferenceBean.getAddress());
            json.put("room_num", conferenceBean.getNum());
            json.put("conference_begin", FormatUtils.parseDate(dateJson.getString("start")));
            json.put("conference_end", FormatUtils.parseDate(dateJson.getString("end")));
        }
        return json;
    }

    /**
     * 今天的00:00:00加上hour为返回值
     * @param hour
     * @return
     */
    private Date addHourInDate(int hour){
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.add(Calendar.HOUR,hour);
        return calendar.getTime();
    }

    /**
     * 会议的开始字段+会议时长之后的Date
     * @param date
     * @param endurance
     * @return
     */
    private Date addEndurance(Date date, int endurance) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.MINUTE, endurance);
        return calender.getTime();
    }
}
