package io.leavesfly.base;

/**
 * �����Ŵ��㷨�� f(x)=x*sin(PI*x) ��[0,4]�����ֵ
 * 
 * @author leavesfly
 * 
 */
public class GeneticAlgori {
	private final int solitaryNum = 6;
	private final float crossProbability = 0.2f;
	private final float mutationProbability = 0.1f;

	private Solitary[] initSolGroup;
	private Solitary[] updateSolGroup;
	public float sufficiencySum;


	public GeneticAlgori() {
		initSolGroup = new Solitary[solitaryNum];
		for (int i = 0; i < initSolGroup.length; i++) {
			initSolGroup[i] = new Solitary(Solitary.randomByte());
			sufficiencySum += initSolGroup[i].getSufficiency();
			//System.out.println(initSolGroup[i].getSufficiency());
		}
	}

	private void resetSuffSum(Solitary[] solGroup) {
		sufficiencySum=0;
		for (int i = 0; i < solGroup.length; i++) {
			sufficiencySum += solGroup[i].getSufficiency();
		}
	}

	public void fillSolGroup(Solitary[] solGroup) {
		resetSuffSum(solGroup);
		for (int i = 0; i < solitaryNum; i++) {
			solGroup[i].setPercent(solGroup[i].getSufficiency()
					/ sufficiencySum);
			//System.out.println(solGroup[i].getPercent());
		}
		//System.out.println("============================");
		for (int i = 0; i < solitaryNum; i++) {
			if (i == 0) {
				solGroup[i].setAddPercent(solGroup[i].getPercent());
			} else {
				solGroup[i].setAddPercent(solGroup[i].getPercent()
						+ solGroup[i - 1].getAddPercent());
			}
		}
	}

	public int getSolIdByRanDomPro(float randomPro, float[] addPerArray) {
		for (int i = 0; i < addPerArray.length; i++) {
			if (addPerArray[i] > randomPro) {
				return i;
			}
		}
		return 0;
	}

	// 1.ѡ��
	public Solitary[] selection(Solitary[] solGroup) {
		Solitary sol = null;
		int id = 0;
		float randomPro = 0f;
		Solitary[] updateSolGroup = new Solitary[solitaryNum];
		float[] addPerArray = new float[solitaryNum];
		for (int i = 0; i < solitaryNum; i++) {
			addPerArray[i] = solGroup[i].getAddPercent();
			//System.out.println(addPerArray[i] );
		}
		//System.out.println("-------------------------------------");
		for (int i = 0; i < solitaryNum; i++) {
			randomPro = (float) Math.random();
			id = getSolIdByRanDomPro(randomPro, addPerArray);
			//System.out.println(id);
			sol = new Solitary(solGroup[id]);
			//sol.print();
			updateSolGroup[i] = sol;
		}

		return updateSolGroup;

	}

	// 2.����
	private void crossover(Solitary[] solArray) {
		solArray[0].cross(solArray[1]);
	}

	public void cross(Solitary[] solGroup) {
		Solitary[] solArray=new Solitary[2];
		for(int i=0;i<(solitaryNum-1)/2;i++){
			if(Math.random()>crossProbability){
				solArray[0]=solGroup[i];
				solArray[1]=solGroup[solitaryNum-1-i];
				crossover(solArray);
			}	
		}
	}

	// 3.����
	public void mutation(Solitary[] solGroup) {
		for (int i = 0; i < solGroup.length; i++) {
			if (Math.random() < mutationProbability) {
				solGroup[i].mutation();
			}
		}
	}

	public Solitary maxSuff(Solitary[] solGroup) {
		float result = 0f;
		byte[] code = null;
		//System.out.println(solGroup.length);
		for (int i = 0; i < solGroup.length; i++) {
			if (result <= solGroup[i].getSufficiency()) {
				result = solGroup[i].getSufficiency();
			//	System.out.println(result);
				code = solGroup[i].getCode();
				//System.out.println(code[3]);
			}
		}
		return new Solitary(code);
	}

	// �Ŵ��㷨
	public Solitary geneticAlgori(int genNum) {
		fillSolGroup(initSolGroup);
		// ѡ��
		updateSolGroup = selection(initSolGroup);
		fillSolGroup(updateSolGroup);
		// ����
		cross(updateSolGroup);
		// ����
		mutation(updateSolGroup);
		initSolGroup = updateSolGroup;

		while (genNum > 0) {
			//ѡ��
			updateSolGroup = selection(initSolGroup);
			fillSolGroup(updateSolGroup);
			// ����
			cross(updateSolGroup);
			// ����
			mutation(updateSolGroup);
			initSolGroup = updateSolGroup;
			genNum--;
			//maxSuff(updateSolGroup).print();
		}
		//System.out.println("--------------------------");
		return maxSuff(updateSolGroup);
	}

	public static void main(String[] args) {
		GeneticAlgori[] geneAlgoriArray=new GeneticAlgori[50];
		GeneticAlgori gen=null;
		Solitary sol=null;
		Solitary temp=new Solitary(Solitary.randomByte());
		for(int i=0;i<geneAlgoriArray.length;i++){
			gen=new GeneticAlgori();
			geneAlgoriArray[i]=gen;
			sol=gen.geneticAlgori(10);
			//sol.print();
			if(temp.getSufficiency()<sol.getSufficiency()){
				temp=sol;
			}
		}
		System.out.println("--------------------------");
		temp.print();

	}
}

class Solitary {
	private static final float PI = 3.1415f;
	private static final int accuracy = 12;
	private static final int crossBitNum = 6;
	private static final int upper = 4;
	private static final int lower = 0;

	private byte[] code;
	private float amount;
	private float sufficiency;

	private float percent;
	private float addPercent;

	public Solitary(byte[] code) {
		this.code = code;
		this.amount=codeToAmount();
		this.sufficiency = accSufficiency();
	}

	public Solitary(Solitary sol) {
		this.code = sol.getCode().clone();
		this.setAmount(sol.getAmount());
		this.setSufficiency(sol.getSufficiency());
		this.percent = sol.getPercent();
		this.addPercent = sol.getAddPercent();
	}

	public static byte[] randomByte() {
		byte[] code = new byte[accuracy];
		for (int i = 0; i < code.length; i++) {
			if (Math.random() >= 0.5f) {
				code[i] = 1;
			} else {
				code[i] = 0;
			}
		}
		return code;
	}

	public byte[] getCode() {
		return code;
	}

	public void setCode(byte[] code) {
		this.code = code;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getSufficiency() {
		return sufficiency;
	}

	public void setSufficiency(float sufficiency) {
		this.sufficiency = sufficiency;
	}

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
	}

	public float getAddPercent() {
		return addPercent;
	}

	public void setAddPercent(float addPercent) {
		this.addPercent = addPercent;
	}

	public float codeToAmount() {
		float result = 0f;
		float temp = 0;
		for (int i = 0; i < code.length; i++) {
			if (code[i] == 1) {
				temp += (float) Math.pow(2, accuracy-i-1);
			}
		}
		result = lower + ((float) (temp * (upper - lower)))
				/ ((float) (Math.pow(2, accuracy) - 1));
		return result;
	}

	public float accSufficiency() {
		float result = 0f;
		result = (float) (this.amount * Math.sin(10*PI* amount))+5;
		return result;
	}

	// ����
	public void cross(Solitary solitary) {
		int bitPoint = 0;
		int temp = 0;
		for (int i = 0; i < crossBitNum; i++) {
			bitPoint = (int) Math.random() * (accuracy - 1);
			temp = code[bitPoint];
			code[bitPoint] = solitary.getCode()[bitPoint];
			solitary.getCode()[bitPoint] = (byte) temp;
			}
		
		this.amount=codeToAmount();
		this.sufficiency = accSufficiency();
		
		solitary.setAmount(solitary.codeToAmount());
		solitary.setSufficiency(solitary.accSufficiency());
	}

	// ����
	public void mutation() {
		int bitPoint = (int) Math.random() * (accuracy - 1);
		if (code[bitPoint] == 0) {
			code[bitPoint] = 1;
		} else {
			code[bitPoint] = 0;
		}
		this.amount=codeToAmount();
		this.sufficiency = accSufficiency();
	}
	
	private void printCode(){
		for(int i=0;i<code.length;i++){
			System.out.print(code[i]);
		}
		System.out.println();
	}
	public  void print(){
		System.out.print("code:");
		printCode();
		System.out.println("amount:"+amount);
		System.out.println("sufficiency:"+sufficiency);
	}
}
