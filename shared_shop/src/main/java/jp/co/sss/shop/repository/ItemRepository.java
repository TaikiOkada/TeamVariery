package jp.co.sss.shop.repository;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
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
     @Query     ("SELECT new Item(i.id, i.name, i.price, i.description, i.image, c.name) "
                + "FROM Item i INNER JOIN i.category c INNER JOIN i.orderItemList oi "
                + "WHERE i.deleteFlag = 0 GROUP BY i.id, i.name, i.price,i.description, i.image, c.name "
                + "ORDER BY SUM(oi.quantity) DESC,i.id ASC")
    public Page<Item> findByQuantityDescQuery(Pageable pageable);

    //カテゴリ別検索(新着順)
    Page<Item> findByCategoryOrderByInsertDateDesc(Category category, Pageable pageable);
    //売れ筋順に並び替え(カテゴリ検索後→売れ筋順に並び替え)
    //@Query("SELECT i FROM Item i JOIN OrderItem oi ON i.id = oi.item WHERE i.category.id = :category_Id ORDER BY oi.quantity DESC")
    @Query     ("SELECT new Item(i.id, i.name, i.price, i.description, i.image, c.name) "
            + "FROM Item i INNER JOIN i.category c INNER JOIN i.orderItemList oi "
            + "WHERE i.category.id = :category_Id"
            + " GROUP BY i.id, i.name, i.price,i.description, i.image, c.name "
            + "ORDER BY SUM(oi.quantity) DESC,i.id ASC")
    public Page<Item>findByCategoryOrderByQuantityDesc(@Param("category_Id")Integer categoryId,Pageable pageable);

    //価格帯別検索
    //新着順
    public Page<Item> findByPriceBetweenOrderByInsertDateDesc(Integer min, Integer max, Pageable pageable);
    //売れ筋順
    //@Query("SELECT i FROM Item i JOIN OrderItem oi ON i.id = oi.item WHERE i.price BETWEEN :price_min AND :price_max ORDER BY oi.quantity DESC")
    @Query     ("SELECT new Item(i.id, i.name, i.price, i.description, i.image, c.name) "
            + "FROM Item i INNER JOIN i.category c INNER JOIN i.orderItemList oi "
            + "WHERE i.price BETWEEN :price_min AND :price_max"
            + " GROUP BY i.id, i.name, i.price,i.description, i.image, c.name "
            + "ORDER BY SUM(oi.quantity) DESC,i.id ASC")
    public Page<Item>findBetweenByOrderByQuantityDesc(@Param("price_min") Integer min,@Param("price_max")Integer max,Pageable pageable);
    }