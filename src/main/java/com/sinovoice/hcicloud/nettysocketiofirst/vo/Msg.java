package com.sinovoice.hcicloud.nettysocketiofirst.vo;

import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class Msg implements Serializable {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Msg.class);

    private static final long serialVersionUID = -6519304261259719883L;

    private String userId;

    private String userName;

    private String receiveUserId;

    private String content;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) throws UnsupportedEncodingException {
//        System.out.println(getEncoding(userName));
//
//        byte[]  b = new byte[0];//编码
//        try {
//            b = userName.getBytes(getEncoding(userName));
//            this.userName =  new String(b, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        if ("ISO-8859-1".equals(getEncoding(userName))){
            byte[]  b = userName.getBytes(getEncoding(userName));
            this.userName =  new String(b, "utf-8");
        }else{
            this.userName =  java.net.URLDecoder.decode(userName,"GB2312");
        }
        log.info("收到客户端的文本内容为："+ this.userName);
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) throws UnsupportedEncodingException {
//        System.out.println(getEncoding(content));
//
//        byte[]  b = new byte[0];//编码
//        try {
//            b = content.getBytes(getEncoding(content));
//            this.content =  new String(b, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        if ("ISO-8859-1".equals(getEncoding(content))){
            byte[]  b = content.getBytes(getEncoding(content));
            this.content =  new String(b, "utf-8");
        }else{
            this.content = java.net.URLDecoder.decode(content,"GB2312");
        }
        log.info("收到客户端的文本内容为："+ this.content);
    }

    public Msg(String userId, String userName, String receiveUserId,
               String content) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.receiveUserId = receiveUserId;
        this.content = content;
    }

    public Msg() {
        super();
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GB2312
                String s = encode;
                return s;      //是的话，返回“GB2312“，以下代码同理
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是ISO-8859-1
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {   //判断是不是UTF-8
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GBK
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";        //如果都不是，说明输入的内容不属于常见的编码格式。
    }
}
