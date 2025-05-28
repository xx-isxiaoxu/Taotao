package taotaomall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taotaomall.dao.OrderItemDao;
import taotaomall.model.OrderItem;
import taotaomall.service.OrderItemService;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao;

    @Override
    public int batchInsertOrderItems(List<OrderItem> orderItems) {
        return orderItemDao.batchInsertOrderItems(orderItems);
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemDao.getOrderItemsByOrderId(orderId);
    }

    @Override
    public int deleteOrderItemsByOrderId(Long orderId) {
        return orderItemDao.deleteOrderItemsByOrderId(orderId);
    }

    @Override
    public int deleteOrderItem(Long id) {
        return orderItemDao.deleteOrderItem(id);
    }
}
