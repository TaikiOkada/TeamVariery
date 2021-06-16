package jp.co.sss.shop.controller.item;

import java.util.Date;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.form.ItemForm;
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
	@Autowired
	HttpSession session;


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
	/*トップ画面→新着一覧への遷移*//*新着順に並び替え*/
	  @RequestMapping(path = "/item/list/1")
	  public String item_list( Model model,  Pageable pageable, ItemForm form) {
		  System.out.println(9);
		  //全件表示
		  model.addAttribute("items", itemRepository.findAllByOrderByInsertDateDesc(pageable));
		  model.addAttribute("flag",0);
		  //ページング
		  //ItemBean itemBean = ((ItemBean)session.getAttribute("items"));
		  Page<Item> ItemPageList = itemRepository.findAllByOrderByInsertDateDesc(pageable);

		  // エンティティ内の検索結果をJavaBeansにコピー
		  //List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean((ItemPageList).getContent());
			// 商品情報をViewへ渡す
			model.addAttribute("pages", ItemPageList);
			//model.addAttribute("items", itemBeanList);
			System.out.println(4);
		  return "/item/list/item_list";
	  }
	  /*サイドバーの処理*/
	  /*カテゴリ別検索*/
	  @RequestMapping("/item/list/category/{sortType}")
	  public String item_listDropdown(Integer categoryId,Model model, Pageable pageable) {
		  Category category = new Category();
		  category.setId(categoryId);
		  model.addAttribute("items", itemRepository.findByCategoryOrderByInsertDateDesc(category, pageable));
		  model.addAttribute("categoryId",categoryId);
		  model.addAttribute("flag",0);
		  //ページング
		  //ItemBean itemBean = ((ItemBean)session.getAttribute("items"));
		  Page<Item> ItemPageList = itemRepository.findAllByOrderByInsertDateDesc(pageable);

		  // エンティティ内の検索結果をJavaBeansにコピー
		  //List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean((ItemPageList).getContent());
			// 商品情報をViewへ渡す
			model.addAttribute("pages", ItemPageList);
			//model.addAttribute("items", itemBeanList);
		  return "/item/list/item_list";
	  }

	  /*価格帯別検索*/
	  @PostMapping("item/list/price/{sortType}?min={下限金額}&max={上限金額}")
	  public String price_search(Model model,Pageable pageable){
		  model.addAttribute("items", itemRepository.findAllByOrderByPriceAsc(pageable));
		  return "/item/list/item_list";
	  }

	  /*テーブルの処理*/
	  /*売れ筋順に並びかえ*/
	  @RequestMapping(path = "/item/list/2")
	   public String showItemOrderBySale(Model model, Pageable pageable) {
		  model.addAttribute("items", itemRepository.findAllByOrderByQuantityDesc());
		  model.addAttribute("flag",1);

		  //ページング
		  //ItemBean itemBean = ((ItemBean)session.getAttribute("items"));
		  Page<Item> ItemPageList = itemRepository.findAllByOrderByInsertDateDesc(pageable);

		  // エンティティ内の検索結果をJavaBeansにコピー
		  //List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean((ItemPageList).getContent());
			// 商品情報をViewへ渡す
			model.addAttribute("pages", ItemPageList);
			//model.addAttribute("items", itemBeanList);
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





