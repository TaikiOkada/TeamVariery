package jp.co.sss.shop.controller.basket;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class BasketCustomerController {

	@RequestMapping(path = "/basket/list", method = RequestMethod.GET)
	public String showBasket() {
		System.out.println("aaa");
		return "basket/shopping_basket";
	}
}
