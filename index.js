//全局变量
var ap_m = 0;
var cn_m = 0;
var ch_m = 0;
var pre_room = -1;
var select = null;
var avatar = null;
var user_id = null;
var access_token = null;
var operate_code = null;//操作码
var url_global = "http://2k4x28.natappfree.cc/";
var url_quick_find = url_global + "hnust_meeting_room_war/available";
var url_appoint_history = url_global + "hnust_meeting_room_war/record";
var url_post_form = url_global + "hnust_meeting_room_war/commitRecord";
var url_user = url_global + "hnust_meeting_room_war/WxUser";
var url_auth = url_global + "hnust_meeting_room_war/WxAuth";
var url_sign = url_global + "hnust_meeting_room_war/WxSign";
var url_cancel = url_global + "hnust_meeting_room_war/cancel";

/*返回结果数组*/
var result_room = new Array();//会议室
var result_start_time = new Array();//开始时间
var result_end_time = new Array();//结束时间
//var appoint_date = new Array();//预约发起时间
var check_status = new Array();//审核状态
var room_purpose = new Array();//用途
var conference_num = new Array();//会议室人数
var check_suggestion = new Array();//审核意见

var room = null;//会议室编号
var cn_time = null;//会议时长
var cn_num = null;//会议人数
var time_before = null;//会议开始时间
var time_after = null;//会议结束时间
/*function*/
//首页点击事件
function all_hide(){
    $("#appoint_manage_content").css("display","none");
    $("#appoint_manage").css("opacity","1");
    ap_m = 0;
    $("#conference_manage_content").css("display","none");
    $("#conference_manage").css("opacity","1");
    cn_m = 0;
    $("#check_manage_content").css("display","none");
    $("#check_manage").css("opacity","1");
    ch_m = 0;
}
function ap_show() {
    if(ap_m == 0){
        all_hide();
        $("#appoint_manage_content").css("display","block");
        $("#appoint_manage").css("opacity","0.7");
        ap_m = 1;
    }
    else if(ap_m == 1){
        all_hide();
    }
    else{
        alert("error");
    }
}
function cn_show() {
    if(cn_m == 0){
        all_hide();
        $("#conference_manage_content").css("display","block");
        $("#conference_manage").css("opacity","0.7");
        cn_m = 1;
    }
    else if(cn_m == 1){
        all_hide();
    }
    else{
        alert("error");
    }
}
function ch_show() {
    if(ch_m == 0){
        all_hide();
        $("#check_manage_content").css("display","block");
        $("#check_manage").css("opacity","0.7");
        ch_m = 1;
    }
    else if(ch_m == 1){
        all_hide();
    }
    else{
        alert("error");
    }
}
//获取地址栏参数
function getUrlParms(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null){
        return unescape(r[2]);
    }
    return null;
}
//获取cookie内容(仅限搜索结果)
function load_cookie() {
    clear_data();
    var result = $.cookie("find_result");
    result = JSON.parse(result);
    console.log(typeof result);
    console.log(result);
    var array_lenth = result.length;
    var node = null;
    console.log("Rlenth: " + array_lenth);
    for(node = 0 ; node < array_lenth ; node ++){
        result_room[node] = result[node].room;//
        //console.log(result_room[node] + "-" + result[node].room);
        result_start_time[node] =result[node].conference_begin;//开始时间
        //console.log(result_start_time[node] + "-" + result[node].conference_begin);
        result_end_time[node] = result[node].conference_end;//结束时间
        //console.log(result_end_time[node] + "-" + result[node].conference_end);
    }
}
//通用开发中弹窗
function under_construction() {
    alert("非常抱歉，此功能正在开发中");
}
//更新搜索界面(方法过于复杂) ok
function update_find_result() {
    load_cookie();
    show_loading();
    var page_content = document.getElementById("find_result_container");//父容器
    var temp_time = new Date(0);//时间寄存用于比较
    var array_lenth = result_room.length;
    for(var node = 0 ; node < array_lenth ; node ++){
        var flag = 0;
        var start_time = new Date(result_start_time[node]);
        var end_time = new Date(result_end_time[node]);
        //放置日期头（年 月 日）
        if(start_time.getFullYear() != temp_time.getFullYear() || start_time.getMonth() != temp_time.getMonth() || start_time.getDate() != temp_time.getDate()){
            var flex_title = document.createElement("div");//创建一个flex_title div容器
            flex_title.setAttribute("class","flex_title");
            flex_title.append(start_time.getFullYear() + "年" + (((start_time.getMonth()+1)<10)?("0" + (start_time.getMonth()+1)):(start_time.getMonth()+1)) +
                        "月" + ((start_time.getDate()<10)?("0" + start_time.getDate()):start_time.getDate()) + "日");
            page_content.append(flex_title);
            temp_time = start_time;//更新时间比较容器
            flag = 1;
        }
        //UI分割行
        if(flag == 0){
            var line = document.createElement("div");
            line.setAttribute("class","line");
        }
        //放置会议室信息
        var flex_content = document.createElement("div");//创建一个flex_content div容器
        var flex_content_item = document.createElement("div");//创建一个flex_content_item div容器
        var flex_item = document.createElement("div");//创建一个flex_item div容器
        var flex_sub_item = document.createElement("div");//创建一个flex_sub_item div容器
        var tag = document.createElement("img");
        tag.setAttribute("src","IMG/go.png");
        tag.setAttribute("class","flex_go");
        flex_content.setAttribute("class","flex_content");//class
        flex_content_item.setAttribute("class","flex_content_item");//class
        flex_content_item.setAttribute("onclick","save_appoint_form("+ node + ")");//onclick
        flex_item.setAttribute( "class","flex_item");//class
        flex_sub_item.setAttribute("class","flex_sub_item");//class
        flex_sub_item.append(((start_time.getHours()<10)?("0" + start_time.getHours()):start_time.getHours()) +
                                ":" + ((start_time.getMinutes()<10)?("0" + start_time.getMinutes()):start_time.getMinutes()) +
                                "-" + ((end_time.getHours()<10)?("0" + end_time.getHours()):end_time.getHours()) +
                                ":" + ((end_time.getMinutes()<10)?("0" + end_time.getMinutes()):end_time.getMinutes()));//填充起始时间
        //第一层套娃
        flex_item.append(result_room[node]);
        flex_item.append(flex_sub_item);
        //第二层套娃
        flex_content_item.append(flex_item);
        flex_content_item.append(tag);
        //第三层套娃
        flex_content.append(flex_content_item);
        //最外层套娃
        page_content.append(flex_content);
        hide_loading();
    }
}
//更新预约记录界面 ok
function update_appoint_history() {
    var node = null;//数组指针
    var array_lenth = result_room.length;
    var container = document.getElementById("appoint_history_container");//父容器
    for(node = 0 ; node < array_lenth ; node ++){
        var lamp = null;
        var flag = 0;
        var flag2 = 0;
        var status = "";
        var start_time = new Date(result_start_time[node]);
        var end_time = new Date(result_end_time[node]);
        var page_button_main = document.createElement("div");//主容器
        var infomation = document.createElement("div");//次级容器
        page_button_main.setAttribute("class","page_button_main");
        if(check_status[node] == 1){
            status = "审核通过";
            lamp = "lamp_success";
            flag2 = 1;
        }else if(check_status[node] == 0){
            status = "审核中";
            lamp = "lamp_wait";
            flag2 = 1;
        }else if(check_status[node] == 2){
            status = "审核不通过";
            lamp = "lamp_fail";
        }else if(check_status[node] == 3){
            status = "过期";
            flag = 1;
        }else{
            status = "未知";
            flag = 1;
        }
        infomation.setAttribute("class","flex");
        infomation.setAttribute("id","room_" + node);
        infomation.innerHTML = "<div class=\"flex_item\">\n" +
            "                <div>"+ result_room[node] +"</div>\n" +
            "                <div class = \"flex_sub_item\">"+ start_time.getFullYear() + "." +
                                                            (((start_time.getMonth()+1)<10)?("0" + (start_time.getMonth()+1)):(start_time.getMonth()+1)) + "." +
                                                            ((start_time.getDate()<10)?("0" + start_time.getDate()):start_time.getDate()) +"</div>\n" +
            "                <div class = \"flex_sub_item\">"+((start_time.getHours()<10)?("0" + start_time.getHours()):start_time.getHours()) + ":" +
                                                            ((start_time.getMinutes()<10)?("0" + start_time.getMinutes()):start_time.getMinutes()) + "-" +
                                                            ((end_time.getHours()<10)?("0" + end_time.getHours()):end_time.getHours()) + ":" +
                                                            ((end_time.getMinutes()<10)?("0" + end_time.getMinutes()):end_time.getMinutes()) +"</div>\n" +
            "            </div>\n" +
            "            <div class = \"flex_status\">\n" +
            "                <div class = \"flex_status_lamp " + lamp + "\"></div>\n" +
            "                <div>"+ status +"</div>\n" +
            "            </div>\n" +
            "            <img class=\"flex_go\" src=\"IMG/go.png\">";
        page_button_main.append(infomation);
        if(flag == 0){
            infomation.setAttribute("onclick","history_show(" + node +")");
            //重新定义变量
            infomation = document.createElement("div");
            infomation.setAttribute("class","flex_content room_content");
            infomation.setAttribute("id","room_" + node + "_content");
            //有取消预约功能
            if(flag2 == 1){
                infomation.innerHTML = "<div class=\"flex_item\">\n" +
                    "                <div class = \"flex_sub_item\">人数：" + conference_num[node] + "人</div>\n" +
                    "                <div class = \"flex_sub_item\">用途：" + room_purpose[node] + "</div>\n" +
                    "                <div class = \"flex_sub_item\">审核意见：</div>\n" +
                    "                <div class = \"flex_textarea check_suggest\">\n" +
                    "                    <div class = \"flex_sub_item check_suggest_content\">" + check_suggestion[node] + "</div>\n" +
                    "                </div>\n" +
                    "                <div class = \"page_sub_function_button\" onclick=\"appoint_cancel(" + node + ")\">取消预约</div>"+
                    "            </div>";
            }
            //无取消预约功能
            else{
                infomation.innerHTML = "<div class=\"flex_item\">\n" +
                    "                <div class = \"flex_sub_item\">人数：" + conference_num[node] + "人</div>\n" +
                    "                <div class = \"flex_sub_item\">用途：" + room_purpose[node] + "</div>\n" +
                    "                <div class = \"flex_sub_item\">审核意见：</div>\n" +
                    "                <div class = \"flex_textarea check_suggest\">\n" +
                    "                    <div class = \"flex_sub_item check_suggest_content\">" + check_suggestion[node] + "</div>\n" +
                    "                </div>\n" +
                    "            </div>";
            }
            page_button_main.append(infomation);
        }
        //$("#appoint_history_container").append(page_button_main);
        container.append(page_button_main);
    }
}
//整合预约信息并跳转到预约表
function save_appoint_form(node) {
    $.cookie("operate_code",node);
    redirect_pull_appoint_form();
}
//填充预约信息
function get_appoint_form() {
    load_cookie();
    cn_time = $.cookie("cn_time");
    cn_num = $.cookie("cn_num");
    operate_code = $.cookie("operate_code");
    var start_time = new Date(result_start_time[operate_code]);
    var end_time = new Date(result_end_time[operate_code]);
    //获取默认信息并填入
    $("#appoint_durance").text(cn_time);
    $("#appoint_num").text(cn_num);
    $("#appoint_begin").text(((start_time.getHours()<10)?("0" + start_time.getHours()):start_time.getHours()) + ":" +
                            ((start_time.getMinutes()<10)?("0" + start_time.getMinutes()):start_time.getMinutes()));
    $("#appoint_after").text(((end_time.getHours()<10)?("0" + end_time.getHours()):end_time.getHours()) + ":" +
                            ((end_time.getMinutes()<10)?("0" + end_time.getMinutes()):end_time.getMinutes()));
    $("#appoint_room").text(result_room[operate_code]);
}
//预约历史点击事件
function history_show(room) {
    if(room == pre_room){
        $("#room_"+ room + "_content").css("display","none");
        pre_room = -1;
    }
    else{
        $("#room_"+ pre_room + "_content").css("display","none");
        $("#room_"+ room + "_content").css("display","block");
        pre_room = room;
    }
}
//清理缓存数据
function clear_data() {
    result_room =[];//会议室
    result_start_time = [];//开始时间
    result_end_time = [];//结束时间
    //appoint_date = [];//预约发起时间
    check_status = [];//审核状态
    room_purpose = [];//用途
    conference_num = [];//会议室人数
    check_suggestion = [];//审核意见
}
/*redirect*/
function redirect_quick_find() {
    window.location.href = "quick_find.html";
}
function redirect_appoint_history() {
    window.location.href = "appoint_history.html";
}
function redirect_find_result() {
    window.location.href = "find_result.html";
}
function redirect_pull_appoint_form() {
    window.location.href = "pull_appoint_form.html";
}
function redirect_index() {
    window.location.href = "index.html";
}
/*pull*/
//发起搜索请求
function quick_find_search() {
    show_loading();
    cn_time = $("#form_duration").val();//会议时长
    cn_num = $("#appointment_num").val();//会议人数
    time_before = ($("#form_time_before").val()==0)?"":$("#form_time_before").val();//会议开始时间
    time_after = ($("#form_time_after").val()==0)?"":$("#form_time_after").val();//会议结束时间
    room = $("#form_room option:selected").text();//会议室编号
    $.cookie("cn_time",cn_time);
    $.cookie("cn_num",cn_num);
   //var room_value = $("#form_room").val();//会议室选择框真值
    if (cn_num == "") {
        alert("请输入人数");
        return;
    } else {
        if (Number(time_before)> 0 && Number(time_after <= 0)) {
            alert("请选择结束时间");
        } else {
            //提交数据
            setTimeout(function () {
                clear_data();
                $.ajax({
                    url:url_quick_find,
                    method :"POST",
                    dataType : "JSON",
                    async:false,
                    data:{
                        conference_durance:cn_time,
                        conference_num:cn_num,
                        conference_begin:time_before,
                        conference_end: time_after ,
                        room:room//null
                    },
                    success:function (result) {
                        //获取返回参数，设置html文本内容
                        var array_lenth = result.length;
                        //alert("pause");
                        if(array_lenth == 0){
                            alert("抱歉，没有找到合适的会议室");
                            hide_loading();
                            return;
                        }
                        $.cookie("find_result",JSON.stringify(result));
                        hide_loading();
                        redirect_find_result();
                    }
                });
            },100);
        }
    }
}
//提交预约表 ok
function pull_appoint_form() {
    show_loading();
    var form_room = result_room[operate_code] + "";//会议室
    var reg =/[\u4e00-\u9fa5]/g;
    form_room = form_room.replace(reg,"");
    var form_conference_num = cn_num;
    var form_start_time = result_start_time[operate_code];//开始时间
    var form_end_time = result_end_time[operate_code];//结束时间
    var form_room_purpose = $("#room_purpose").val();//会议室用途
    var form_phone_num = $("#appoint_phone").val();//手机号
    var form_conncet = $("#appoint_connect_method").val() + ": " + $("#appoint_connect").val();//联系方式
    //console.log(form_room + "-" + form_conference_num + "-" + form_start_time + "-" + form_end_time+ "-" +form_room_purpose+ "-" +form_phone_num+ "-" +form_conncet);
    //提交
    setTimeout(function () {
        user_id = $.cookie("uid");
        if(user_id == ""){
            alert("用户认证失败，即将返回主界面重新认证");
            hide_loading();
            redirect_index();
        }
        $.ajax({
            url : url_post_form,
            method : "POST",
            dataType : "JSON",
            async: false,
            data :{
                uid : user_id ,
                room: form_room ,
                conference_num: form_conference_num,
                conference_begin : form_start_time ,
                conference_end : form_end_time ,
                room_purpose : form_room_purpose ,
                phone_num : form_phone_num ,
                connect_method : form_conncet
            },
            success : function (result) {
                if(result.status == "success"){
                    alert("提交成功");
                    hide_loading();
                    redirect_appoint_history();
                }else{
                    alert("发生未知错误，请重试");
                    hide_loading();
                }
            }
        });
    },100);
}
//发起取消预约请求
function appoint_cancel(operate_code){
    var confirm = window.confirm("是否确定取消预约？");
    if(confirm == true){
        confirm = window.confirm("确定要取消吗？");
        if(confirm == true){
            //发送取消预约请求
            show_loading();
            setTimeout(function () {
                var form_room = result_room[operate_code];
                var form_start_time = result_start_time[operate_code];
                var reg =/[\u4e00-\u9fa5]/g;
                form_room = form_room.replace(reg,"");
                $.ajax({
                    url:url_cancel,
                    dataType:"JSON",
                    async:false,
                    method:"POST",
                    data:{
                        room:form_room,
                        conference_begin:form_start_time
                    },
                    success : function (result) {
                        //获取返回状态
                        if(result.status == "success"){
                            alert("取消成功");
                            $("#room_"+ operate_code + "_content").remove();
                            $("#room_"+ operate_code).remove();
                            hide_loading();
                        }else{
                            alert("发生未知错误，请重试");
                            hide_loading();
                        }
                    }
                });
            },100);
        }
    }
}
/*request*/
//获取uid
function load() {
    show_loading();
    //获取随机串
    var createNonceStr = function() {
        return Math.random().toString(36).substr(2, 15);
    };

    // timestamp
    var createTimeStamp = function () {
        return parseInt(new Date().getTime() / 1000) + '';
    };
    user_id = "";
    $.cookie("uid","");//清空uid
    $.cookie("token","");//清空access token
    //release
    setTimeout(function () {
        $.ajax({
            url: url_sign,
            dataType:"JSON",
            method: 'POST', //HTTP请求类型
            data: {
                url:location.href.split('#')[0], //url 如果写的是固定的值的话，分享之后在分享会报错
                timestamp: createTimeStamp,
                nonce_str: createNonceStr
            },
            timeout: 10000, //超时时间设置为10秒；
            success: function(data) {
                wx.config({
                    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                    appId: "wxe400285c5b59f371", // 必填，公众号的唯一标识
                    timestamp: data.timestamp , // 必填，生成签名的时间戳
                    nonceStr: data.nonceStr, // 必填，生成签名的随机串
                    signature: data.signature,// 必填，签名，见附录1
                    jsApiList: [] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                });
                var auths = new Array();
                $.ajax({
                    url:url_auth,
                    async:false,
                    method : "POST",
                    dataType: "JSON",
                    data:{
                        url:location.href.split('#')[0],
                        code:getUrlParms("code")
                    },
                    timeout:100000,
                    success : function (data) {
                        auths[0] = data.access_token;
                        auths[1] = data.openid;
                        user_id = data.openid;
                        access_token = data.access_token;
                        $.ajax({
                            url:url_user,
                            method: "POST",
                            dataType:"JSON",
                            data:{
                                url : location.href.split('#')[0],
                                access_token: auths[0],
                                openid:auths[1]
                            },
                            success:function () {
                                    hide_loading();
                            }
                        });
                    }
                });
            }
        });
    },100);
    //save
    $.cookie("uid",user_id);
    $.cookie("token",access_token);
    console.log("userid: " + user_id);
}
//获取预约记录
function get_appoint_history() {
    show_loading();
    setTimeout(function () {
        user_id = $.cookie("uid");
        console.log("uid: " +user_id);
        $.ajax({
            url : url_appoint_history,
            method : "POST",
            async: false,
            dataType : "JSON",
            data :{
                uid: user_id
            },
            success:function (result) {
                //将数据赋值给全局
                //let result = JSON.parse(message);
                let array_lenth = result.length;
                clear_data();
                for(let node = 0 ; node <  array_lenth; node ++){
                    result_room[node] =result[node].room;//会议室
                    result_start_time[node] = result[node].conference_begin;//开始时间
                    result_end_time[node] = result[node].conference_end;//结束时间
                    //appoint_date = "";//预约发起时间
                    check_status[node] = result[node].check_status;//审核状态
                    room_purpose[node] = result[node].room_purpose;//用途
                    conference_num[node] = result[node].conference_num;//会议室人数
                    //conference_num[node] = "NULL";
                    check_suggestion[node] = result[node].check_suggestion;//审核意见
                    //check_suggestion[node] = "NULL";
                }
                //更新界面
                hide_loading();
                update_appoint_history();
            }
        });
    },100);

}
/*update interface*/
//限制结束时间不高于开始时间
function time_limit(){
    var time_before = $("#form_time_before").val();
    var time_after_null = document.getElementById("form_time_after_null");
    var form_time_after = document.getElementById("form_time_after");
    if(time_before != "0"){
        var time_after;
        form_time_after.innerHTML = null;
        var select_struct = new Option();
        select_struct.textContent = "";
        select_struct.value = "0";
        form_time_after.append(select_struct);
        for(time_after = Number(time_before) + 1 ; Number(time_after) <= 22 ; time_after = Number(time_after) + 1){
            var select_option = new Option();
            if(Number(time_after) >= 10){
                select_option.textContent = time_after;
                select_option.value = time_after;
                form_time_after.append(select_option);
            }
            else{
                select_option.textContent = "0" + time_after;
                select_option.value = time_after;
                form_time_after.append(select_option);
            }

        }
        $(time_after_null).remove();
    }
    else{
        form_time_after.innerHTML = null;
        select_struct = new Option();
        select_struct.textContent = "";
        select_struct.value = "0";
        console.log(select_struct);
        form_time_after.append(select_struct);
        var select_struct = new Option();
        select_struct.textContent = "请选择开始时间";
        select_struct.value = "-1";
        select_struct.id = "form_time_after_null";
        select_struct.style = "font-size: 20px";
        form_time_after.append(select_struct);
    }
}
//显示加载动画
function show_loading() {
    $(".page_mask").css("display","block");
}
//隐藏加载动画
function hide_loading() {
    $(".page_mask").css("display","none");
}
