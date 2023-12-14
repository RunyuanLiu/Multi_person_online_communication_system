package com.hspedu.qqcilen.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.*;

public class fileClientService {
    private FileInputStream fileInputStream = null;
    public void sendFileToOne(String src,String dest,String senderId,String getterId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_File_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);
        byte[] fileBytes = new byte[(int)new File(src).length()];
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);//将src文件读入到程序的字节数组
//            将文件对应的字节数字设置到message
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                }
            }
        }
        System.out.println("\n"+senderId+"想给"+getterId+"发送文件："
                +src+"到对方电脑"+dest);
//        发送
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getclientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
        }

    }
}
