package jp.co.sss.shop.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;

/**
 * order_itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

	List<OrderItem> findByOrder(Order order);
}
