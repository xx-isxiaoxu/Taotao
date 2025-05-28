package taotaomall.dao;

import taotaomall.model.OrderItem;
import java.util.List;

public interface OrderItemDao {
    // 批量插入订单明细
    int batchInsertOrderItems(List<OrderItem> orderItems);

    // 查询某订单的所有明细
    List<OrderItem> getOrderItemsByOrderId(Long orderId);

    // 查询某商品的所有明细（可选）
    List<OrderItem> getOrderItemsByGoodsId(Long goodsId);

    // 删除某订单的所有明细
    int deleteOrderItemsByOrderId(Long orderId);

    // 删除单个明细
    int deleteOrderItem(Long id);
}
