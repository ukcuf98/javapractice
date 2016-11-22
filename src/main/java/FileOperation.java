import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description:文件操作
 * @author: Lucifer
 * @date: 2016/11/3 16:25
 */
public class FileOperation {
    public static void main(String[] args) {
        readFile2();
    }

    public static void readFile1(){
        File file = new File("D://file.txt");
        InputStream in = null;
        try {
            System.out.println("以字节为单位读取文件内容，一次读一个字节：");
            in = new FileInputStream(file);
            // 一次读一个字节
            int b;
            while ((b = in.read()) != -1){
                System.out.write(b);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public static void readFile2(){
        File file = new File("D://file.txt");
        InputStream in = null;
        try {
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            byte [] bArr = new byte[100];
            int b;
            in = new FileInputStream(file);
            System.out.println("当前字节输入流中的字节数为:" + in.available());
            while ((b=in.read(bArr))!=-1){
                System.out.write(bArr,0,b);
            }
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
