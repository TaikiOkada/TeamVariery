package jp.co.sss.shop.bean;


public class PrefectureBean {

	/**
	 * ID
	 */

		private Integer id;

//	地域ID
		private Integer regionId;

//	都道府県名

	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
