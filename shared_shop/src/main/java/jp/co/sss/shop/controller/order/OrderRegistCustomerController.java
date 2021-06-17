package jp.co.sss.shop.controller.order;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.form.OrderShowForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.util.BeanCopy;

@Controller
public class OrderRegistCustomerController {
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	ItemRepository itemRepository;
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
	public String checkOrder(@ModelAttribute OrderShowForm form ,Integer stock, Model model, HttpSession session) {
		// 合計値段
		int totalNum = 0;
		int subTotalNum = 0;

		// 表示用リスト
		List<OrderItemBean> orderItemBeanList = new ArrayList<OrderItemBean>();
		OrderItemBean orderItemBean = new OrderItemBean();

		// 買い物かごの中身をリストに代入
		@SuppressWarnings("unchecked")
		List<BasketBean> basketBeanList = ((List<BasketBean>) session.getAttribute("baskets"));

		// 商品情報の生成
		ItemBean itemBean = new ItemBean();
		//入力値を登録情報にコピーする
//		BeanUtils.copyProperties(form, items);

		// 買い物かごリストの中身を注文リストにコピーする
		for (BasketBean basketBean : basketBeanList) {
			itemBean = BeanCopy.copyEntityToBean(itemRepository.getOne(basketBean.getId()));
			orderItemBean = new OrderItemBean();

			orderItemBean.setId(basketBean.getId());				// ID
			orderItemBean.setName(basketBean.getName());			// 名前
			orderItemBean.setOrderNum(basketBean.getOrderNum());	// 注文数
			orderItemBean.setPrice(itemBean.getPrice());			// 値段
			orderItemBean.setImage(itemBean.getImage());			// 画像
			orderItemBean.setSubtotal(itemBean.getPrice() * basketBean.getOrderNum());	// 小計

			// リストに追加 ここに在庫数チェック？
			orderItemBeanList.add(orderItemBean);
		}

		// 注文リストの商品値段の合計
		for (OrderItemBean bean : orderItemBeanList) {
			subTotalNum += bean.getSubtotal();
		}
		totalNum = subTotalNum;

		// 注文リストをリクエストスコープに追加
		model.addAttribute("orderItems", orderItemBeanList);

		// 合計値段をリクエストスコープに追加
		model.addAttribute("subTotalNum", subTotalNum);		// 商品合計
		model.addAttribute("totalNum", totalNum);			// 合計

		return "order/regist/order_check";
	}

	/**
	* 注文完了画面表示
	*
	* @return order/regist/order_complete 注文完了画面
	*/
	@RequestMapping(path = "/order/complete", method = RequestMethod.POST)
	public String completeOrder() {
		//登録情報を保存
		//orderRepository.save(items);

		return "order/regist/order_complete";
	}
}
