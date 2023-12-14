package com.hspedu.qqcilen.view;

import com.hspedu.qqcilen.service.MessageClientService;
import com.hspedu.qqcilen.service.UserClientService;
import com.hspedu.qqcilen.service.fileClientService;

import java.util.Scanner;

public class QQView {
    private boolean loop = true;//控制是否显示菜单
    private String key = "";//接收用户的输入系统
    private UserClientService userClientService = new UserClientService();//用于登录服务器和登录用户
    private MessageClientService messageClientService = new MessageClientService();//用于消息用户私聊群聊
    private fileClientService  fileClientService1 = new fileClientService();//该对象用于传输文件
    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("客户端退出系统");
    }
    private void mainMenu(){
        while (loop){
            System.out.println("=======欢迎登录网络通讯系统=======");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.println("请输入你的选择：");
            key = Utility.readString(1);
            switch (Integer.parseInt(key)){
                case 1:
                    System.out.println("请输入用户号");
                    String userId = Utility.readString(50);
                    System.out.println("请输入密 码");
                    String passwd = Utility.readString(20);
                    //需要到服务器端验证该用户是否合法
                    if (userClientService.checkUser(userId,passwd)){
                        System.out.println("=======欢迎(用户"+userId+")=======" );
//                        进入二级菜单
                        while (loop){
                            System.out.println("\n=======网络通信系统二级菜单(用户"+userId+")=====");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("请输入你的选择：");
                            key = Utility.readString(1);
                            switch (key){
                                case "1":
                                    System.out.println("显示在线用户列表");
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
//                                    System.out.println("群发消息");
                                    System.out.println("请输入你想对大家说的话：");
                                    String content1 = Utility.readString(1000);
//                                    调用一个方法将消息封装到message中，发送给服务端
                                    messageClientService.sendMessageToAll(content1,userId);
                                    break;
                                case "3":
                                    System.out.print("请输入你想私聊的用户(在线)：");
                                    String getId = Utility.readString(50);
                                    System.out.println("请输入想说的话：");
                                    String content = Utility.readString(1000);
//编写一个方法，将消息发送给服务器
                                    messageClientService.sendOneMessage(content,userId,getId);
//                                    System.out.println("私聊消息");
                                    break;

                                case "4":
                                    System.out.print("请输入你想发送文件的用户(在线)：");
                                    String getId1 = Utility.readString(50);
                                    System.out.println("请输入发送的文件完整路径(形式d:\\xxx.jpg)");
                                    String src = Utility.readString(1000);
                                    System.out.println("" +
                                            "请输入发送到对方的路径(形式d:\\xxx.jpg)");
                                    String dest = Utility.readString(1000);
                                    fileClientService1.sendFileToOne(src,dest,userId,getId1);
//                                    System.out.println("发送文件");
                                    break;

                                case "9":
                                    userClientService.logout();
                                    loop = false;
                            }
                        }
                    }else {
                        System.out.println("=======登录失败=====");

                    }
                    break;
                case 9:
                    loop = false;
                    break;
            }
        }

    }

}
