package io.leavesfly.base;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

/** ���ö��߳������ļ� */
public class MultiThreadDownload {
	private final String urlStr;
    private final int threadNum;
	private final String desFilePath="E:\\MulThreadDownLoadBigFile";
	private final String fileName;
	private int threadLeave;
	
	public MultiThreadDownload( String urlStr,int theadNum,String fileName){
		this.urlStr=urlStr;
		this.threadNum=theadNum;
		this.fileName=fileName;
		 this.threadLeave=theadNum;
	}
	
	public synchronized int getThreadLeaveNum(){
		return --threadLeave;
	}
	
	public void download( ) throws IOException{
		File fileDirec = new File(desFilePath);
		if(!fileDirec.exists()) 
			fileDirec.mkdir();
		URL url = new URL(this.urlStr); // ����URL
		URLConnection con = url.openConnection(); // ��������
		int contentLen = con.getContentLength(); // �����Դ�ܳ���
		int subLen = contentLen / threadNum; // ÿ���߳�Ҫ���صĴ�С
		int remainder = contentLen % threadNum; // ����
		File destFile = new File(desFilePath+"\\"+fileName); // Ŀ���ļ�
		// ///�����������߳�
		for (int i = 0; i < threadNum; i++) {
			int start = subLen * i; // ��ʼλ��
			int end = start + subLen - 1; // ����λ��
			if (i == threadNum - 1) { // ���һ���̵߳Ľ���λ��
				end += remainder;
			}
			Thread t = new Thread(new DownloadRunnable(start, end, url,
					destFile));
			t.start();
		}
	}
		
	/** �߳������� */
	class DownloadRunnable implements Runnable {
		private final int start;
		private final int end;
		private final URL srcURL;
		private final File destFile;
		public static final int BUFFER_SIZE = 1024 * 8; // ��������С

		DownloadRunnable(int start, int end, URL srcURL, File destFile) {
			this.start = start;
			this.end = end;
			this.srcURL = srcURL;
			this.destFile = destFile;
		}

		public void run() {
			System.out.println(Thread.currentThread().getName() + "����...");
			BufferedInputStream bis = null;
			RandomAccessFile ras = null;
			byte[] buf = new byte[BUFFER_SIZE];// ����һ��������
			URLConnection con = null;
			try {
				con = srcURL.openConnection(); // ������������
				// �������ӵ�����ͷ�ֶΣ���ȡ��Դ���ݵķ�Χ��start��end
				con.setRequestProperty("Range", "bytes=" + start + "-" + end);
				// ���������л�ȡ����������װ�ɻ�����
				bis = new BufferedInputStream(con.getInputStream());

				ras = new RandomAccessFile(destFile, "rw"); // ����RandomAccessFile
				ras.seek(start); // ���ļ�ָ���ƶ���startλ��

				int len = -1; // ��ȡ�����ֽ���
				while ((len = bis.read(buf)) != -1) { // �������ж�ȡ����
					ras.write(buf, 0, len); // �������ȡ��д��Ŀ���ļ�
				}
				
				System.out.println(Thread.currentThread().getName() + "�Ѿ��������");
				System.out.println(getThreadLeaveNum()+"�߳�ʣ��...");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// �ر����е�IO������
				if (ras != null) {
					try {
						ras.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	public static void main(String[] args){
		String urlStr="http://bs.baidu.com/wenku-course/%2F80_646.mp4?sign=MBOT:vdB2hqA81IaA:CMvxraDzhmch9TFmkrWOSP2n%2FC8%3D&time=1358274833&start=5&method=streaming";
		String fileName="ML_1.mp4";
		MultiThreadDownload mulThreadDown=new MultiThreadDownload(urlStr, 20,fileName);
		try {
			mulThreadDown.download();
		} catch (IOException e) {
			System.out.println("------------���س����쳣----------");
			e.printStackTrace();
		}
	}

	
}

