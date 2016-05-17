package io.leavesfly.base;

import java.io.*;
import java.net.*;

public class PicturesToFile {

	private static int count = 0;
	private static File fileDir;
	private final static String filePath = "D:\\images";
	private static int BUFFER_SIZE = 1024 * 10;
	private BufferedInputStream bufInputStream;
	private BufferedOutputStream bufOutputStream;
	private FileOutputStream fOut;
     
	//���þ�̬�鹹��Ŀ¼
	static {
		fileDir = new File(filePath);
		if (!fileDir.exists()) {
			fileDir.mkdir();

			System.out.println("����Ŀ¼�ɹ�");
		}
	}
    
	//��ֹ���̵߳Ĳ�ͬ��
	protected synchronized int getCount() {
		return count++;
	}

	//���ݴ�������ͼƬ�ĵ�ַ����ͼƬ �����浽Ŀ¼��
	public void getPicturesToFiles(String pictureURL) {
		URL url = null;
		try {
			url = new URL(pictureURL);
			
			URLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setConnectTimeout(1000 * 15 );

			bufInputStream = new BufferedInputStream(url.openStream());
			
			//��ͼƬ�����Ʊ��� �������������
			//String fileName = (String.valueOf(getCount())).concat(pictureURL
				//	.substring(pictureURL.lastIndexOf("/") + 1));
			//����������������
			String fileName = (String.valueOf(getCount())).concat(pictureURL
					.substring(pictureURL.lastIndexOf(".")));

			fOut = new FileOutputStream(new File(filePath + "\\" + fileName));
			bufOutputStream = new BufferedOutputStream(fOut);
			byte[] buffer = new byte[BUFFER_SIZE];
			int readNumber = 0;
			while ((readNumber = bufInputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
				bufOutputStream.write(buffer, 0, readNumber);
			}
			bufOutputStream.flush();
			bufInputStream.close();
			bufOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
