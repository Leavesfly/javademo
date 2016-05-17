package io.leavesfly.base;

import java.util.ArrayList;
import java.util.List;

/**
 * ����ʶ��
 * 
 * @author leavesfly
 * 
 */
public class BP {
	private static final int OutputCellNum = 1;
	private static final int hideCellRowNUm = 20;
	private static final int InputCellNum = 20;
	private static final int HideLevel = 2;

	private final float learnRate;
	private final int iterationNum;

	private List<CellValue[]> hideCellValueArrayOfList;

	private float[][] maxtriInputHide;
	private List<float[][]> ListOfmaxtriHide;

	private float[] maxtriHideOutput;
	
	private byte[][] codeInput;
	private byte[] inputCode;
	private CellValue[] inputArray;

	private float outputData;
	private CellValue output;
	private int exceptOut;

	private float weightInitLow;
	private float weightInitUpper;

	public BP() {

		iterationNum = 1000;
		learnRate = 0.1f;

		// ��ʼ��inputCode
		codeInput=new byte[][] { { 1, 1, 1, 1 }, { 0, 0, 0, 1 },
				{ 1, 1, 1, 1 }, { 1, 0, 0, 0 }, { 1, 1, 1, 1 } };
		
		inputCode = new byte[InputCellNum];
		for(int i=0;i<InputCellNum;i++){
			inputCode[i]=codeInput[i/4][i%4];
		}
		
		
		exceptOut = new DigitalRecognize().covCodeToDigital(codeInput);
		System.out.println(exceptOut);

		inputArray = new CellValue[InputCellNum];
		CellValue cellValue = null;
		for (int i = 0; i < InputCellNum; i++) {
			cellValue = new CellValue(inputCode[i]);
			inputArray[i] = cellValue;
		}

		// ��ʼ��output
		output = new CellValue();
		outputData = 0;

		// --------------------------------�����ʼ��------------
		weightInitLow = (float) (-5/InputCellNum );
		weightInitUpper = (float) (5/InputCellNum);

		maxtriInputHide = new float[InputCellNum][];
		float[] temp = null;
		for (int i = 0; i < InputCellNum; i++) {
			temp = new float[hideCellRowNUm];
			for (int j = 0; j < hideCellRowNUm; j++) {
				temp[j] = weightInitLow + (weightInitUpper - weightInitLow)
						* (float) Math.random();
			}
			maxtriInputHide[i] = temp;
		}

		maxtriHideOutput = new float[hideCellRowNUm];
		for (int i = 0; i < hideCellRowNUm; i++) {
			maxtriHideOutput[i] = weightInitLow
					+ (weightInitUpper - weightInitLow) * (float) Math.random();
		}

		float[][] maxtriTemp = null;
		ListOfmaxtriHide = new ArrayList<float[][]>();
		for (int i = 1; i < HideLevel ; i++) {
			maxtriTemp = new float[hideCellRowNUm][];
			for (int j = 0; j < hideCellRowNUm; j++) {
				temp = new float[hideCellRowNUm];
				for (int m = 0; m < hideCellRowNUm; m++) {
					temp[m] = weightInitLow + (weightInitUpper - weightInitLow)
							* (float) Math.random();
				}
				maxtriTemp[j] = temp;
			}
			ListOfmaxtriHide.add(maxtriTemp);
		}
		// --------------------------------------

		hideCellValueArrayOfList = new ArrayList<CellValue[]>();
		CellValue[] cellValueArray = null;
		CellValue cell=null;
		for (int i = 0; i < HideLevel; i++) {
			cellValueArray = new CellValue[hideCellRowNUm];
			for(int j=0;j<hideCellRowNUm;j++){
				cell=new CellValue();
				cellValueArray[j]=cell;
			}
			hideCellValueArrayOfList.add(cellValueArray);
		}

	}


	private void maxtriMultip(float[][] maxtri, CellValue[] original,
			CellValue[] result) {
		for (int i = 0; i < maxtri.length; i++) {
			float temp = 0f;
			for (int j = 0; j < maxtri[i].length; j++) {
				temp += maxtri[i][j] * original[j].getOutput();
			}
			//System.out.println(result[i].getOutput());
			result[i].setInput(temp);
			result[i].cellOutput();
		}
	}

	public boolean calOutputFlush() {
		maxtriMultip(maxtriInputHide, inputArray,
				hideCellValueArrayOfList.get(0));
		for (int i = 1; i < HideLevel; i++) {
			maxtriMultip(ListOfmaxtriHide.get(i - 1),
					hideCellValueArrayOfList.get(i-1),
					hideCellValueArrayOfList.get(i));
		}
		CellValue[] cellValueArray = new CellValue[OutputCellNum];
		cellValueArray[0] = output;
		float[][] maxtri = new float[OutputCellNum][];
		maxtri[0] = maxtriHideOutput;

		maxtriMultip(
				maxtri,
				hideCellValueArrayOfList.get(hideCellValueArrayOfList.size() - 1),
				cellValueArray);
		//outputData = Math.round(output.getOutput());
		outputData=output.getOutput();
		if (outputData == exceptOut) {
			return true;
		} else {
			return false;
		}
	}

	// ------------------------------

	private float outputLevelError(float output) {

		return output * (1 - output) * (exceptOut - output);
	}

	public void weightChangeHideOutput() {

		float outputError = outputLevelError(output.getOutput());
		for (int i = 0; i < maxtriHideOutput.length; i++) {
			maxtriHideOutput[i] += outputError
					* learnRate
					* hideCellValueArrayOfList.get(HideLevel - 1)[i]
							.getOutput();
		}
	}

	// ----------------------------

	private float hideOutputSum(int location) {

		return maxtriHideOutput[location];
	}

	private float hideHideSum(int location) {
		float result = 0f;
		for (int i = 0; i < ListOfmaxtriHide.get(0)[location].length; i++) {
			result += ListOfmaxtriHide.get(0)[location][i];
		}

		return result;
	}

	public float hideOutputError(CellValue cellValue, int location) {
		float hideError = cellValue.getOutput() * (1 - cellValue.getOutput())
				* hideOutputSum(location) * cellValue.getOutput();
		return hideError;
	}

	public float hideHideError(CellValue cellValue, int location) {

		float hideError = cellValue.getOutput() * (1 - cellValue.getOutput())
				* hideHideSum(location) * cellValue.getOutput();
		return hideError;
	}

	public void weightChangeHideHide() {
		float outputError = 0f;
		for (int i = 0; i < hideCellRowNUm; i++) {
			outputError = hideOutputError(
					hideCellValueArrayOfList.get(HideLevel - 1)[i], i);
			for (int j = 0; j < hideCellRowNUm; j++) {
				ListOfmaxtriHide.get(0)[i][j] += hideCellValueArrayOfList
						.get(0)[j].getOutput() * learnRate * outputError;
			}
		}
	}

	public void weightChangeOutput() {
		float outputError = 0f;
		for (int i = 0; i < hideCellRowNUm; i++) {
			outputError = hideHideError(
					hideCellValueArrayOfList.get(HideLevel - 2)[i], i);
			for (int j = 0; j < hideCellRowNUm; j++) {
				maxtriInputHide[i][j] += inputArray[j].getOutput() * learnRate
						* outputError;
			}
		}
	}

	public void backCompute() {
		weightChangeHideOutput();
		weightChangeHideHide();
		weightChangeOutput();
	}

	public void backPropagation() {
		int count = 0;
		while (!calOutputFlush()) {
			backCompute();
			count++;
			if (count > iterationNum) {
				break;
			}
		}
		//calOutputFlush();
		System.out.println(outputData);

	}

	// ���Գ���
	public static void main(String[] args) {
		BP bp=new BP();
		bp.backPropagation();
	}

}

class CellValue {
	private float input;
	private float output;

	public CellValue() {

	}

	public CellValue(float input) {
		this.input = input;
		output = input;
	}

	public float getInput() {
		return input;
	}

	public void setInput(float input) {
		this.input = input;
	}

	public float getOutput() {
		return output;
	}

	public void setOutput(float output) {
		this.output = output;
	}

	public void cellOutput() {
		output = 1 / (1 + (float) Math.exp(-input));
	}

}
