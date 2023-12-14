package com.hspedu.qqcilen.service;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 该类管理客户端连接到服务器端的线程
 */
public class ManageClientConnectServerThread {
// 将多个线程放入到一个HashMap集合，key就是用户id, value 就是线程

    private  static ConcurrentHashMap<String,ClientConnectServerThread> hm = new ConcurrentHashMap<>();
//    将某个集合加入到线程中
    public  static void addClientConnectServerThreadC(String useId,ClientConnectServerThread clientConnectServerThread){
        hm.put(useId, clientConnectServerThread);
    }
//    通过userId可以得到对应线程
    public static ClientConnectServerThread getclientConnectServerThread(String userId){
       return hm.get(userId);
    }
}
