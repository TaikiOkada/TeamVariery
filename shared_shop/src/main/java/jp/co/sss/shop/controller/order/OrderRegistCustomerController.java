package jp.co.sss.shop.controller.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class OrderRegistCustomerController {

	/**
	 * 届け先入力画面表示
	 *
	 * @return order_address_input 届け先入力画面
	 */
	@RequestMapping(path = "/address/input", method = RequestMethod.POST)
	public String inputAddress() {

		return "order/regist/order_address_input";
	}

	/**
	 * 支払方法画面表示
	 *
	 * @return order_Payment_input 支払方法画面
	 */
	@RequestMapping(path = "/payment/input", method = RequestMethod.POST)
	public String inputPayment() {

		return "order/regist/order_payment_input";
	}

	/**
	* 注文確認画面表示
	*
	* @return order/regist/order_check 画面
	*/
	@RequestMapping(path = "/order/check", method = RequestMethod.POST)
	public String checkOrder() {

		return "order/regist/order_check";
	}

	/**
	* 注文完了画面表示
	*
	* @return order/regist/order_complete 注文完了画面
	*/
	@RequestMapping(path = "/order/complete", method = RequestMethod.POST)
	public String completeOrder() {

		return "order/regist/order_complete";
	}
}
