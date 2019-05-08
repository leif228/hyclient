package com.eyunda.third.domain.enumeric;

public enum WrapStyleCode {
	box("箱"), pocket("袋"), reel("卷"), bundle("捆"), pail("桶"), individual("个"), scatter("批");

	private String description;

	private WrapStyleCode(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public static void main(String[] args) {
		WrapStyleCode wrap = WrapStyleCode.pail;

		System.out.println(wrap);
		System.out.println(wrap.ordinal()); // 用于枚举 转int

		int i = wrap.compareTo(WrapStyleCode.reel); // pail和 reel的顺序之差

		System.out.println("pail和 other的顺序之差:" + i);

		wrap = Enum.valueOf(WrapStyleCode.class, "bundle"); // String 转枚举

		System.out.println(wrap);
		System.out.println(wrap.ordinal());

		// 遍历枚举
		for (WrapStyleCode c : WrapStyleCode.values()) {
			System.out.println(c);
		}
	}

}
