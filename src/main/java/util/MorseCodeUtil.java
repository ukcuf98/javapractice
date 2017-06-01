package util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:摩斯密码转换工具类
 * @author: Lucifer
 * @date: 2017/5/1 14:10
 */
public class MorseCodeUtil {
    private static Map<String,String> baseCodeMap;

    static {
        baseCodeMap = new HashMap<String,String>();
        baseCodeMap.put("A", "01");      /* A                   */
        baseCodeMap.put("B", "1000");    /* B                   */
        baseCodeMap.put("C", "1010");    /* C                   */
        baseCodeMap.put("D", "100");     /* D                   */
        baseCodeMap.put("E", "0");       /* E                   */
        baseCodeMap.put("F", "0010");    /* F                   */
        baseCodeMap.put("G", "110");     /* G                   */
        baseCodeMap.put("H", "0000");    /* H                   */
        baseCodeMap.put("I", "00");      /* I                   */
        baseCodeMap.put("J", "0111");    /* J                   */
        baseCodeMap.put("K", "101");     /* K                   */
        baseCodeMap.put("L", "0100");    /* L                   */
        baseCodeMap.put("M", "11");      /* M                   */
        baseCodeMap.put("N", "10");      /* N                   */
        baseCodeMap.put("O", "111");     /* O                   */
        baseCodeMap.put("P", "0110");    /* P                   */
        baseCodeMap.put("Q", "1101");    /* Q                   */
        baseCodeMap.put("R", "010");     /* R                   */
        baseCodeMap.put("S", "000");     /* S                   */
        baseCodeMap.put("T", "1");       /* T                   */
        baseCodeMap.put("U", "001");     /* U                   */
        baseCodeMap.put("V", "0001");    /* V                   */
        baseCodeMap.put("W", "011");     /* W                   */
        baseCodeMap.put("X", "1001");    /* X                   */
        baseCodeMap.put("Y", "1011");    /* Y                   */
        baseCodeMap.put("Z", "1100");    /* Z                   */
        baseCodeMap.put("0", "11111");   /* 0                   */
        baseCodeMap.put("1", "01111");   /* 1                   */
        baseCodeMap.put("2", "00111");   /* 2                   */
        baseCodeMap.put("3", "00011");   /* 3                   */
        baseCodeMap.put("4", "00001");   /* 4                   */
        baseCodeMap.put("5", "00000");   /* 5                   */
        baseCodeMap.put("6", "10000");   /* 6                   */
        baseCodeMap.put("7", "11000");   /* 7                   */
        baseCodeMap.put("8", "11100");   /* 8                   */
        baseCodeMap.put("9", "11110");   /* 9                   */
        baseCodeMap.put(".", "010101");  /* Full stop           */
        baseCodeMap.put(",", "110011");  /* Comma               */
        baseCodeMap.put("?", "001100");  /* Question mark       */
        baseCodeMap.put("\\", "011110"); /* Apostrophe          */
        baseCodeMap.put("!", "101011");  /* Exclamation mark    */
        baseCodeMap.put("/", "10010");   /* Slash               */
        baseCodeMap.put("(", "10110");   /* Left parenthesis    */
        baseCodeMap.put(")", "101101");  /* Right parenthesis   */
        baseCodeMap.put("&", "01000");   /* Ampersand           */
        baseCodeMap.put(":", "111000");  /* Colon               */
        baseCodeMap.put(";", "101010");  /* Semicolon           */
        baseCodeMap.put("=", "10001");   /* Equal sign          */
        baseCodeMap.put("+", "01010");   /* Plus sign           */
        baseCodeMap.put("-", "100001");  /* Hyphen1minus        */
        baseCodeMap.put("_", "001101");  /* Low line            */
        baseCodeMap.put("\"", "010010");  /* Quotation mark      */
        baseCodeMap.put("$", "0001001"); /* Dollar sign         */
        baseCodeMap.put("@", "011010");

    }

    public static  String toMorse(String msg){
        return toMorse(msg," ");
    }

    public static String toMorse(String msg,String split){
        if(StringUtils.isBlank(msg)){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String[] msgArr = msg.split(split);
        for (int i = 0; i < msgArr.length; i++) {
            String tmp = msgArr[i];
            if(StringUtils.isNotBlank(tmp)){
                char[]charArr = tmp.toCharArray();
                for (int j = 0; j < charArr.length; j++) {
                    String tmpChar = charArr[j]+"";
                    tmp = baseCodeMap.get(tmpChar.toUpperCase());
                    sb.append(tmp);
                }
                sb.append(split);
            }
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    public static void main(String[] args) {
        toMorse("abc");
    }
}
