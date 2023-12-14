package com.hspedu.qqcommon;
/***
 * 表示消息类型
 */
public interface MessageType {
    String MESSAGE_LOGINF_SUCCEED = "1";
    String MESSAGE_LOGINF_Fail = "2";
    String MESSAGE_COMM_MES = "3";//普通信息包
    String MESSAGE_GET_ONLINE_FRIEND = "4";//要求返回在线用户列表
    String MESSAGE_RETURN_ONLINE_FRIEND = "5";//返回在线用户列表
    String MESSAGE_CLIENT_EXIT= "6";//客户端请求退出
    String MESSAGE_TO_ALL_MES= "7";//群发消息
    String MESSAGE_File_MES= "8";//文件消息发送

}
