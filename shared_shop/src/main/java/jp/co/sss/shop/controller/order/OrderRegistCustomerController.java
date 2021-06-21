package jp.co.sss.shop.controller.order;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.bean.OrderBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.PrefectureRepository;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.BeanCopy;

@Controller
public class OrderRegistCustomerController {
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	OrderItemRepository orderItemRepository;
	@Autowired
	PrefectureRepository prefectureRepository;
	@Autowired
	HttpSession session;

	OrderBean orderBean = new OrderBean();
	OrderItemBean orderItemBean = new OrderItemBean();

	/**
	 * 届け先入力画面表示
	 *
	 * @return order_address_input 届け先入力画面
	 */
	@RequestMapping(path = "/address/input", method = RequestMethod.POST)
	public String inputAddress(Model model, @ModelAttribute OrderForm orderForm) {
		Integer id = ((UserBean) session.getAttribute("user")).getId();
		model.addAttribute("user", userRepository.getOne(id));

		model.addAttribute("prefectures", prefectureRepository.findAll());

		return "order/regist/order_address_input";
	}

	/**
	 * 支払方法画面表示
	 *
	 * @return order_Payment_input 支払方法画面
	 */
	@RequestMapping(path = "/payment/input", method = RequestMethod.POST)
	public String inputPayment(@Valid @ModelAttribute OrderForm orderForm, BindingResult result
			,Model model, boolean backflg) {
		// 入力内容にエラーがあった場合
		if (result.hasErrors()) {
			return inputAddress(model,orderForm);
		}

		// 確認画面から戻ってきていない場合
		if (backflg != true) {
			BeanUtils.copyProperties(orderForm, orderBean);
		}

		model.addAttribute("payMethod", orderForm.getPayMethod());

		return "order/regist/order_payment_input";
	}

	/**
	* 注文確認画面表示
	*
	* @return order/regist/order_check 画面
	*/
	@RequestMapping(path = "/order/check", method = RequestMethod.POST)
	public String checkOrder(@ModelAttribute OrderForm orderForm ,Integer stock, Model model, HttpSession session) {
		// 合計値段
		int totalNum = 0;
		int subTotalNum = 0;

		// 商品表示用リスト
		List<OrderItemBean> orderItemBeanList = new ArrayList<OrderItemBean>();

		// 買い物かごの中身をリストに代入
		@SuppressWarnings("unchecked")
		List<BasketBean> basketBeanList = ((List<BasketBean>) session.getAttribute("baskets"));

		// 商品情報の生成
		ItemBean itemBean = new ItemBean();

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
		totalNum = subTotalNum + orderBean.getPrefectureId().getRegionId().getFee();	// 送料込み合計

		//Beanへコピー
		orderBean.setPayMethod(orderForm.getPayMethod());
		model.addAttribute("orderBean",orderBean);

		// 支払方法場合分け
		switch(orderBean.getPayMethod()) {
			case 1:
				model.addAttribute("payMethod","クレジットカード");
			break;

			case 2:
				model.addAttribute("payMethod","銀行振り込み");
			break;

			case 3:
				model.addAttribute("payMethod","着払い");
			break;

			case 4:
				model.addAttribute("payMethod","電子マネー");
			break;

			case 5:
				model.addAttribute("payMethod","コンビニ決済");
			break;
		}

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
	public String completeOrder(@ModelAttribute OrderForm orderForm) {

		// 注文登録情報を保存
		Order order = new Order();
		BeanUtils.copyProperties(orderForm, order);
		order.setPrefectureId(orderBean.getPrefectureId());

		User user = new User();
		Integer id = ((UserBean) session.getAttribute("user")).getId();
		user=userRepository.getOne(id);
		order.setUser(user);

		orderRepository.save(order);

		// 注文商品テーブルへ情報を保存
		OrderItem orderItem = new OrderItem();
		ItemBean itemBean = new ItemBean();
		Item item = new Item();

		// 買い物かごの中身をリストに代入
		@SuppressWarnings("unchecked")
		List<BasketBean> basketBeanList = ((List<BasketBean>) session.getAttribute("baskets"));

		List<OrderItemBean> orderItemBeanList = new ArrayList<OrderItemBean>();
		BeanUtils.copyProperties(orderItemBean, orderItem);
		// 買い物かごの中身数分 回す
		for (BasketBean basketBean : basketBeanList) {
			item = new Item();
//			itemBean = BeanCopy.copyEntityToBean(itemRepository.getOne(basketBean.getId()));
			item = itemRepository.getOne(basketBean.getId());
//			BeanUtils.copyProperties(itemBean, item);
			BeanUtils.copyProperties(item, itemBean);
			orderItemBean = new OrderItemBean();

			orderItemBean.setId(basketBean.getId());				// ID
			orderItemBean.setName(basketBean.getName());			// 名前
			orderItemBean.setOrderNum(basketBean.getOrderNum());	// 注文数
			orderItemBean.setPrice(itemBean.getPrice());			// 値段
			orderItemBean.setImage(itemBean.getImage());			// 画像
			orderItemBean.setSubtotal(itemBean.getPrice() * basketBean.getOrderNum());	// 小計

			orderItem = new OrderItem();
			orderItem.setQuantity(orderItemBean.getOrderNum());
			orderItem.setOrder(order);
			orderItem.setPrice(orderItemBean.getPrice());
			orderItem.setItem(item);
			orderItemRepository.save(orderItem);

			// 在庫数減らす
			item.setStock(item.getStock() - orderItem.getQuantity());
			itemRepository.save(item);

			// 買い物かごの中身削除
			session.removeAttribute("baskets");
		}

		return "order/regist/order_complete";
	}
}
