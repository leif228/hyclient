package com.hangyi.zd.domain;

public enum NodeCode {
	arrivedLoadingDock(1,"抵达装点"), 
	loadingStart(2,"开始装货"), 
	loadingEnd(3,"装货完毕"), 
	leaveLoadingDock(4,"驶离装点"), 
	arrivedUnloadingDock(5,"抵达卸点"), 
	unloadingStart(6,"开始卸货"), 
	unloadingEnd(7,"卸货完毕"),//9绕航报警10中途停航报警11超航时报警12超里程报警
	unRoutePolice(9,"绕航报警"), 
	stopRoutePolice(10,"停航报警"), 
	moreTRoutePolice(11,"超时报警"), 
	moreDRoutePolice(12,"超程报警");

	private int n;
	private String description;

	private NodeCode(int n,String description) {
		this.n = n;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public int getN() {
		return n;
	}

	public static NodeCode getByN(int n){
		for(NodeCode nc:NodeCode.values()){
			if(nc.getN()==n)
				return nc;
		}
		return null;
	}
}
