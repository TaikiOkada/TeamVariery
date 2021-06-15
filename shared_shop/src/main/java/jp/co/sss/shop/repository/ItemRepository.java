package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.OrderItem;

/**
 * itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {


	// 商品情報を新着順で検索
	Page<Item> findByDeleteFlagOrderByInsertDateDesc(int deleteFlag, Pageable pageable);
	List<Item> findAllByOrderByInsertDateDesc(Pageable pageable);

	//商品情報を売れ筋順で検索　
	//OrderItemのQuantityを昇順で並べる→Itemテーブルに反映させる処理
	//SELECT Quantity FROM order_items
	  @Query("SELECT i FROM Item i JOIN OrderItem oi ON i.id = oi.item ORDER BY oi.quantity DESC")//INNERを省略をしてる
	  public List<Item>findAllByOrderByQuantityDesc();

	//カテゴリ別検索
	List<Item> findByCategory(Category category);
}
