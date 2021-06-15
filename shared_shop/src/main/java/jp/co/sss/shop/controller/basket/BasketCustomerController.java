package jp.co.sss.shop.controller.basket;

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
import jp.co.sss.shop.form.BasketForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.util.BeanCopy;

@Controller
public class BasketCustomerController {

	@Autowired
	HttpSession session;

	@Autowired
	ItemRepository itemRepository;

	/** 買い物かご */
	List<BasketBean> basketItems = new ArrayList<BasketBean>();

	/**
	 * 買い物かごに追加
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/add", method = RequestMethod.POST)
	public String addItem(@ModelAttribute BasketForm form,Model model) {
		int orderNum = 1;
		System.out.println("id = " + form.getId());
		System.out.println("メソッド開始");
		// 警告出さないようにしてるだけのアノテーション
		@SuppressWarnings("unchecked")
		List<BasketBean> basketBeanList = ((List<BasketBean>) session.getAttribute("baskets"));

		System.out.println("セッション情報をリストにコピー");
		// フォームで追加された商品情報（BasketForm のゲッター使ってID取ってきて、商品情報をDBから取って来てる。）
		// その後でItemBean 型に成形してitemって変数で値を保持
		ItemBean item = BeanCopy.copyEntityToBean(itemRepository.getOne(form.getId()));

		// 買い物かごが空だった場合、新しくリストを作る。（セッションに詰める用）
		if (basketBeanList == null) {
			basketBeanList = new ArrayList<BasketBean>();
			System.out.println("新規リスト");
		} else {
			// 同じ商品がカゴに入っているか確認
			for (BasketBean bean: basketBeanList) {
				if (bean.getId() == item.getId()) {
					orderNum += bean.getOrderNum();
					System.out.println("重複 = " + orderNum);

//					BasketBean basketBean = new BasketBean();
//					basketBean.setId(item.getId());
//					basketBean.setName(item.getName());
//					basketBean.setStock(item.getStock());
//					basketBean.setOrderNum(orderNum);
//
					return basketList();
				}
			}
		}

		BasketBean basketBean = new BasketBean();
		basketBean.setId(item.getId());
		basketBean.setName(item.getName());
		basketBean.setStock(item.getStock());
		basketBean.setOrderNum(orderNum);

		basketBeanList.add(basketBean);

		session.setAttribute("baskets", basketBeanList);
		System.out.println("メソッド終了");

		return basketList();
	}

	/**
	 * 対象商品を買い物かごから削除
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/delete", method = RequestMethod.POST)
	public String deleteItem(@ModelAttribute BasketForm form,Model model) {
//		basketItems.remove(basketItems.indexOf(id));
		ItemBean item = BeanCopy.copyEntityToBean(itemRepository.getOne(form.getId()));
		System.out.println(basketItems);
		sessionSetAttribute();
		return basketList();
	}

	/**
	 * 全商品を買い物かごから削除
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/allDelete", method = RequestMethod.POST)
	public String allDelete() {
		// セッションから削除
		basketItems.clear();
		sessionSetAttribute();
		return basketList();
	}

	/**
	 * ナビゲーションバーから買い物かご画面表示
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/list", method = RequestMethod.GET)
	public String basketListGet() {
//		// 買い物かごがまだ用意されていない場合
//		if (session.getAttribute("basket") == null) {
//			session.setAttribute("basket", itemRepository.getOne(1));
//		}
		return "basket/shopping_basket";
	}

	/**
	 * 買い物かご画面表示
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/list", method = RequestMethod.POST)
	public String basketList() {

		return "basket/shopping_basket";
	}

	/**
	 * セッションに保存
	 */
	public void sessionSetAttribute() {
		session.setAttribute("baskets", basketItems);
	}

}
