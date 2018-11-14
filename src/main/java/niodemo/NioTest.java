package niodemo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @Description:
 * @author: Lucifer
 * @date: 2018/9/3 17:04
 */
public class NioTest {

	/**
	 * 该方式会导致中文乱码
	 * 来源https://juejin.im/post/5b8a103e51882542e56e646a
	 */
	public static void testNio(){
		try {
			RandomAccessFile rdf=new RandomAccessFile("D:\\nio\\test.txt","rw");
			//利用channel中的FileChannel来实现文件的读取
			FileChannel inChannel=  rdf.getChannel();
			//设置缓冲区容量为10
			ByteBuffer buf=  ByteBuffer.allocate(10);
			//从通道中读取数据到缓冲区，返回读取的字节数量
			int byteRead=inChannel.read(buf);
			//数量为-1表示读取完毕。
			while (byteRead!=-1){
				//切换模式为读模式，其实就是把postion位置设置为0，可以从0开始读取
				buf.flip();
				//如果缓冲区还有数据
				while (buf.hasRemaining()){
					//输出一个字符
					System.out.print((char) buf.get());
				}
				//数据读完后清空缓冲区
				buf.clear();
				//继续把通道内剩余数据写入缓冲区
				byteRead = inChannel.read(buf);
			}
			//关闭通道
			rdf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 *	解决中文乱码问题
	 * https://blog.csdn.net/zjkc050818/article/details/78810818
	 */
	public static void test_CN() {

		try {

			// 通过设置字符集的编码，并将ByteBuffer转换为CharBuffer来避免中文乱码
			Charset charset = Charset.forName("UTF-8");
			CharsetDecoder decoder = charset.newDecoder();

			RandomAccessFile file = new RandomAccessFile("d:\\nio\\test.txt", "rw");

			FileChannel fileChannel = file.getChannel();

			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			CharBuffer charBuffer = CharBuffer.allocate(1024);

			int byteread = fileChannel.read(byteBuffer);

			while (-1 != byteread) {
				System.out.println("read" + byteread);
				byteBuffer.flip();
				while (byteBuffer.hasRemaining()) {
					decoder.decode(byteBuffer, charBuffer, false);
					charBuffer.flip();
					System.out.print(charBuffer);
				}
				byteBuffer.clear();
				charBuffer.clear();
				byteread = fileChannel.read(byteBuffer);
			}
			fileChannel.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		testNio();
		test_CN();
		System.out.println("abc");
	}
}
