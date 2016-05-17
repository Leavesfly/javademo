package io.leavesfly.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Apriori {
	private List<String> dataSource;
	private List<String> unitSet;
	private List<String> itemSet;
	private List<String> maxItemSet;
	private static final float minConfidence = 0.5f;
	private static final int minSupportNum = 2;
	private static final String filePath = "XXXXXXX";

	public Apriori() {
		itemSet = new ArrayList<String>();
		maxItemSet = new LinkedList<String>();
		dataSource = new ArrayList<String>();
		readDataFromFile();
		unitSet = new ArrayList<String>();
		fillUnitSet();
	}

	public List<String> getUnitSet() {
		return unitSet;
	}

	public void setUnitSet(List<String> unitSet) {
		this.unitSet = unitSet;
	}

	private void readDataFromFile() {
		try {
			FileInputStream fileIn = new FileInputStream(new File(filePath));
			BufferedReader bufferedIn = new BufferedReader(
					new InputStreamReader(fileIn));
			String str = null;
			while ((str = bufferedIn.readLine()) != null) {
				dataSource.add(str);
				System.out.println(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fillUnitSet() {
		for (int i = 0; i < dataSource.size(); i++) {
			String[] strArray = dataSource.get(i).split(",");
			for (int j = 0; j < strArray.length; j++) {
				String str = strArray[j];
				if (!unitSet.contains(str)) {
					unitSet.add(str);
				}

			}
		}

	}

	public Map<String, Integer> initalFrequentSet() {
		Map<String, Integer> candidateSet = new HashMap<String, Integer>();
		for (int i = 0; i < dataSource.size(); i++) {
			String[] strArray = dataSource.get(i).split(",");
			for (int j = 0; j < strArray.length; j++) {
				String str = strArray[i];
				if (!candidateSet.containsKey(str)) {
					candidateSet.put(str, 1);
				} else {
					int count = candidateSet.get(str) + 1;
					candidateSet.put(str, count);
				}
			}
		}
		return candidateSet;

	}

	public List<String> getFrequentSetByCandidateSet(
			Map<String, Integer> candidateSet) {
		List<String> frequentSet = new ArrayList<String>();
		for (String str : candidateSet.keySet()) {
			if (candidateSet.get(str) >= minSupportNum) {
				frequentSet.add(str);
			}
		}
		return frequentSet;
	}

	private static String[] kSubSet(String str) {
		String[] strArray = str.split(",");
		String[] stringArray = new String[strArray.length];
		for (int i = 0; i < strArray.length; i++) {
			String strTemp = "";
			for (int j = 0; j < strArray.length; j++) {
				if (j < strArray.length - 1) {
					if (j != i) {
						strTemp += strArray[j] + ",";
					}
				} else {
					if (j != i) {
						strTemp += strArray[j];
					}
				}
			}
			stringArray[i] = strTemp;
		}
		return stringArray;
	}

	private int getPositionOfUnitSet(String str) {
		for (int i = 0; i < unitSet.size(); i++) {
			if (str.equals(unitSet.get(i))) {
				return i;
			}
		}
		return -1;
	}

	private boolean isContains(String base, String match) {
		String[] strArray = base.split(",");
		String[] stringArray = match.split(",");
		int count = 0;
		int strArrayPoint = 0;
		for (int i = 0; i < stringArray.length; i++) {
			for (int j = strArrayPoint; j < strArray.length; j++) {
				if (stringArray[i].equals(strArray[j])) {
					count++;
					if (count == stringArray.length) {
						return true;
					}
					strArrayPoint = j + 1;
					break;
				}
			}
		}
		return false;
	}

	private List<String> allModelByFrequentSet(List<String> frequentSet) {
		List<String> allModel = new ArrayList<String>();
		for (int i = 0; i < frequentSet.size(); i++) {
			String str = frequentSet.get(i);
			String strMatch = str.substring(str.length() - 1);
			for (int j = getPositionOfUnitSet(strMatch) + 1; j < unitSet.size(); j++) {
				String strTemp = new String(str);
				strTemp += "," + unitSet.get(j);
				for (int m = 0; m < dataSource.size(); m++) {
					if (isContains(dataSource.get(m), strTemp)) {
						allModel.add(strTemp);
					}
				}

			}
		}

		return allModel;
	}

	public boolean hasInfrequentSubset(String candidateUnit,
			List<String> frequentSet) {
		String[] stringArray = kSubSet(candidateUnit);
		int countNum = 0;
		for (int i = 0; i < stringArray.length; i++) {
			for (int j = 0; j < frequentSet.size(); j++) {
				if (!stringArray[i].equals(frequentSet.get(j))) {
					countNum++;
				}
			}
			if (countNum == frequentSet.size()) {
				return true;
			}
		}

		return false;
	}

	public Map<String, Integer> genCandidateSetByFrequentSet(
			List<String> frequentSet) {
		List<String> strList = allModelByFrequentSet(frequentSet);
		for (int i = 0; i < strList.size(); i++) {
			if (hasInfrequentSubset(strList.get(i), frequentSet)) {
				strList.remove(i);
				i--;
			}
		}
		Map<String, Integer> candidateSet = new HashMap<String, Integer>();
		for (int i = 0; i < strList.size(); i++) {
			int countNum = 0;
			for (int j = 0; j < dataSource.size(); j++) {
				if (isContains(dataSource.get(j), strList.get(i))) {
					countNum++;
				}
			}
			candidateSet.put(strList.get(i), countNum);
		}

		return candidateSet;
	}

	public void aprioriAlgorithm() {
		Map<String, Integer> candidateSet = initalFrequentSet();
		List<String> frequentSet = getFrequentSetByCandidateSet(candidateSet);
		while (!frequentSet.isEmpty()) {
			Map<String, Integer> candidateSetTemp = genCandidateSetByFrequentSet(frequentSet);
			frequentSet = getFrequentSetByCandidateSet(candidateSetTemp);
			itemSet.addAll(frequentSet);
		}
	}

	public void printMaxItemSet() {
		String str = "{";
		for (int i = 0; i < itemSet.size(); i++) {
			str += itemSet.get(i) + "  ";
		}
		str += "}";
		System.out.println(str);
	}

	public void findMaxItem() {
		maxItemSet.addAll(itemSet);
		for (int i = 0; i < maxItemSet.size(); i++) {
			for (int j = i + 1; j < maxItemSet.size(); j++) {
				if (isContains(maxItemSet.get(j), maxItemSet.get(i))) {
					maxItemSet.remove(i);
					i--;
					j--;
				}
			}
		}
	}

	// ====================================�������������㷨================================

	public String cutString(String root, String match) {
		List<String> strList = new ArrayList<String>();
		String[] strArray = root.split(",");
		for (int i = 0; i < strArray.length; i++) {
			if (!match.matches(strArray[i])) {
				strList.add(strArray[i]);
			}
		}
		String str = "";
		for (int i = 0; i < strList.size(); i++) {
			if (i != strList.size() - 1) {
				str += strList.get(i) + ",";
			} else {
				str += strList.get(i);
			}
		}
		return str;

	}



	public float getConfidence(String str, String strCut) {
		int supportStr = 0;
		int supportCut = 0;
		for (int i = 0; i < dataSource.size(); i++) {
			if (isContains(dataSource.get(i), str)) {
				supportStr++;
			}
			if (isContains(dataSource.get(i), strCut)) {
				supportCut++;
			}
		}

		float confidence = ((int) supportStr) / ((int) supportCut);
		return confidence;
	}

	// ����ͼ�Ŀ�ȱ��� !(root.length)
	public void ruleGenerate() {
		LinkedList<String> strQueue = new LinkedList<String>();
		Set<String> strSet = new HashSet<String>();
		for (int num = 0; num < maxItemSet.size(); num++) {
			String root = maxItemSet.get(num);
			strSet.clear();
			strQueue.clear();
			if (root.length() != 1) {
				String[] strArray = kSubSet(root);
				for (int i = 0; i < strArray.length; i++) {
					strQueue.addLast(strArray[i]);
				}
				while (!strQueue.isEmpty()) {
					String strTemp = strQueue.removeFirst();
					if (getConfidence(root, strTemp) >= minConfidence) {
						System.out.println(strTemp + "==>"
								+ cutString(root, strTemp) + "��ǿ����!");
					} else {
						System.out.println(strTemp + "==>"
								+ cutString(root, strTemp) + "��������.");
					}
					strSet.add(strTemp);
					if (strTemp.length() != 1) {
						strArray = kSubSet(strTemp);
						for (int i = 0; i < strArray.length; i++) {
							if (!strSet.contains(strArray[i])) {
								strQueue.addLast(strArray[i]);
							}

						}
					}

				}
			}
		}
	}

	public static void main(String[] args) {
		Apriori apriori = new Apriori();
		// apriori.aprioriAlgorithm();
		// apriori.printMaxItemSet();
		System.out.println(apriori.getUnitSet());
		System.out.println(apriori.getUnitSet().size());

	}
}
