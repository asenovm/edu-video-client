package com.ngm.explaintome.data;

import com.ngm.explaintome.FilterableEntity;

public class Tag extends ModelElement implements FilterableEntity {
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

	@Override
	public String getFilterText() {
		return getName();
	}

}
