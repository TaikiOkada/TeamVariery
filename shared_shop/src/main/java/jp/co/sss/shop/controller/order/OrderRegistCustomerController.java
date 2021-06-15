package jp.co.sss.shop.controller.order;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.form.OrderShowForm;
import jp.co.sss.shop.repository.OrderRepository;

@Controller
public class OrderRegistCustomerController {
	@Autowired
	OrderRepository orderRepository;
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
	public String checkOrder(@ModelAttribute OrderShowForm form) {
		//登録情報の生成
		Item items = new Item();
		//入力値を登録情報にコピーする
		BeanUtils.copyProperties(form, items);
		//登録情報を保存
		//orderRepository.save(items);
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
