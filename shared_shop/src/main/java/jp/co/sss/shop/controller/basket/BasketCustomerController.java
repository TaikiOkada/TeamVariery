package jp.co.sss.shop.controller.basket;

import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BasketCustomerController {

	/**
	 * 買い物かごに追加
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/add", method = RequestMethod.POST)
	public String addItem(Session session) {
		System.out.println("買い物add");
		return basketList();
	}

	/**
	 * 対象商品を買い物かごから削除
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/delete", method = RequestMethod.POST)
	public String deleteItem(Session session) {
		System.out.println("買い物delete");
		return basketList();
	}

	/**
	 * 全商品を買い物かごから削除
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/allDelete", method = RequestMethod.POST)
	public String allDelete(Session session) {
		System.out.println("買い物allDelete");
		return basketList();
	}

	/**
	 * ナビゲーションバーから買い物かご画面表示
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/list", method = RequestMethod.GET)
	public String basketListGet() {

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

}
