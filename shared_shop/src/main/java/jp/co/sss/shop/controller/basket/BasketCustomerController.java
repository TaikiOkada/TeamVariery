/* ----------------------------------------------------------------------------------- *

                 BasketCustomerController : 買い物かごコントローラー

 * ----------------------------------------------------------------------------------- */


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

	/**
	 * 買い物かごに追加
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/add", method = RequestMethod.POST)
	public String addItem(@ModelAttribute BasketForm form,Model model) {
		int orderNum = 1;
		// 警告出さないようにしてるだけのアノテーション
		@SuppressWarnings("unchecked")
		List<BasketBean> basketBeanList = ((List<BasketBean>) session.getAttribute("baskets"));
		// フォームで追加された商品情報（BasketForm のゲッター使ってID取ってきて、商品情報をDBから取って来てる。）
		// その後でItemBean 型に成形してitemって変数で値を保持
		ItemBean item = BeanCopy.copyEntityToBean(itemRepository.getOne(form.getId()));

		// 買い物かごが空だった場合、新しくリストを作る。（セッションに詰める用）
		if (basketBeanList == null) {
			basketBeanList = new ArrayList<BasketBean>();
		} else {
			// 同じ商品がカゴに入っているか確認
			for (BasketBean bean: basketBeanList) {
				// 同じ商品を追加時
				if (bean.getId() == item.getId()) {
					// 在庫より注文数が少なければ
					if (bean.getOrderNum() < bean.getStock()) {
						// 数量追加
						int myIndex = basketBeanList.indexOf(bean);		// リストの要素を取得
						bean.setOrderNum(bean.getOrderNum() + 1);
						basketBeanList.set(myIndex, bean);
					} else {	// 在庫数を超えてしまう場合
						// Beanの情報を送る
						model.addAttribute("basketNum",bean);
					}

					return basketList();
				}
			}
		}
		// 買い物かごが空で在庫がない場合
		if (item.getStock() <= 0) {
			BasketBean bean = new BasketBean();
			bean.setName(item.getName());
			model.addAttribute("basketNum",bean);
			return basketList();
		}

		// 新しくリストに追加するためのBean設定
		BasketBean basketBean = new BasketBean();
		basketBean.setId(item.getId());
		basketBean.setName(item.getName());
		basketBean.setStock(item.getStock());
		basketBean.setOrderNum(orderNum);

		basketBeanList.add(basketBean);

		session.setAttribute("baskets", basketBeanList);

		return basketList();
	}

	/**
	 * 対象商品を買い物かごから削除
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/delete", method = RequestMethod.POST)
	public String deleteItem(@ModelAttribute BasketForm form,Model model) {
		// 警告出さないようにしてるだけのアノテーション
		@SuppressWarnings("unchecked")
		List<BasketBean> basketBeanList = ((List<BasketBean>) session.getAttribute("baskets"));
		// フォームで追加された商品情報（BasketForm のゲッター使ってID取ってきて、商品情報をDBから取って来てる。）
		// その後でItemBean 型に成形してitemって変数で値を保持
		ItemBean item = BeanCopy.copyEntityToBean(itemRepository.getOne(form.getId()));
		// 同じ商品がカゴに入っているか確認
		for (BasketBean bean: basketBeanList) {
			if (bean.getId() == item.getId()) {
				int myIndex = basketBeanList.indexOf(bean);		// リストの要素を取得
				// 数量確認 (複数個あるかどうか)
				if (bean.getOrderNum() >= 2) {
					bean.setOrderNum(bean.getOrderNum() - 1);
					basketBeanList.set(myIndex, bean);
				} else {	// 1つしかない場合
					basketBeanList.remove(myIndex);
					if (basketBeanList.isEmpty()){
						// セッション情報を消す
						session.removeAttribute("baskets");;
					}
				}
				return basketList();
			}
		}
		return basketList();
	}

	/**
	 * 全商品を買い物かごから削除
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/allDelete", method = RequestMethod.POST)
	public String allDelete() {
		// セッション情報を消す
		session.removeAttribute("baskets");;

		return basketList();
	}

	/**
	 * ナビゲーションバーから買い物かご画面表示
	 *
	 * @return basket/shopping_basket 買い物かご画面
	 */
	@RequestMapping(path = "/basket/list", method = RequestMethod.GET)
	public String basketListGet() {
		// 警告出さないようにしてるだけのアノテーション
		@SuppressWarnings("unchecked")
		List<BasketBean> basketBeanList = ((List<BasketBean>) session.getAttribute("baskets"));

		// 買い物かごの中身があった場合
		if (basketBeanList != null) {
			// 買い物かごの中身分回す
			for (BasketBean bean: basketBeanList) {
				// 買い物かごの商品の在庫を常に更新
				if (bean.getStock() != itemRepository.getOne(bean.getId()).getStock()) {
					bean.setStock(itemRepository.getOne(bean.getId()).getStock());
					int myIndex = basketBeanList.indexOf(bean);		// リストの要素を取得
					basketBeanList.set(myIndex, bean);					// リストの更新
				}
			}
		}

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
