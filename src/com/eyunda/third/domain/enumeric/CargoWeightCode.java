package com.eyunda.third.domain.enumeric;

public enum CargoWeightCode {
	tons100("100吨以内", 0, 100),

	tons100_300("100-300吨", 100, 300),

	tons300_500("300-500吨", 300, 500),

	tons500_1000("500-1000吨", 500, 1000),

	tons1000_3000("1000-3000吨", 1000, 3000),

	tons3000_5000("3000-5000吨", 3000, 5000),

	tons5000_10000("5000-10000吨", 5000, 10000),

	tons10000("10000吨以上", 10000, 99999999);

	private String description;
	private int min;
	private int max;

	private CargoWeightCode(String description, int min, int max) {
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

}
