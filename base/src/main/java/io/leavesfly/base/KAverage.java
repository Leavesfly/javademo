package io.leavesfly.base;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class KAverage {
	private List<SampleData> dataList;
	private int classNum;
	private final String FilePath = "E:\\DM\\KAverage.txt";
	private List<List<SampleData>> classList;
	private List<SampleData> averageOfClassList;
	private int conduct;

	public static void main(String[] args) {

		KAverage ka = new KAverage(2);
		// List<SampleData> dataList=new ArrayList<SampleData>();
		// dataList.add(new SampleData(0, 1, 1));
		// ka.averageSample(dataList).print();

		ka.k_average();
		ka.printDataList();
		ka.printResult();
	}

	public KAverage(int classNum) {
		this.classNum = classNum;
		dataList = getData();
		classList = new ArrayList<List<SampleData>>();
		averageOfClassList = new ArrayList<SampleData>();
	}

	private List<SampleData> getData() {
		List<SampleData> dataList = new ArrayList<SampleData>();
		BufferedReader bufferedIn = null;
		try {
			bufferedIn = new BufferedReader(new InputStreamReader(
					new FileInputStream(FilePath)));
			String str = null;
			SampleData data = null;
			while ((str = bufferedIn.readLine()) != null) {
				data = new SampleData(str);
				dataList.add(data);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return dataList;
	}

	public SampleData averageSample(List<SampleData> oneClass) {
		int attribute_1 = 0;
		int attribute_2 = 0;
		for (int i = 0; i < oneClass.size(); i++) {
			attribute_1 += oneClass.get(i).getAttribute_1();
			attribute_2 += oneClass.get(i).getAttribute_2();
		}
		attribute_1 = attribute_1 / oneClass.size();
		attribute_2 = attribute_2 / oneClass.size();

		SampleData data = new SampleData(0, attribute_1, attribute_2);
		return data;
	}

	private int getMinDistanceClassID(SampleData sampleData) {
		// ///������
		int result = 0;
		int temp = Integer.MAX_VALUE;
		for (int i = 0; i < averageOfClassList.size(); i++) {
			if (temp > sampleData.subtractOfSquare(averageOfClassList.get(i))) {
				// System.out.println(sampleData.subtractOfSquare(averageOfClassList.get(0)));
				temp = sampleData.subtractOfSquare(averageOfClassList.get(i));
				result = i;
			}
		}
		return result;
	}

	private int getInClassDistance(SampleData average, List<SampleData> oneClass) {
		int result = 0;
		for (int i = 0; i < oneClass.size(); i++) {
			result += average.subtractOfSquare(oneClass.get(i));
		}
		return result;
	}

	private int getConduct() {
		int result = 0;
		for (int i = 0; i < classList.size(); i++) {
			result += getInClassDistance(averageOfClassList.get(0),
					classList.get(i));
		}
		return result;
	}

	public void k_average() {
		// ��ʼ��
		List<SampleData> oneClass = null;
		// int k = 0;
		for (int i = 0; i < classNum; i++) {
			oneClass = new ArrayList<SampleData>();
			oneClass.add(dataList.get(i));
			classList.add(oneClass);
			averageOfClassList.add(classList.get(i).get(0));
			// oneClass.get(0).print();
			// k = dataList.size() - k;
		}

		SampleData sampleData = null;
		for (int i = classNum; i < dataList.size(); i++) {
			sampleData = dataList.get(i);
			int classId = getMinDistanceClassID(sampleData);
			// System.out.println(classId);
			classList.get(classId).add(sampleData);
		}
		int subConduct = Math.abs(getConduct() - conduct);
		// printResult();
		// ---------------------------------����
		while (subConduct != 0) {
			conduct = getConduct();
			averageOfClassList.clear();
			for (int i = 0; i < classList.size(); i++) {
				// classList.get(i).get(0).print();
				// classList.get(i).get(1).print();
				// System.out.println(classList.get(i).size());
				sampleData = averageSample(classList.get(i));
				// sampleData.print();
				averageOfClassList.add(sampleData);
				classList.get(i).clear();
			}
			for (int i = 0; i < dataList.size(); i++) {
				sampleData = dataList.get(i);
				int classId = getMinDistanceClassID(sampleData);
				// System.out.println(classId);
				classList.get(classId).add(sampleData);
			}
			subConduct = Math.abs(getConduct() - conduct);
			// printResult();
		}

	}

	public void printResult() {
		for (int i = 0; i < classList.size(); i++) {
			for (int j = 0; j < classList.get(i).size(); j++) {
				classList.get(i).get(j).print();
			}
			System.out.println("=============================");
		}
	}

	public void printDataList() {
		for (int i = 0; i < dataList.size(); i++) {
			dataList.get(i).print();
		}
		System.out.println("================================");
	}

}

class SampleData {
	private int ID;
	private int attribute_1;
	private int attribute_2;

	public SampleData(int ID, int attribute_1, int attribute_2) {
		this.ID = ID;
		this.attribute_1 = attribute_1;
		this.attribute_2 = attribute_2;
	}

	public SampleData(String str) {
		String[] strs = str.split(",");
		ID = Integer.parseInt(strs[0]);
		attribute_1 = Integer.parseInt(strs[1]);
		attribute_2 = Integer.parseInt(strs[2]);
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getAttribute_1() {
		return attribute_1;
	}

	public void setAttribute_1(int attribute_1) {
		this.attribute_1 = attribute_1;
	}

	public int getAttribute_2() {
		return attribute_2;
	}

	public void setAttribute_2(int attribute_2) {
		this.attribute_2 = attribute_2;
	}

	public int subtractOfSquare(SampleData average) {
		int result = Math.abs(average.getAttribute_1() - attribute_1)
				+ Math.abs(average.getAttribute_2() - attribute_2);
		return (int) Math.pow(result, 2);
	}

	public void print() {
		System.out.println(ID + ", " + attribute_1 + ", " + attribute_2);
	}

}