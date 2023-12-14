package com.hspedu.qqcilen.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;


/***
 * 该类完成用户登录验证和用户注册等功能
 */
public class UserClientService {
  private User u =   new User();//方便在其他地方使用user信息，因此做成成员属性
//    根据userId和pwd到服务器验证该用户是否合法
    private Socket socket;
    private boolean b;
    public boolean checkUser(String userId,String pwd) {
     u.setUserId(userId);
     u.setPasswd(pwd);
//     连接到服务器发送对象
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"),9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//发送对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();//接收对象
            if (ms.getMesType().equals(MessageType.MESSAGE_LOGINF_SUCCEED)){
                b = true;
//创建一个和服务器保持通讯的线程->创建线程类
                ClientConnectServerThread clientConnectServerThreadst = new ClientConnectServerThread(socket);
                clientConnectServerThreadst.start();
//                为了方便管理将线程放入到集合中
                ManageClientConnectServerThread.addClientConnectServerThreadC(userId,clientConnectServerThreadst);

            }else{
//                如果登录失败就关闭socket
                socket.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
        }
        return b;
    }
//向服务器端请求在线用户列表
    public void onlineFriendList(){
//发送一个message，类型为MESSAGE_GET_ONLINE_FRIEND
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());
//        发送给服务器
//        获取到当前线程的socket对应的Objectoutputstream对象
        try {
//
            ObjectOutputStream oos= new ObjectOutputStream(ManageClientConnectServerThread.
                    getclientConnectServerThread(u.getUserId()).
                    getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//    编写方法，退出客户端，并给服务端发送一个退出系统的message对象
    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());//指定是哪个客户端要退出
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId()+"退出系统");
            System.exit(0);//结束进程
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
        }
    }
}
