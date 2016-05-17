package io.leavesfly.ioc;

public class Dog implements Animal {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void say() {
		System.out.println("Dog--->" + name);
	}

}
