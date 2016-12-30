package com.widget.jcdialog.widget.pickerview.model;

import com.widget.jcdialog.utils.ToolUtils;
import com.widget.jcdialog.widget.itemDecotation.IIndexTargetInterface;
import com.widget.jcdialog.widget.itemDecotation.ITitleCategoryInterface;

import java.util.List;

public class CityModel implements IIndexTargetInterface,ITitleCategoryInterface {
	private String name;
	private String pinyin;
	private String tag;
	private List<DistrictModel> districtList;
	
	public CityModel() {
		super();
	}

	public CityModel(String name, List<DistrictModel> districtList) {
		super();
		this.name = name;
		this.districtList = districtList;
	}

	public CityModel(String name, String pinyin) {
		this.name = name;
		this.pinyin = pinyin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModel> districtList) {
		this.districtList = districtList;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "CityModel{" +
				"name='" + name + '\'' +
				", pinyin='" + pinyin + '\'' +
				", districtList=" + districtList +
				'}';
	}

	@Override
	public String getTitleCategory() {
		String fisrtChar = String.valueOf(ToolUtils.getFirstLetter((name.charAt(0)))).toUpperCase();
		if (!fisrtChar.matches("[A-Z]")){
			fisrtChar = "#";
		}
		return fisrtChar;
	}

	@Override
	public String getTarget() {
		return name;
	}
}
