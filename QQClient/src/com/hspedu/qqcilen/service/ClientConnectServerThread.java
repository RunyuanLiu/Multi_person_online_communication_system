package com.hspedu.qqcilen.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends Thread{
//    该线程需要持有Socket,因此将Socket设置为属性
    private Socket socket;
    public ClientConnectServerThread(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
//        因为Thread需要在后台和服务器通信，因此我们要使用while循环
        while (true){
            try {
                System.out.println("客户端线程，等待从读取服务端发送的消息");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message ms =(Message) ois.readObject();//如果服务器没有发送Message对象，线程会阻塞在这里
//                判断该message的类型，然后做相应的业务处理
//                客户端发送的是获取在线列表收到的是返回在线列表
                if (ms.getMesType().equals(MessageType.MESSAGE_RETURN_ONLINE_FRIEND)){
//取出在线列表信息并显示
                 String[] onlineUsers = ms.getContent().split(" ");
                    System.out.println("=======当前在线用户列表如下=======");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户："+ onlineUsers[i]);

                    }
                } else if (ms.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
// 把从服务器上转发的消息，显示出来
                    System.out.println("\n"+ms.getSender()
                            +"对"+ms.getGetter()+"说"
                            +ms.getContent());

                } else if (ms.getMesType().equals(MessageType.MESSAGE_File_MES)) {
                    System.out.println("\n"+ms.getSender()+"给"+ms.getGetter()+"发送件："+ms.getSrc()+"到："+ms.getDest());
//                    取出message的文件字节数组，通过文件输出流写入到磁盘
                    FileOutputStream fileOutputStream = new FileOutputStream(ms.getDest());
                    fileOutputStream.write(ms.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("保存文件成功");
                } else if (ms.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)){
//                    显示在客户端的控制台
                    System.out.println("\n"+ms.getSender()+"对大家说："+ms.getContent());

                }else {

                    System.out.println("其他类型的message");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
            }
        }
    }
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


}
