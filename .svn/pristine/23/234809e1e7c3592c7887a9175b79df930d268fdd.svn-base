package com.hangyi.zd.domain;

public enum GroupCode {
	gport(1,"码头"), 
	gkehu(2,"客户"), 
	gship(3,"船队");

	private int n;
	private String description;

	private GroupCode(int n,String description) {
		this.n = n;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public int getN() {
		return n;
	}

	public static GroupCode getByN(int n){
		for(GroupCode nc:GroupCode.values()){
			if(nc.getN()==n)
				return nc;
		}
		return null;
	}
}
