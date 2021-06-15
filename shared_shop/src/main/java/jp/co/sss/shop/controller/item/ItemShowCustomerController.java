package jp.co.sss.shop.controller.item;

import java.util.Date;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.util.BeanCopy;
import jp.co.sss.shop.util.Constant;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class ItemShowCustomerController {
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;

	@Autowired
	OrderItemRepository orderitemRepository;

	EntityManager entityManager;


	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @param pageable ページング情報
	 * @return "/" トップ画面へ
	 */
	@RequestMapping(path = "/")
	public String index(Model model,  Pageable pageable) {
		model.addAttribute("items", itemRepository.findAll());
		return "index";
	}
	/*メニューバーの処理*/
	/*トップ画面→商品一覧への遷移*/
	  @RequestMapping(path = "/item/list/1")
	  public String item_list(Model model,  Pageable pageable) {
		  //全件表示
		  model.addAttribute("items", itemRepository.findAll());
		  return "/item/list/item_list";
	  }
	  /*サイドバーの処理*/
	  /*カテゴリ別検索*/
	  @RequestMapping("/item/list/category/1")
	  public String item_listDropdown(Integer categoryId,Model model, Pageable pageable) {
		  Category category = new Category();
		  category.setId(categoryId);
		  model.addAttribute("items", itemRepository.findByCategory(category));
		  model.addAttribute("categoryId",categoryId);
		  return "/item/list/item_list";
	  }



	  /*テーブルの処理*/
	  /*新着順に並び替え*/
	  @RequestMapping(path = "/item/list/neworder")
	   public String findByDeleteFlagOrderByInsertDateDesc(Model model, Pageable pageable) {
		  model.addAttribute("items", itemRepository.findAllByOrderByInsertDateDesc(pageable));
		  	return "/item/list/item_list";
		}


	  /*売れ筋順に並びえ*/
	  @RequestMapping(path = "/item/list/2")
	   public String showItemOrderBySale(Model model, Pageable pageable) {
		  model.addAttribute("items", itemRepository.findAllByOrderByQuantityDesc());
			return "/item/list/item_list";
		}

	  /*商品詳細検索*/
		@RequestMapping(path = "/item/detail/{id}")
		public String showItem(@PathVariable int id, Model model) {
			// 商品IDに該当する商品情報を取得
			Item item = itemRepository.findById(id).orElse(null);

			ItemBean itemBean = new ItemBean();

			// Itemエンティティの各フィールドの値をItemBeanにコピー
			BeanUtils.copyProperties(item, itemBean);

			// 商品情報にカテゴリ名を設定
			itemBean.setCategoryName(item.getCategory().getName());

			// 商品情報をViewへ渡す
			model.addAttribute("item", itemBean);

			return "item/detail/item_detail";
		}

}





