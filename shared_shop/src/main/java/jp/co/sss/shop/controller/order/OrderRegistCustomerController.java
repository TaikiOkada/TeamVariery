package jp.co.sss.shop.controller.order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import jp.co.sss.shop.util.PriceCalc;

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

	static final int canCanselTime = 5;

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

		// エラーメッセージ用リスト
		List<ItemBean> stockErrList = new ArrayList<ItemBean>();

		// 商品情報の生成
		ItemBean itemBean = new ItemBean();
		// 在庫数取得用
		Item item = new Item();

		// 買い物かごリストの中身を注文リストにコピーする
		for (BasketBean basketBean : basketBeanList) {
			itemBean = BeanCopy.copyEntityToBean(itemRepository.getOne(basketBean.getId()));
			item = itemRepository.getOne(basketBean.getId());

			orderItemBean = new OrderItemBean();

			orderItemBean.setId(basketBean.getId());				// ID
			orderItemBean.setName(basketBean.getName());			// 名前
			orderItemBean.setOrderNum(basketBean.getOrderNum());	// 注文数
			orderItemBean.setPrice(itemBean.getPrice());			// 値段
			orderItemBean.setImage(itemBean.getImage());			// 画像
			orderItemBean.setSubtotal(itemBean.getPrice() * basketBean.getOrderNum());	// 小計

			// 在庫数が足りない場合
			if (item.getStock() < orderItemBean.getOrderNum()) {
				orderItemBean.setOrderNum(item.getStock());	// 注文数を在庫数までに置き換える

				stockErrList.add(itemBean);
			}

			// リストに追加 ここに在庫数チェック
			if (orderItemBean.getOrderNum() > 0) {
				orderItemBeanList.add(orderItemBean);
			}
		}
		// エラーメッセージ用の情報を保存
		model.addAttribute("itemStocks",stockErrList);

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
		Item item = new Item();

		// 買い物かごの中身をリストに代入
		@SuppressWarnings("unchecked")
		List<BasketBean> basketBeanList = ((List<BasketBean>) session.getAttribute("baskets"));

		BeanUtils.copyProperties(orderItemBean, orderItem);
		// 買い物かごの中身数分 回す
		for (BasketBean basketBean : basketBeanList) {
			item = new Item();
			item = itemRepository.getOne(basketBean.getId());

			orderItem = new OrderItem();
			orderItem.setQuantity(basketBean.getOrderNum());
			orderItem.setOrder(order);
			orderItem.setPrice(item.getPrice());
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

	/**
	* 注文キャンセル確認画面表示
	*
	* @return order/regist/order_cancel 注文キャンセル確認画面
	*/
	@RequestMapping(path = "/order/cancel", method = RequestMethod.POST)
	public String cancelOrder(@ModelAttribute OrderForm orderForm, Model model) {
		Order order = orderRepository.findById(orderForm.getId()).orElse(null);

		// 表示する注文情報を生成
		OrderBean orderBean = new OrderBean();
		BeanUtils.copyProperties(order, orderBean);
		orderBean.setInsertDate(order.getInsertDate().toString());

		// 会員名を注文情報に設定
		orderBean.setUserName(order.getUser().getName());

		// 注文商品情報を取得
		List<OrderItemBean> orderItemBeanList = new ArrayList<OrderItemBean>();
		for (OrderItem orderItem : order.getOrderItemsList()) {
			OrderItemBean orderItemBean = new OrderItemBean();

			orderItemBean.setName(orderItem.getItem().getName());
			orderItemBean.setPrice(orderItem.getPrice());
			orderItemBean.setOrderNum(orderItem.getQuantity());

			//購入時単価の合計値を計算
			//※OrderItemのItemフィールドからgetPriceを利用すると、購入時ではなく現在の単価になってしまう。
			int subtotal = orderItem.getPrice() * orderItem.getQuantity();

			orderItemBean.setSubtotal(subtotal);

			orderItemBeanList.add(orderItemBean);
		}

		// 合計金額を算出
		int total = PriceCalc.orderItemPriceTotal(orderItemBeanList);
		// 合計金額を算出(送料込み)
		int feeTotal = total + orderBean.getPrefectureId().getRegionId().getFee();

		// 注文情報をViewへ渡す
		model.addAttribute("order", orderBean);
		model.addAttribute("orderItemBeans", orderItemBeanList);
		model.addAttribute("total", total);
		model.addAttribute("feeTotal", feeTotal);

		model.addAttribute("order", orderBean);

		return "order/regist/order_cancel";
	}

	/**
	* 注文キャンセル完了画面表示
	*
	* @return order/regist/order_cancel_complete 注文完了画面
	*/
	@RequestMapping(path = "/order/cansel/complete", method = RequestMethod.POST)
	public String cancelComplete(@ModelAttribute OrderForm orderForm, Model model) {

		Order order = orderRepository.findById(orderForm.getId()).orElse(null);

        // 現在の時間取得
		Date nowTime = new Date();
		// 登録時の時間取得
        Date time = order.getInsertDate();
		// Date型の値を設定しなおすためのオブジェクト
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);

        calendar.add(Calendar.MINUTE, canCanselTime);
        Date canselTime = calendar.getTime();

		// キャンセル時間内の場合
		if (canselTime.compareTo(nowTime) == 1) {

			// 注文商品テーブル削除
			List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
			for (OrderItem orderItem : orderItems) {
				// 在庫を元に戻す
				Item item = new Item();
				item = itemRepository.getOne(orderItem.getItem().getId());
				item.setStock(item.getStock() + orderItem.getQuantity());

				orderItemRepository.deleteById(orderItem.getId());
			}

			// 注文テーブル削除
			orderRepository.deleteById(orderForm.getId());
		} else {
			model.addAttribute("canselFlg", 1);
		}

		return "order/regist/order_cancel_complete";
	}
}
