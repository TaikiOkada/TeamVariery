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
	//DropDownList作成時の検索機能
	//@Query("SELECT i FROM Item i WHERE i.categories.Id = :category_Id")
	//List<Item> findByCategoty_Id(@Param("category_Id")Integer category_Id);
	//Page<Item> findByItem(Item item, Pageable pageable);
}
