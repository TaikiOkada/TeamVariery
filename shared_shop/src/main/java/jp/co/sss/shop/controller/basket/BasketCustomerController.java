package jp.co.sss.shop.controller.basket;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.repository.ItemRepository;

@Controller
public class BasketCustomerController {

	HttpSession session;

	ItemRepository itemRepository;

	/**
	 * 買い物かごに追加
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/add", method = RequestMethod.POST)
	public String addItem() {
		// 買い物かごがまだ用意されていない場合
		if (session.getAttribute("basket") == null) {
			session.setAttribute("basket", itemRepository.getOne(1));
		}
		System.out.println("買い物add");
		return basketList();
	}

	/**
	 * 対象商品を買い物かごから削除
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/delete", method = RequestMethod.POST)
	public String deleteItem() {
		System.out.println("買い物delete");
		return basketList();
	}

	/**
	 * 全商品を買い物かごから削除
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/allDelete", method = RequestMethod.POST)
	public String allDelete() {
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

}
