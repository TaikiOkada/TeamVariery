package jp.co.sss.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "delivery_fee")
public class Fee {

	/**
	 * ID
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_delivery_fee_gen")
	@SequenceGenerator(name = "seq_delivery_fee_gen", sequenceName = "seq_delivery_fee", allocationSize = 1)

	private Integer id;

	@Column
	private String region;

	@Column
	private Integer fee;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRegion() {
		return region;
	}

	public String setRegion(String region) {
		return this.region = region;
	}

	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

}
