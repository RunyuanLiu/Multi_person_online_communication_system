package com.hspedu.qqserver.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class SendNewsToAllService implements Runnable{
    @Override
    public void run() {
        while (true) {//为了可以推送多次新闻可以使用while循环
            System.out.println("请输入服务器要推送的新闻/消息(输入exit表示退出推送服务)");
            String news = Utility.readString(10000);
            if ("exit".equals(news)){
                break;
            }
//        构建一个消息，群发消息
            Message message = new Message();
            message.setSender("服务器");
            message.setContent(news);
            message.setSendTime(new Date().toString());
            message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
            System.out.println("服务器推送消息给所有人说：" + news);
//        遍历当前所有的通信线程，得到socket，并转发message对象
            HashMap<String, ServerConnectClientThread> hm = ManageClientThread.getHm();
            Iterator<String> iterator = hm.keySet().iterator();
            while (iterator.hasNext()) {
                String onileUserId = iterator.next();
                Socket socket = hm.get(onileUserId).getSocket();
                try {
                    ObjectOutputStream obs = new ObjectOutputStream(socket.getOutputStream());
                    obs.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                }
            }
        }
    }
}
