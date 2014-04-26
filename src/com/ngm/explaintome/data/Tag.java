package com.ngm.explaintome.data;

public class Tag extends ModelElement {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Tag [name=" + name + "]";
	}
	
}
