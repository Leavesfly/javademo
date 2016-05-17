package io.leavesfly.base;

import java.util.ArrayList;
import java.util.List;

public class CatchPictures {
	
	private List<String> urls=new ArrayList<String>();// ���ڴ洢�����ַ; 

	public CatchPictures(String url) {
		addURL(url);

	}

	public  void addURL(String url) {
		if (url.startsWith("http://"))
			urls.add(url);
		else{
			System.out.println("���������ַ��ʽ����");
			urls=null;
		}
			
		
	}

	public void downloadPictures() {
		Thread thread = null;
		for (String url : urls) {
			thread = new AnalyseHTML(url);
			thread.start();
		}
		
	}

	public static void main(String[] agrs) {
		CatchPictures catPictures = new CatchPictures("http://zhan.renren.com/shicigefu?ref=hotnewsfeed&sfet=3732&fin=4&ff_id=&from=PubNewFeed");
		//catPictures.addURL("http://www.sina.com.cn");
		//catPictures.addURL("http://www.renren.com");
		//catPictures.addURL("http://www.baidu.com");
		
		catPictures.downloadPictures();//���ö��߳�����ͼƬ
	}

}
