package jp.co.sss.shop.controller.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.repository.ItemRepository;

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


		return "index";
	}

	/*トップ画面→新着順への遷移*/
	  @RequestMapping(path = "/item/list/{sortType}")
	  public String index1(Model model, Pageable pageable) {

	  return "/item/list";
	  }
	  /*ドロップダウンリスト*/
	  @RequestMapping("/findAllDropDown")
	  public String item_listDropdown(int deleteFlag, Pageable pageable) {
		  return "redirect:/";
	  }


	/*商品詳細検索*/
	  @RequestMapping(path = "/item/detail/{id}")
	  public String itemdetail(Model model, Pageable pageable) {
		  System.out.println("3");
	  return "/item/detail/item_detail_Stock5over";
	  }

	@RequestMapping(path = "item/list/1")
	public String item_list(){
		System.out.println("a");
		return "/item_list";
	}

}





