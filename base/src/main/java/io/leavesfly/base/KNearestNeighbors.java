package io.leavesfly.base;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class KNearestNeighbors {
	private static final String filePath = "E:\\DM\\kNN.txt";
	private List<PersonData> personList;

	public KNearestNeighbors() {
		personList = new ArrayList<PersonData>();
		try {
			BufferedReader bufferedIn = new BufferedReader(
					new InputStreamReader(new FileInputStream(filePath)));
			String str = null;
			PersonData personData = null;
			while ((str = bufferedIn.readLine()) != null) {
				personData = new PersonData(str);
				personList.add(personData);
			}
			if(bufferedIn!=null){
				bufferedIn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<PersonData> getPersonList() {
		return personList;
	}

	public void setPersonList(List<PersonData> personList) {
		this.personList = personList;
	}

	
	public int maxPosition(int[] type) {
		int temp = 0;
		int max = 0;
		for (int j = 0; j < type.length; j++) {
			if (type[j] > max) {
				max = type[j];
				temp = j;
			}
		}
		return temp;
	}
	
	
	
	public int maxDistancePosition(PersonData[] personArray,PersonData unknowType){
		int maxPosition=0;
		float maxDistance=0;
		float temp=0;
		for(int i=0;i<personArray.length;i++){
			temp=unknowType.distance(personArray[i]);
			if(temp>maxDistance){
				maxDistance=temp;
				maxPosition=i;
			}
		}
		return maxPosition;
	}
	
	
	public HeightType kNN(PersonData unknowType, int neighborNum_k) {
		
		PersonData[] personArray = new PersonData[neighborNum_k];
		for (int i = 0; i < personArray.length; i++) {
			personArray[i] = personList.get(i);
		}
		int maxDisPosition = 0;
		for (int j = personArray.length - 1; j < personList.size(); j++) {
			maxDisPosition = maxDistancePosition(personArray, unknowType);
			if (personList.get(j).distance(unknowType) < personArray[maxDisPosition]
					.distance(unknowType)) {
				personArray[maxDisPosition] = personList.get(j);
			}
		}
		int[] type = new int[3];
		for (int i = 0; i < personArray.length; i++) {
			if (personArray[i].getHeightType() == HeightType.Low) {
				type[0]++;
			} else if (personArray[i].getHeightType() == HeightType.high) {
				type[2]++;
			} else {
				type[1]++;
			}
		}
		int heightType = maxPosition(type);
		if (heightType == 0) {
			return HeightType.Low;
		}
		if (heightType == 1) {
			return HeightType.average;
		}
		return HeightType.high;

	}
	
	
	
	public static void main(String[] args) {
		KNearestNeighbors knn=new KNearestNeighbors();
		PersonData person=new PersonData("Tom", Sex.Male, 2.0f);
		System.out.println(knn.kNN(person,3));
		//System.out.println(new KNearestNeighbors().getPersonList().size());
	}
}

class PersonData {
	private String name;
	private Sex sex;
	private float height;
	private HeightType heightType;
	
	public PersonData(String name,Sex sex,float height){
		this.name=name;
		this.sex=sex;
		this.height=height;
	}
	
	public PersonData(String str) {
		String[] strs = str.split(",");
		name = strs[0];
		if (strs[1].equals("male")) {
			sex = Sex.Male;
		} else {
			sex = Sex.Femal;
		}
		height = Float.parseFloat(strs[2]);
		if (strs[3].equals("low")) {
			heightType = HeightType.Low;
		} else if (strs[3].equals("average")) {
			heightType = HeightType.average;
		} else {
			heightType = HeightType.high;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public HeightType getHeightType() {
		return heightType;
	}

	public void setHeightType(HeightType heightType) {
		this.heightType = heightType;
	}
	
	public float distance(PersonData unknowType){
		
		return Math.abs(unknowType.getHeight()-height);
	}

}

enum Sex {
	 Femal,Male
}

enum HeightType{
	Low,average,high
}
