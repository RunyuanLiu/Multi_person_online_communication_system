package com.hspedu.qqserver.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/***
 * 服务端，监听9999端口，等待客户端连接，并保持通信
 */
public class QQServer {
    private ServerSocket ss = null;
//    创建一个集合，存放多个用户，如果是这些用户登录，旧任务是合法的
    private static HashMap<String,User> validUsers = new HashMap<>();

    public static void main(String[] args) {
        new QQServer();
    }
    static {//在静态代码块中，初始化 validUsers
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("300","123456"));
        validUsers.put("400",new User("400","123456"));
        validUsers.put("至尊宝",new User("至尊宝","123456"));
        validUsers.put("紫霞仙子",new User("紫霞仙子","123456"));
        validUsers.put("菩提老祖",new User("菩提老祖","123456"));
    }
    private boolean checkUser(String useId, String passwd){
        User user = validUsers.get(useId);
        if (user == null){
            return false;
        }
        if (!(user.getPasswd().equals(passwd))){
            return false;
        }
        return true;
    }
    public QQServer(){
//        注意：端口可以写在配置文件，
        try {
            new Thread(new SendNewsToAllService()).start();
            System.out.println("服务器端在9999端口监听...");
            ss = new ServerSocket(9999);
//            是个循环监听的过程
            while (true){//当和某个客户端连接后，会继续监听
                Socket socket = ss.accept();//如果没有客户端连接，将会阻塞在这里
//                得到客户端发送的user对象
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User u = (User) ois.readObject();//读取客户端发送的user对象
//                创建一个Message对象，准备回复客户端
                Message message = new Message();
                ObjectOutputStream oos  = new ObjectOutputStream(socket.getOutputStream());
                if (checkUser(u.getUserId(), u.getPasswd())){//登录成功
//                登录成功向客户端发送Message对象
                      message.setMesType(MessageType.MESSAGE_LOGINF_SUCCEED);
                      oos.writeObject(message);
//                      创建一个线程，和客户端保持通信，该线程需要有socekt对象
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, u.getUserId());
                    serverConnectClientThread.start();
//                    把该线程放到一个集合中，进行管理
                    ManageClientThread.addClientThread(u.getUserId(), serverConnectClientThread);

                }else {//登录失败
                    System.out.println("用户 id="+u.getUserId()+"pwd="+u.getPasswd()+"验证失败");
                    message.setMesType(MessageType.MESSAGE_LOGINF_Fail);
                    oos.writeObject(message);
                    socket.close();

                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
//      如果服务器退出了while，说明服务器不在监听，因此需要关闭ServerSocket
            try {
                ss.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
