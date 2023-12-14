package com.hspedu.qqcilen.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.*;

public class MessageClientService {
    public void sendOneMessage(String content,String sendId,String getId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(sendId);
        message.setGetter(getId);
        message.setContent(content);
        message.setSendTime(new java.util.Date().toString());//发送时间
        System.out.println(sendId+"对"+getId+"说"+content);
//发送给服务端
        try {
            ObjectOutputStream oos= new ObjectOutputStream(ManageClientConnectServerThread.
                    getclientConnectServerThread(sendId)
                    .getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
        }
    }
    public void sendMessageToAll(String content,String senderId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
        message.setSender(senderId);
        message.setContent(content);
        message.setSendTime(new java.util.Date().toString());//发送时间
        System.out.println(senderId+"对家大说"+content);
//发送给服务端
        try {
            ObjectOutputStream oos= new ObjectOutputStream(ManageClientConnectServerThread.
                    getclientConnectServerThread(senderId)
                    .getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
        }

    }


}
