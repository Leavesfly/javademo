package io.leavesfly.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @author LeavesFly
 *
 */
public class CodeCounter {
	private static long codeLineNumber = 0L;
	private static long codeFileNumber = 0L;

	private String filePath;

	public CodeCounter(String filePath) {
		this.filePath = filePath;
	}

	public static void main(String[] args) {
		 String filePath = "D:\\JavaTools\\workspace2\\smallsql\\src\\main";
		//String filePath = "D:\\JavaTools\\workspace2\\guagua-master";
		// String filePath = "D:\\JavaTools\\workspace2\\codecounter";
		CodeCounter codeCounter = new CodeCounter(filePath);
		codeCounter.countCodeLine();

		System.out.println("Code File Number :" + codeCounter.getCodeFileNumber());
		System.out.println("Code Line Number :" + codeCounter.getCodeLineNumber());
	}

	public long getCodeLineNumber() {
		if (codeLineNumber == 0 || codeFileNumber == 0) {
			countCodeLine();
		}
		return codeLineNumber;
	}

	public long getCodeFileNumber() {
		if (codeLineNumber == 0 || codeFileNumber == 0) {
			countCodeLine();
		}
		return codeFileNumber;
	}

	public void countCodeLine() {
		File file = new File(filePath);

		visitFile(file, new AcceptVisitable() {

			@Override
			public void visit(File file) {
				BufferedReader bufferedRead = null;
				try {
					bufferedRead = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
					while (bufferedRead.readLine() != null) {
						++codeLineNumber;
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e.getCause());
				} finally {
					if (bufferedRead != null) {
						try {
							bufferedRead.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}

			}

			@Override
			public boolean isAccepted(File file) {
				if (file != null && file.getName().endsWith(".java")) {
					++codeFileNumber;
					return true;
				}
				return false;

			}
		});
	}

	private void visitFile(File file, AcceptVisitable acceptVisitable) {

		if (file.isDirectory()) {
			File[] fileArray = file.listFiles();
			for (File tmpFile : fileArray) {
				visitFile(tmpFile, acceptVisitable);
			}
		} else if (file.isFile() && acceptVisitable.isAccepted(file)) {
			acceptVisitable.visit(file);
		}
	}

	/**
	 * 
	 * @author LeavesFly
	 *
	 */
	private static interface AcceptVisitable {

		boolean isAccepted(File file);

		void visit(File file);
	}
}