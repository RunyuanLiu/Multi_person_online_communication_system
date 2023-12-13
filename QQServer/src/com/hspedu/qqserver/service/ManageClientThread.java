package com.hspedu.qqserver.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ManageClientThread {
    private static HashMap<String,ServerConnectClientThread> hm = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    //    添加线程对象到hm集合
    public static void addClientThread(String userId,ServerConnectClientThread serverConnectClientThread){
        hm.put(userId,serverConnectClientThread);
    }
//    根据userId返回ServerConnectThread
    public static ServerConnectClientThread getServerConnectThread(String userId){
        return hm.get(userId);
    }
//    编写方法，可以返回在线用户列表
    public static String getOnlineUser(){
//        集合遍历，遍历hashMap的key
        String onlineUserList = "";
        Iterator<String> iterator = hm.keySet().iterator();
        while (iterator.hasNext()) {
             onlineUserList +=  iterator.next() + " ";
        }
        return onlineUserList;
    }
//    增加一个方法,从集合中，一处某个线程对象
    public static void removeClientThread(String userId){
        hm.remove(userId);
    }
}
