package jp.co.sss.shop.form;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class PriceForm {

	@Digits(integer = 10, fraction = 1)
	@NotNull
	private Integer max = 0;


	@Digits(integer = 10, fraction = 1)
	@NotNull
	private Integer min = 0;

	public Integer getMax() {
		return max;
	}
	public void setMax(Integer price_max) {
		this.max = price_max;
	}
	public Integer getMin() {
		return min;
	}
	public void setMin(Integer price_min) {
		this.min = price_min;
	}
}
