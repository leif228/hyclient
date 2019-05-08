package com.eyunda.third.domain.enumeric;

public enum EvalTypeCode {
	verysatisfied("非常满意"),
	satisfied("满意"),
	ok("一般"),
	dissatisfied("不满意"),
	verydissatisfied("极不满意");

	private String description;

	private EvalTypeCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	public static EvalTypeCode valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }
}
