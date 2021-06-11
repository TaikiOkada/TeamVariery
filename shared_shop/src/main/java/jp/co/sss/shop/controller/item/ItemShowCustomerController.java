package jp.co.sss.shop.controller.item;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
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
	/*トップ画面→新着一覧への遷移*/
	  @RequestMapping(path = "/item/list/1")
	  public String item_list(Model model,  Pageable pageable) {
		  //全件表示
		  model.addAttribute("items", itemRepository.findAll());

		  return "/item/list/item_list";
	  }
	  /*サイドバーの処理*/
	  /*ドロップダウンリスト*/
	  @RequestMapping("/findAllDropDown")
	  public String item_listDropdown(int deleteFlag, Pageable pageable) {
		  return "redirect:/";
	  }

	  /*テーブルの処理*/
	  /*新着順に並び替え*/
	  @RequestMapping(path = "/item/list/1?categoryId=&amp;min=&amp;max=")
	   public String showItem(Model model, Pageable pageable) {
		   System.out.println(32);
		  // 商品情報を全件検索(新着順)
			Page<Item> itemList = itemRepository.findByDeleteFlagOrderByInsertDateDesc(Constant.NOT_DELETED, pageable);

			// エンティティ内の検索結果をJavaBeansにコピー
			List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList.getContent());

			// 商品情報をViewへ渡す
			model.addAttribute("pages", itemList);
			model.addAttribute("items", itemBeanList);
			model.addAttribute("url", "/item/list/");

			System.out.println(22);
			return "redirect://item/list/item_list";
		}


	  /*売れ筋順に並び替え*/
	  @RequestMapping(path = "/item/list/2?categoryId=&amp;min=&amp;max=")
	   public String showItemOrderBySale(Model model, Pageable pageable) {
		   System.out.println(32);
		  // 商品情報を全件検索(売れ筋順)
			Page<Item> itemList = itemRepository.findByDeleteFlagOrderByInsertDateDesc(Constant.NOT_DELETED, pageable);

			// エンティティ内の検索結果をJavaBeansにコピー
			List<ItemBean> itemBeanList = BeanCopy.copyEntityToItemBean(itemList.getContent());

			// 商品情報をViewへ渡す
			model.addAttribute("pages", itemList);
			model.addAttribute("items", itemBeanList);
			model.addAttribute("url", "/item/list/");

			System.out.println(22);
			return "item/list/item_list";
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





