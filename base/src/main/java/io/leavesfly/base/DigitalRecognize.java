package io.leavesfly.base;

public class DigitalRecognize {
	private byte[][][] digitalMap;
	private static final int digitalNum = 10;
	private static final int rowNum = 5;
	private static final int collNum = 4;

	public DigitalRecognize() {
		digitalMap = new byte[digitalNum][][];
		byte[][] digital = null;

		digital = new byte[][] { { 1, 1, 1, 1 }, { 1, 0, 0, 1 },
				{ 1, 0, 0, 1 }, { 1, 0, 0, 1 }, { 1, 1, 1, 1 } };
		digitalMap[0] = digital;

		digital = new byte[][] { { 0, 0, 0, 1 }, { 0, 0, 0, 1 },
				{ 0, 0, 0, 1 }, { 0, 0, 0, 1 }, { 0, 0, 0, 1 } };
		digitalMap[1] = digital;

		digital = new byte[][] { { 1, 1, 1, 1 }, { 0, 0, 0, 1 },
				{ 1, 1, 1, 1 }, { 1, 0, 0, 0 }, { 1, 1, 1, 1 } };
		digitalMap[2] = digital;

		digital = new byte[][] { { 1, 1, 1, 1 }, { 0, 0, 0, 1 },
				{ 1, 1, 1, 1 }, { 0, 0, 0, 1 }, { 1, 1, 1, 1 } };
		digitalMap[3] = digital;

		digital = new byte[][] { { 1, 0, 0, 1 }, { 1, 0, 0, 1 },
				{ 1, 1, 1, 1 }, { 0, 0, 0, 1 }, { 0, 0, 0, 1 } };
		digitalMap[4] = digital;

		digital = new byte[][] { { 1, 1, 1, 1 }, { 1, 0, 0, 0 },
				{ 1, 1, 1, 1 }, { 0, 0, 0, 1 }, { 1, 1, 1, 1 } };
		digitalMap[5] = digital;

		digital = new byte[][] { { 1, 1, 1, 1 }, { 1, 0, 0, 0 },
				{ 1, 1, 1, 1 }, { 1, 0, 0, 1 }, { 1, 1, 1, 1 } };
		digitalMap[6] = digital;

		digital = new byte[][] { { 1, 1, 1, 1 }, { 0, 0, 0, 1 },
				{ 0, 0, 0, 1 }, { 0, 0, 0, 1 }, { 0, 0, 0, 1 } };
		digitalMap[7] = digital;

		digital = new byte[][] { { 1, 1, 1, 1 }, { 1, 0, 0, 1 },
				{ 1, 1, 1, 1 }, { 1, 0, 0, 1 }, { 1, 1, 1, 1 } };
		digitalMap[8] = digital;

		digital = new byte[][] { { 1, 1, 1, 1 }, { 1, 0, 0, 1 },
				{ 1, 1, 1, 1 }, { 0, 0, 0, 1 }, { 1, 1, 1, 1 } };
		digitalMap[9] = digital;
	}

	public void print() {
		for (int i = 0; i < digitalNum; i++) {
			for (int j = 0; j < rowNum; j++) {
				for (int m = 0; m < collNum; m++) {
					if (digitalMap[i][j][m] == 0) {
						System.out.print(' ');
					} else {
						System.out.print(digitalMap[i][j][m]);
					}
				}
				System.out.println();
			}
			System.out.println("----------------------------");
		}
	}
	
	private boolean isMatch(byte[][] code,byte[][] originalCode){
		for(int i=0;i<originalCode.length;i++){
			for(int j=0;j<originalCode[i].length;j++){
				if(code[i][j]!=originalCode[i][j]){
					return false;
				}
			}
		}
		return true;
	}
	
	public int covCodeToDigital(byte[][] code){
		for(int i=0;i<digitalMap.length;i++){
			if(isMatch(code, digitalMap[i])){
				return i;
			}
		}
		return -1;
	}
	
	
	public static void main(String[] args) {
		DigitalRecognize dg = new DigitalRecognize();
		byte[][] digital = null;
		digital = new byte[][] { { 1, 1, 1, 1 }, { 1, 0, 0, 0 },
				{ 1, 1, 1, 1 }, { 0, 0, 0, 1 }, { 1, 1, 1, 1 } };
		System.out.println(dg.covCodeToDigital(digital));
		//dg.print();
	}

}
