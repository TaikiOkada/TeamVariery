package jp.co.sss.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "prefecture")
public class Prefecture {

	/**
	 * ID
	 */

	@Id
	private Integer id;

//	地域ID
	@ManyToOne
	@JoinColumn(name = "region_id", referencedColumnName = "id")
	private Fee regionId;

//	都道府県名

	@Column
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Fee getRegionId() {
		return regionId;
	}

	public void setRegionId(Fee regionId) {
		this.regionId = regionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
