package io.leavesfly.base;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ID3 {
	private List<Animal> sampleSet;
	private int sampleNum;
	private List<AnimalAttributeHelper> animalAttributeList;
	private static final String filePath="E://DM//ID3.txt";
	
	public ID3(){
		BufferedReader bufferedIn=null;
		try {
			 bufferedIn=new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			 String str = null;
			 String[] strArray=null;
			 while((str=bufferedIn.readLine())!=null){
				 strArray=str.split(",");
				 for(int i=0;i<strArray.length;i++){
					 if(i!=strArray.length-1){
						 int temp=Integer.parseInt(strArray[i]);
					 }
					 
				 }
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public float expectationOfNumArray(int[] numInType ) {
		float expectation = 0f;
		float probability = 0f;
		for (int i = 0; i < numInType.length; i++) {
			probability = ((float) numInType[i] / (float) sampleNum);
			expectation -= probability * (Math.log(probability) / Math.log(2));
		}
		return expectation;
	}

	public int[] numArraySetInType(List<Animal> amimalSet){
		int[] numArraySetInType=new int[2];
		Iterator<Animal> iter=amimalSet.iterator();
		Animal animal=null;
		while(iter.hasNext()){
			animal=iter.next();
			if(animal.getLaysEggs().equals(LayEggs.isLay)){
				numArraySetInType[0]++;
			}else{
				numArraySetInType[1]++;
			}
		}
		return numArraySetInType;
	}
	
	public List<List<Animal>> subSetListByAttribute(List<Animal> animalSet,String attribute){
		return null;
	}
	
	public int[] getNumInType(List<Animal> animalSubSet,String attribute){
		int[] numIntype=new int[sampleNum];
		return numIntype;
	}
	
	public float entropyOfAttribute(String attribute){
		return 0f;
	}
	
	public float gainOfAttribute(List<Animal> animalSet,String attribute){
		return 0f;
	}
	
	
}

class Animal{
	private int warmBlooded;
	private int feathers;
	private int fur;
	private int swims;
	private LayEggs laysEggs;
	
	public Animal(){
		
	}
	public Animal(int warmBlooded,int feathers,int fur,int swims,LayEggs laysEggs){
		this.warmBlooded=warmBlooded;
		this.feathers=feathers;
		this.fur=fur;
		this.swims=swims;
		this.laysEggs=laysEggs;
	}
	
	public int getWarmBlooded() {
		return warmBlooded;
	}
	public void setWarmBlooded(int warmBlooded) {
		this.warmBlooded = warmBlooded;
	}
	public int getFeathers() {
		return feathers;
	}
	public void setFeathers(int feathers) {
		this.feathers = feathers;
	}
	public int getFur() {
		return fur;
	}
	public void setFur(int fur) {
		this.fur = fur;
	}
	public int getSwims() {
		return swims;
	}
	public void setSwims(int swims) {
		this.swims = swims;
	}
	public LayEggs getLaysEggs() {
		return laysEggs;
	}
	public void setLaysEggs(LayEggs laysEggs) {
		this.laysEggs = laysEggs;
	}
		
}

class AnimalAttributeHelper{
	private String attributeName;
	private int typeNum;
	
	public AnimalAttributeHelper(String attributeName,int typeNum ){
		this.attributeName=attributeName;
		this.typeNum=typeNum;
	}
	
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public int getTypeNum() {
		return typeNum;
	}
	public void setTypeNum(int typeNum) {
		this.typeNum = typeNum;
	}
	
}


enum LayEggs{
	isLay,notLay
}
