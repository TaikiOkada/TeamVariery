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

	@ManyToOne
	@JoinColumn(name = "region_id", referencedColumnName = "id")
	private Fee regionId;

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

	public Fee setRegionId(Fee regionId) {
		return this.regionId = regionId;
	}


}
