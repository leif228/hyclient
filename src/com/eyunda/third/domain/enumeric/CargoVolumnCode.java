package com.eyunda.third.domain.enumeric;

public enum CargoVolumnCode {
	teus20("20个以内", 0, 20),

	teus20_50("20-50个", 20, 50),

	tons50_100("50-100个", 50, 100),

	tons100("100个以上", 100, 99999999);

	private String description;
	private int min;
	private int max;

	private CargoVolumnCode(String description, int min, int max) {
		this.description = description;
		this.min = min;
		this.max = max;
	}

	public String getDescription() {
		return description;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public static int getMaxByMin(double min) {
		for(CargoVolumnCode c : CargoVolumnCode.values()){
			if(min == c.getMin()){
				return c.getMax();
			}
		}
		return -1;
	}
}
