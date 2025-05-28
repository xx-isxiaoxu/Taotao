package taotaomall.service;

import taotaomall.model.OrderItem;
import java.util.List;

public interface OrderItemService {
    int batchInsertOrderItems(List<OrderItem> orderItems);
    List<OrderItem> getOrderItemsByOrderId(Long orderId);
    int deleteOrderItemsByOrderId(Long orderId);
    int deleteOrderItem(Long id);
}
