package com.eyunda.third.domain.enumeric;

import java.util.ArrayList;
import java.util.List;

public enum CargoTypeCode {
	container20e("集装箱.20’E", CargoBigTypeCode.container),

	container20f("集装箱.20’F", CargoBigTypeCode.container),

	container40e("集装箱.40’E", CargoBigTypeCode.container),

	container40f("集装箱.40’F", CargoBigTypeCode.container),

	container45e("集装箱.45’E", CargoBigTypeCode.container),

	container45f("集装箱.45’F", CargoBigTypeCode.container),

	coal("干散货.煤炭", CargoBigTypeCode.bulk),

	metalmineral("干散货.矿石", CargoBigTypeCode.bulk),

	steel("干散货.钢铁", CargoBigTypeCode.bulk),

	metal("干散货.有色金属", CargoBigTypeCode.bulk),

	tile("干散货.建材", CargoBigTypeCode.bulk),

	wood("干散货.木材", CargoBigTypeCode.bulk),

	cement("件杂货.水泥", CargoBigTypeCode.bulk),

	manure("件杂货.化肥农药", CargoBigTypeCode.bulk),

	salt("件杂货.盐", CargoBigTypeCode.bulk),

	food("件杂货.粮食", CargoBigTypeCode.bulk),

	machinery("件杂货.机械电器", CargoBigTypeCode.bulk),

	chemicals("件杂货.化工", CargoBigTypeCode.bulk),

	industry("件杂货.轻工医药", CargoBigTypeCode.bulk),

	industrial("件杂货.日用品", CargoBigTypeCode.bulk),

	farming("件杂货.农林牧渔", CargoBigTypeCode.bulk),

	cotton("件杂货.棉花", CargoBigTypeCode.bulk),

	explosives("危险品.爆炸品", CargoBigTypeCode.danger),

	liquefied("危险品.液化气体", CargoBigTypeCode.danger),

	flammable("危险品.易燃液体", CargoBigTypeCode.danger),

	flammablesolids("危险品.易燃固体", CargoBigTypeCode.danger),

	oxidizing("危险品.氧化剂", CargoBigTypeCode.danger),

	poisons("危险品.毒害品", CargoBigTypeCode.danger),

	radioactive("危险品.放射性物品", CargoBigTypeCode.danger),

	corrosives("危险品.腐蚀品", CargoBigTypeCode.danger),

	miscellaneous("危险品.杂类", CargoBigTypeCode.danger);

	private String description;
	private CargoBigTypeCode cargoBigType;

	public static List<CargoTypeCode> getCargoTypeCodes(CargoBigTypeCode cargoBigType) {
		List<CargoTypeCode> results = new ArrayList<CargoTypeCode>();
		for (CargoTypeCode e : CargoTypeCode.values()) {
			if (e.getCargoBigType() == cargoBigType) {
				results.add(e);
			}
		}
		return results;
	}
	public static List<CargoTypeCode> getCargoTypeCodes() {
		List<CargoTypeCode> results = new ArrayList<CargoTypeCode>();
		for (CargoTypeCode e : CargoTypeCode.values()) {
			if (e != container20f && e != container40e && e != container40f && e != container45e && e != container45f) {
				results.add(e);
			}
		}
		return results;
	}
	// 通过类型内容获得枚举对象
	public static CargoTypeCode getByDescription(String description) {

		for (CargoTypeCode e : CargoTypeCode.values()) {
			if (e.getDescription() == description) {
				return e;
			}
		}

		return null;
	}

	public String getDescription() {
		return description;
	}

	public String getShortDesc() {
		return description.substring(description.indexOf(".") + 1);
	}

	public CargoBigTypeCode getCargoBigType() {
		return cargoBigType;
	}

	private CargoTypeCode(String description, CargoBigTypeCode cargoBigType) {
		this.description = description;
		this.cargoBigType = cargoBigType;
	}
}
