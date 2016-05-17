package io.leavesfly.base.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Fanta {
	private LinkedList<SubProblem> openTable;
	private HashSet<SubProblem> closedTable;

	public Fanta() {
		openTable = new LinkedList<SubProblem>();
		closedTable = new HashSet<SubProblem>();
	}

	public void breadthFirstSearch() {
		SubProblem initProblem = new SubProblem(3, 1, 2, 3);
		openTable.addLast(initProblem);
		List<SubProblem> sunProList = null;
		while (true) {

			initProblem = openTable.removeFirst();
			closedTable.add(initProblem);
			
			sunProList = initProblem.getAllNextProList();
			if (sunProList.size() == 0) {
				
				
				if (initProblem.getProblemWide() == 1) {
					initProblem.setSolved(true);
					if(initProblem.getPointToFarther()!=null){
						
					}
					System.out.println(initProblem.getNowState() + "=>"
							+ initProblem.getDestination());
					
				} else {
					System.out.println("�������!");
				}		
				
			} else {
				openTable.addAll(sunProList);
				
				
			}

		}

	}
	
	public List<SubProblem> stopProblem(List<SubProblem> sunProList){
		for (int i = 0; i < sunProList.size(); i++) {
			
		}
		return null;
	}
	
	
	//������
	public void geneTree(){
		SubProblem initProblem = new SubProblem(3, 1, 2, 3);
		//openTable.addLast(initProblem);
		List<SubProblem> sunProList = null;
		closedTable.add(initProblem);
		sunProList = initProblem.getAllNextProList();
		if (sunProList.size() == 0) {				
			if (initProblem.getProblemWide() == 1) {		
				System.out.println(initProblem.getNowState() + "=>"
						+ initProblem.getDestination());			
			} else {
				System.out.println("�������!");
			}	
		} 
		openTable.addAll(sunProList);
		while(!openTable.isEmpty()){	
			initProblem=openTable.removeFirst();
			closedTable.add(initProblem);
			sunProList = initProblem.getAllNextProList();
			if(sunProList.size()!=0){
				openTable.addAll(sunProList);
			}
		}
		
	}
	//����Ҷ�ӽڵ� ��������Ҷ�ӱ��������ɻ�ò�������
	
	
	public static void main(String[] args){
		
	}

}

class SubProblem {
	private int problemWide;
	private int nowState;
	private int byUse;
	private int destination;
	private boolean isSolved;
	private SubProblem pointToFarther;

	public SubProblem(int problemWide, int nowState, int byUse, int desination) {
		this.problemWide = problemWide;
		this.nowState = nowState;
		this.byUse = byUse;
		this.destination = desination;
	}

	public int getProblemWide() {
		return problemWide;
	}

	public void setProblemWide(int problemWide) {
		this.problemWide = problemWide;
	}

	public int getNowState() {
		return nowState;
	}

	public void setNowState(int nowState) {
		this.nowState = nowState;
	}

	public int getByUse() {
		return byUse;
	}

	public void setByUse(int byUse) {
		this.byUse = byUse;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public boolean isSolved() {
		return isSolved;
	}

	public void setSolved(boolean isSolved) {
		this.isSolved = isSolved;
	}

	public SubProblem getPointToFarther() {
		return pointToFarther;
	}

	public void setPointToFarther(SubProblem pointToFarther) {
		this.pointToFarther = pointToFarther;
	}

	public List<SubProblem> getAllNextProList() {
		SubProblem subPro = null;
		List<SubProblem> sunProList = new ArrayList<SubProblem>();
		if(problemWide>=2){
			subPro = new SubProblem(problemWide - 1, nowState, destination, byUse);
			subPro.setPointToFarther(this);
			sunProList.add(subPro);

			subPro = new SubProblem(1, nowState, byUse, destination);
			subPro.setPointToFarther(this);
			sunProList.add(subPro);

			subPro = new SubProblem(problemWide - 1, byUse, nowState, destination);
			subPro.setPointToFarther(this);
			sunProList.add(subPro);
		}
		
		return sunProList;
	}

}