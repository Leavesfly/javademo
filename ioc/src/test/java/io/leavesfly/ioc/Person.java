package io.leavesfly.ioc;

public class Person {
	private Animal pet;

	public void setPet(Animal pet) {
		this.pet = pet;
	}

	public void petSay() {
		pet.say();
	}

}
