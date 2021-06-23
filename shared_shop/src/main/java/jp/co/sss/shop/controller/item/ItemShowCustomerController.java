package jp.co.sss.shop.controller.item;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.form.ItemForm;
import jp.co.sss.shop.form.PriceForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
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
    int price_max = 0;
    int price_min = 0;
    int priceFlag = 0;// 1のとき→価格帯別検索
    int categoryFlag = 0;// １のとき→カテゴリ検索
    Integer categorySave = 0;// カテゴリー検索後のデータを保持
    /**
     * トップ画面 表示処理
     *
     * @param model    Viewとの値受渡し
     * @param pageable ページング情報
     * @return "/" トップ画面へ
     */
    @RequestMapping(path = "/")
    public String index(Model model, Pageable pageable) {
        model.addAttribute("items", itemRepository.findAll());
        return "index";
    }
    /* メニューバーの処理 */
    /* 新着一覧への遷移 *//* 新着順に並び替え */
    @RequestMapping(path = "/item/list/1")
    public String item_list(Model model, Pageable pageable, @ModelAttribute ItemForm form) {
        Page<Item> ItemPageList = itemRepository.findAllByOrderByInsertDateDesc(pageable);
        List<Item> itemList = ItemPageList.getContent();
        // 商品情報をViewへ渡す
        model.addAttribute("flag", 0);
        model.addAttribute("pages", ItemPageList);
        model.addAttribute("items", itemList);
        // model.addAttribute("url", "item/list");
        return "/item/list/item_list";
    }
    /* サイドバーの処理 */
    /* カテゴリ別検索 */
    @RequestMapping("/item/list/category")
    public String item_listDropdown(Integer categoryId, Model model, Pageable pageable) {
        Category category = new Category();
        category.setId(categoryId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("flag", 0);
        categoryFlag = 1;
        // ページング
        categorySave = categoryId;
        Page<Item> ItemPageList = itemRepository.findByCategoryOrderByInsertDateDesc(category, pageable);
        List<Item> itemList = ItemPageList.getContent();
        model.addAttribute("pages", ItemPageList);
        model.addAttribute("items", itemList);
        model.addAttribute("url", "/item/list/category");
        return "/item/list/item_list";
    }
    /* 価格帯別検索 トップ画面・新着順のとき*/
    @RequestMapping(path = "/item/list/price/1")
    public String price_search(@Valid @ModelAttribute PriceForm form, BindingResult result, HttpSession session,
            Model model, Pageable pageable) {
        // 入力エラー発生時
        if (result.hasErrors()) {
            return "item/list/item_list";
        }
        price_max = form.getMax();
        price_min = form.getMin();
        priceFlag = 1;
        model.addAttribute("max", form.getMax());
        model.addAttribute("min", form.getMin());
        session.setAttribute("min", form.getMin());
        session.setAttribute("max", form.getMax());
        // ページング
        Page<Item> ItemPageList = itemRepository.findByPriceBetweenOrderByInsertDateDesc(form.getMin(), form.getMax(),
                pageable);
        List<Item> itemList = ItemPageList.getContent();
        model.addAttribute("pages", ItemPageList);
        model.addAttribute("items", itemList);
        model.addAttribute("flag", 0);
        return "/item/list/item_list";
    }
    //価格帯別検索 売れ筋順のとき
    @RequestMapping(path = "/item/list/price/{SortType}")
    public String price_searchOrderBySale
    (@PathVariable int SortType ,@Valid @ModelAttribute PriceForm form, BindingResult result, HttpSession session,
            Model model, Pageable pageable) {
        // 入力エラー発生時
        if (result.hasErrors()) {
            return "/item/list/item_list";
        }
        price_max = form.getMax();
        price_min = form.getMin();
        priceFlag = 1;
        model.addAttribute("max", form.getMax());
        model.addAttribute("min", form.getMin());
        session.setAttribute("min", form.getMin());
        session.setAttribute("max", form.getMax());
        // ページング
        Page<Item> ItemPageList = itemRepository.findBetweenByOrderByQuantityDesc(form.getMin(), form.getMax(),
                pageable);
        List<Item> itemList = ItemPageList.getContent();
        model.addAttribute("pages", ItemPageList);
        model.addAttribute("items", itemList);
        model.addAttribute("SortType",2);
        return "/item/list/item_list";
    }
    /* テーブルの処理 */
    /* 売れ筋順に並びかえ */
    @RequestMapping(path = "/item/list/2")
    public String showItemOrderBySale(@Valid @ModelAttribute PriceForm form, HttpSession session, Model model,
            Pageable pageable) {
        if (categoryFlag == 1) {// カテゴリ検索
            model.addAttribute("flag", 0);
            categoryFlag = 0;
            // ページング
            Page<Item> ItemPageList = itemRepository.findByCategoryOrderByQuantityDesc(categorySave, pageable);
            List<Item> itemList = ItemPageList.getContent();
            model.addAttribute("pages", ItemPageList);
            model.addAttribute("items", itemList);
            model.addAttribute("flag",1);
        } else if (priceFlag == 1) {// 価格帯別検索
            model.addAttribute("max", price_max);
            model.addAttribute("min", price_min);
            session.setAttribute("min", form.getMin());
            session.setAttribute("max", form.getMax());
            priceFlag = 0;
            // ページング
            Page<Item> ItemPageList = itemRepository.findBetweenByOrderByQuantityDesc(price_min, price_max, pageable);
            List<Item> itemList = ItemPageList.getContent();
            model.addAttribute("pages", ItemPageList);
            model.addAttribute("items", itemList);
            model.addAttribute("flag",1);
        }else {//通常検索
        	model.addAttribute("items", itemRepository.findByQuantityDescQuery(pageable));
			model.addAttribute("flag",1);
			//ページング
			Page<Item>ItemPageList = itemRepository.findByQuantityDescQuery(pageable);
			List<Item> itemList = ItemPageList.getContent(); model.addAttribute("pages",
			ItemPageList); model.addAttribute("items", itemList);
        }
        return "/item/list/item_list";
    }
    /* 商品詳細検索 */
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
