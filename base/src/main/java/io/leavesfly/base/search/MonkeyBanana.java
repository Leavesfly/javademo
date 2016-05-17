package io.leavesfly.base.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MonkeyBanana {

	private LinkedList<MBProState> openTable;
	private Set<MBProState> closedTable;

	public MonkeyBanana() {
		openTable = new LinkedList<MBProState>();
		closedTable = new HashSet<MBProState>();
	}

	/**
	 * 
	 */
	// ������Ȳ���
	public void findBananaBreadthFirst() {
		MBProState initState = new MBProState('b', 'a');
		List<MBProState> stateList = initState.getAllNextState();

		for (int i = 0; i < stateList.size(); i++) {
			stateList.get(i).setProblemState(initState);
			if (stateList.get(i).isGetBanana() == true) {
				String str = null;
				MBProState proState = stateList.get(i);
				str = proState.toString();
				while (proState.getProblemState() != null) {
					proState = proState.getProblemState();
					str += " <= " + proState.toString();
				}
				System.out.println(str);
				return;
			}
		}
		openTable.addAll(stateList);
		closedTable.add(initState);

		while (!openTable.isEmpty()) {
			initState = openTable.removeFirst();
			if (!isContain(initState)) {
				stateList = initState.getAllNextState();
				for (int i = 0; i < stateList.size(); i++) {
					stateList.get(i).setProblemState(initState);
					if (!closedTable.contains(stateList.get(i))) {
						openTable.addLast(stateList.get(i));
					}
					if (stateList.get(i).isGetBanana() == true) {
						String str = null;
						MBProState proState = stateList.get(i);
						str = proState.toString();
						while (proState.getProblemState() != null) {
							proState = proState.getProblemState();
							str += " <= " + proState.toString();
						}
						System.out.println(str);
						return;
					}
				}
				closedTable.add(initState);
			}

		}

	}

	/**
	 * ���OPEN�����ջ�Ϳ���ʵ��������Ȳ��� �������
	 */
	public void findBananaDepthFirst() {
		MBProState initState = new MBProState('b', 'a');
		List<MBProState> stateList = initState.getAllNextState();

		for (int i = 0; i < stateList.size(); i++) {
			stateList.get(i).setProblemState(initState);
			if (stateList.get(i).isGetBanana() == true) {
				String str = null;
				MBProState proState = stateList.get(i);
				str = proState.toString();
				while (proState.getProblemState() != null) {
					proState = proState.getProblemState();
					str += " <= " + proState.toString();
				}
				System.out.println(str);
				return;
			}
		}
		// ��stateList�Ƿ��������ô������뵽OPEN���о����˲���ʲô�㷨
		//��ʱ�Ǿֲ�����
		//��OPEN�����������ȫ������
		//A��A*�㷨Ҫ���ǵ����ۺ���g(x)   f(x)=g(x)+h(x)
		openTable.addAll(0, stateList);
		closedTable.add(initState);
		while (!openTable.isEmpty()) {
			initState = openTable.removeFirst();
			if (!isContain(initState)) {
				stateList = initState.getAllNextState();
				for (int i = 0; i < stateList.size(); i++) {
					stateList.get(i).setProblemState(initState);
					if (!closedTable.contains(stateList.get(i))) {
						openTable.addLast(stateList.get(i));
					}
					if (stateList.get(i).isGetBanana() == true) {
						String str = null;
						MBProState proState = stateList.get(i);
						str = proState.toString();
						while (proState.getProblemState() != null) {
							proState = proState.getProblemState();
							str += " <= " + proState.toString();
						}
						System.out.println(str);
						return;
					}
				}
				closedTable.add(initState);
			}

		}
	}

	public boolean isContain(MBProState proState) {
		Iterator<MBProState> iPro = closedTable.iterator();
		MBProState state = null;
		while (iPro.hasNext()) {
			state = iPro.next();
			if (state.equal(proState)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		MonkeyBanana mb = new MonkeyBanana();
		 mb.findBananaBreadthFirst();
		//mb.findBananaDepthFirst();
	 //System.out.println(new MBProState('a', 'b'));
	}

}

class MBProState {
	private static final char bananaPosition = 'c';
	private char monkeyPosition;
	private boolean isOnBox;
	private char boxPosition;
	private boolean isGetBanana;
	private MBProState problemState;
	private char[] positionSet = new char[] { 'a', 'b', 'c' };

	public char[] positionsCanGo(char position) {
		char[] chars = new char[2];
		int m = 0;
		for (int i = 0; i < positionSet.length; i++) {
			if (position != positionSet[i]) {
				chars[m] = positionSet[i];
				m++;
			}
		}
		return chars;
	}

	public MBProState getProblemState() {
		return problemState;
	}

	public void setProblemState(MBProState problemState) {
		this.problemState = problemState;
	}

	public char getMonkeyPosition() {
		return monkeyPosition;
	}

	public void setMonkeyPosition(char monkeyPosition) {
		this.monkeyPosition = monkeyPosition;
	}

	public boolean isOnBox() {
		return isOnBox;
	}

	public void setOnBox(boolean isOnBox) {
		this.isOnBox = isOnBox;
	}

	public char getBoxPosition() {
		return boxPosition;
	}

	public void setBoxPosition(char boxPosition) {
		this.boxPosition = boxPosition;
	}

	public boolean isGetBanana() {
		return isGetBanana;
	}

	public void setGetBanana(boolean isGetBanana) {
		this.isGetBanana = isGetBanana;
	}

	public MBProState() {

	}

	public MBProState(char monkeyPosition, char boxPosition) {
		this.monkeyPosition = monkeyPosition;
		this.boxPosition = boxPosition;
		isOnBox = false;
		isGetBanana = false;
	}

	public MBProState(MBProState state) {
		this.monkeyPosition = state.getMonkeyPosition();
		this.isOnBox = state.isOnBox;
		this.boxPosition = state.getBoxPosition();
		this.isGetBanana = state.isOnBox;
	}

	public List<MBProState> getAllNextState() {
		List<MBProState> stateList = new ArrayList<MBProState>();
		if (isOnBox == false && isGetBanana == false) {
			char[] positions = positionsCanGo(monkeyPosition);
			for (int i = 0; i < positions.length; i++) {
				stateList.add(monkeyGoTo(positions[i]));
			}

		}
		if (monkeyPosition == boxPosition && isOnBox == false
				&& isGetBanana == false) {
			char[] positions = positionsCanGo(boxPosition);
			for (int i = 0; i < positions.length; i++) {
				stateList.add(pushBoxTo(positions[i]));

			}
			stateList.add(climbBox());
		}
		if (monkeyPosition == boxPosition && isOnBox == true
				&& monkeyPosition == bananaPosition) {
			stateList.add(graspBanana());
		}

		return stateList;
	}

	public MBProState monkeyGoTo(char postion) {
		MBProState state = new MBProState(this);
		state.setMonkeyPosition(postion);
		return state;
	}

	public MBProState pushBoxTo(char position) {
		MBProState state = new MBProState(this);
		state.setMonkeyPosition(position);
		state.setBoxPosition(position);
		return state;
	}

	public MBProState climbBox() {
		MBProState state = new MBProState(this);
		state.isOnBox = true;
		return state;
	}

	public MBProState graspBanana() {
		MBProState state = new MBProState(this);
		state.isGetBanana = true;
		return state;
	}

	public boolean equal(MBProState state) {
		if (this.monkeyPosition == state.getMonkeyPosition()
				&& this.boxPosition == state.getBoxPosition()
				&& this.isOnBox == state.isOnBox
				&& this.isGetBanana == state.isGetBanana) {
			return true;
		}
		return false;
	}
/**
 * ���������������h(x)
 * @return
 */

	public int distanceToBanana(){
		
		return 0;
	}
	
	
	@Override
	public String toString() {
		return "(" + monkeyPosition + "," + isOnBox + "," + boxPosition + ","
				+ isGetBanana + ")";
	}

}
