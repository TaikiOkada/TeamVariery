package jp.co.sss.shop.controller.order;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class DeliveryFeeController {

	@RequestMapping(path="/confirm" ,method =RequestMethod.GET )
	public String showDeliveryFee() {
		System.out.println("あああ");

		return "/Comfirm";
	}

}
