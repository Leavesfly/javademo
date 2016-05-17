package io.leavesfly.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalyseHTML extends Thread {
	private String url;

	public AnalyseHTML(String url) {
		this.url = url;
	}

	@Override
	public void run() {

		try {
			analyseHTML(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ��ȡ��ҳ�Ĵ��뱣�����ڴ��StringBuffer���͵�Content��
	public String getHtmlCode(String httpUrl) throws IOException {
		StringBuffer content = new StringBuffer();
		
		URL url = new URL(httpUrl); // ����URL�����
		
		URLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setConnectTimeout(1000 * 15);
		
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(url
				.openStream())); // ʹ��openStream�õ�һ������,����һ��BufferedReader����
		String input;
		while ((input = bufReader.readLine()) != null) { // ������ȡѭ��
			content.append(input);
			//System.out.println(input);
		}
		bufReader.close();
		return content.toString();
	}

	// ����������ʽ���ҵ�ƥ�����ҳͼƬ��URL
	public void analyseHTML(String url) throws IOException {

		// ��������ҳ����Content�в���ƥ���ͼƬ����,�������·��.
		String ImgReg1 = "(?x)(src|SRC|background|BACKGROUND)=('|\")/?(([\\w-]+/)*([\\w-]+\\.(jpg|JPG|png|PNG|gif|GIF)))('|\")";
		// ��������ҳ����Content�в���ƥ���ͼƬ����,���ھ���·��.
		String ImgReg2 = "(?x)(src|SRC|background|BACKGROUND)=('|\")(http://([\\w-]+\\.)+[\\w-]+(:[0-9]+)*(/[\\w-]+)*(/[\\w-]+\\.(jpg|JPG|png|PNG|gif|GIF)))('|\")";

		String content = getHtmlCode(url);
		

		Pattern pattern = Pattern.compile(ImgReg1);
		Matcher matcher = pattern.matcher(content); // ����ƥ��
		PicturesToFile picToFile = new PicturesToFile();

		while (matcher.find()) {
			System.out.println("--------------------------------");
			System.out.println("�ҵ�ͼƬ��URL��");
			System.out.println(url+"/"+matcher.group(3)); // ���ͼƬ���ӵ�ַ������̨

			picToFile.getPicturesToFiles(url+"/"+matcher.group(3)); // ���������ز�����ͼƬ�ļ���ָ��Ŀ¼
		}
		pattern = Pattern.compile(ImgReg2);
		matcher = pattern.matcher(content); // ����ƥ��

		while (matcher.find()) {
			System.out.println("�ҵ�ͼƬ��URL��");
			System.out.println(matcher.group(3)); // ���ͼƬ���ӵ�ַ������̨

			picToFile.getPicturesToFiles(matcher.group(3)); // ���������ز�����ͼƬ�ļ���ָ��Ŀ¼
		}
	}

	// ���Ժ���
	public static void main(String[] args) throws IOException {

		String url = "http://www.bnu.edu.cn";
		AnalyseHTML catchPictures = new AnalyseHTML(url);
		catchPictures.analyseHTML(url);
//       System.out.println(catchPictures.getHtmlCode(url));
	}

}
