package crawler.jandan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @Description:图片下载保存
 * @author: Lucifer
 * @date: 2016/11/22 10:54
 */
public class JianDanImageCreator implements Runnable {
	private static int count = 0;
	private String imageUrl;
	private int page;
	private String subpath;
	 //存储路径
	private static final String basePath = "E:/jiandan/"; 
	public JianDanImageCreator(String imageUrl,int page,String subpath) {
		this.imageUrl = imageUrl;
		this.page = page;
		this.subpath = subpath;
	}
	@Override
	public void run() {
		File dir = new File(basePath);
		if(!dir.exists()){
			dir.mkdirs();
			System.out.println("图片存放于"+basePath+"目录下");
		}
		String imageName = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
		try {
			File file = new File( basePath+subpath+page+"--"+imageName);
			OutputStream os = new FileOutputStream(file);
			//创建一个url对象
			URL url = new URL(imageUrl);
			InputStream is = url.openStream();
			byte[] buff = new byte[1024];
			while(true) {
				int readed = is.read(buff);
				if(readed == -1) {
					break;
				}
				byte[] temp = new byte[readed];
				System.arraycopy(buff, 0, temp, 0, readed);
				//写入文件
				os.write(temp);
			}
			System.out.println("第"+(count+1)+"张:"+file.getAbsolutePath());
			is.close(); 
            os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
