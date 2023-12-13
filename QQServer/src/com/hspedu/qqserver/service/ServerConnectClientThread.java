package com.hspedu.qqserver.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String userId;
    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {//线程处于run状态，可以接收和发送消息
        while (true){
            System.out.println("服务端和客户端"+userId+"保持通信，读取数据。。。");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message ms =  (Message)ois.readObject();
                if (ms.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    System.out.println(ms.getSender()+"要在线用户列表");
                    String onlineUser = new ManageClientThread().getOnlineUser();
//                    构建messge对象返回给客户端
                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RETURN_ONLINE_FRIEND);
                    message2.setContent(onlineUser);
                    message2.setGetter(ms.getSender());
//                    返回给客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                }else if (ms.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
//                    根据message获取getid，然后在得到对应的线程
                    Socket socket1  = ManageClientThread.getServerConnectThread(ms.getGetter()).socket;
//                    准备转发
                    ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());
                    oos.writeObject(ms);//转发，提示如果客户不在线，可以保存到数据库，这样就可以实现离线留言

                } else if (ms.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)) {
                    HashMap<String, ServerConnectClientThread> hm = ManageClientThread.getHm();//返回所有线程
                    Iterator<String> iterator = hm.keySet().iterator();
//                    遍历线程的KeySet
                    while (iterator.hasNext()) {
                        String onlineUserId =  iterator.next().toString();
                        if (!onlineUserId.equals(ms.getSender())){//排除群发消息的用户
                            Socket socket2 = ManageClientThread.getServerConnectThread(onlineUserId).socket;
//                            ObjectOutputStream oos = new ObjectOutputStream(socket2.getOutputStream());
                            ObjectOutputStream oos = new ObjectOutputStream(hm.get(onlineUserId).socket.getOutputStream());
                            oos.writeObject(ms);
                        }
                    }


                } else if (ms.getMesType().equals(MessageType.MESSAGE_File_MES)) {
//                    根据getteri将message对象zhuanfa
                    ObjectOutputStream oos =
                           new ObjectOutputStream(ManageClientThread.getServerConnectThread(ms.getGetter()).socket.getOutputStream()) ;
                    oos.writeObject(ms);
                } else if(ms.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    System.out.println(ms.getSender()+"退出系统");
//                    将客户端对应的线程从及集合中删除
                    ManageClientThread.removeClientThread(ms.getSender());
                    socket.close();//关闭当前线程所持有的socket，即关闭连接
//                    退出线程
                    break;
                }else{
                    System.out.println("其他类型的message，暂时不处理");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
            }
        }
    }
}
