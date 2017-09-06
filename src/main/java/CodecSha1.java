import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @author: Lucifer
 * @date: 2016/9/28 16:42
 */
public class CodecSha1 {

    public static void main(String[] args){
        String blank = new String(Base64.encodeBase64("".getBytes()));
        System.out.println(blank);
        System.out.println(Base64.decodeBase64(blank));
        String test = getImageStr("E://663145.jpg");
        System.out.println(test);
        GenerateImage(test);
        String test2 = getStrFromPic("E://663145.jpg");
        System.out.println(test2);
        List<String> list = new ArrayList<String>();
        list.add("abc");
        list.add("15236");
        list.add("lucifer");
        Collections.sort(list);
        String result = DigestUtils.shaHex(list.get(0)+list.get(1)+list.get(2));
        System.out.println(result);
    }

    public static String getImageStr(String path){
//        File file = new File(path);
        InputStream in = null;
        byte[]data = null;
        try {
            in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(Base64.encodeBase64(data));
    }

    public static String getStrFromPic(String path){
        StringBuffer result = new StringBuffer();
        File file = new File(path);
        InputStream in = null;
        try {
//            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            byte [] bArr = new byte[100];
            int b;
            in = new FileInputStream(file);
//            System.out.println("当前字节输入流中的字节数为:" + in.available());
            while ((b=in.read(bArr))!=-1){
                result.append(new String(bArr));
            }
            in.close();
            return new String(Base64.encodeBase64(result.toString().getBytes()));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static boolean GenerateImage(String imgStr)
    {   //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return false;
        try
        {
            //Base64解码
            byte[] b = Base64.decodeBase64(imgStr);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            //生成jpeg图片
            String imgFilePath = "e://222.jpg";//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
