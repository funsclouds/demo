package com.example.demo;

import com.example.demo.util.AseText;

public class text {
    public static void main(String[] args) {
        String key = "ligh";
        String phone = "65465456456111212321564654654654654545489778998456456561513212313215645456564";
        String qqq = "qwdwqdqwdwofjojasfsaf548465465484";
        System.out.println("加密前的字符串: " + phone);
        AseText aesUtil = new AseText(key);
        String encrypt = aesUtil.encrypt(phone);

        String qqqw = aesUtil.encrypt(qqq);

        System.out.println("加密后的字符串: " + encrypt);

        System.out.println("加密后的字符串: " + qqqw);

        String decrypt = aesUtil.decrypt(encrypt);

        String decrypt1 = aesUtil.decrypt(qqqw);

        System.out.println("解密之后的字符串: " + decrypt);

        System.out.println("解密之后的字符串: " + decrypt1);
    }


}