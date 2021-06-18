package jp.co.sss.shop.repository;

import java.util.List;

import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.OrderItem;

/**
 * itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
@PersistenceContext

public interface ItemRepository extends JpaRepository<Item, Integer> {


	// 商品情報を新着順で検索
	public Page<Item> findByDeleteFlagOrderByInsertDateDesc(int deleteFlag, Pageable pageable);
	Page<Item> findAllByOrderByInsertDateDesc(Pageable pageable);



	//商品情報を売れ筋順で検索　
	//OrderItemのQuantityを昇順で並べる→Itemテーブルに反映させる処理
	//SELECT Quantity FROM order_items
	  @Query("SELECT i FROM Item i JOIN OrderItem oi ON i.id = oi.item ORDER BY oi.quantity DESC")//INNERを省略をしてる
	  public Page<Item>findAllByOrderByQuantityDesc(Pageable pageable);

	//カテゴリ別検索
	Page<Item> findByCategoryOrderByInsertDateDesc(Category category, Pageable pageable);

	//価格帯別検索
	//新着順
	public Page<Item> findByPriceBetweenOrderByInsertDateDesc(Integer min, Integer max, Pageable pageable);

	//売れ筋順
	 @Query("SELECT i FROM Item i JOIN OrderItem oi ON i.id = oi.item WHERE i.price BETWEEN :price_min AND :price_max ORDER BY oi.quantity DESC")//INNERを省略をしてる
	  public Page<Item>findBetweenByOrderByQuantityDesc(@Param("price_min") Integer min,@Param("price_max")Integer max,Pageable pageable);


	@Query("SELECT i FROM Item i WHERE (i.price = :price_max AND :price_max <= i.price) OR i.price <= :price_min")
	public Page<Item> findAllByOrderByPriceAsc(@Param("price_max") int price_max , @Param("price_min") int price_min,Pageable pageable);
	//Page<Item> findAllByOrderByPriceAsc(@Param("price_max") int price_max , @Param("price_min") int price_min, Pageable pageable);
}
