package taotaomall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import taotaomall.model.OrderItem;
import taotaomall.service.OrderItemService;

import java.util.List;

@RestController
@RequestMapping("/api/orderItem")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    // 查询某订单的所有明细
    @GetMapping("/list/{orderId}")
    public List<OrderItem> getOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderItemService.getOrderItemsByOrderId(orderId);
    }

    // 批量插入订单明细
    @PostMapping("/batchInsert")
    public int batchInsertOrderItems(@RequestBody List<OrderItem> orderItems) {
        return orderItemService.batchInsertOrderItems(orderItems);
    }

    // 删除某订单的所有明细
    @DeleteMapping("/deleteByOrder/{orderId}")
    public int deleteOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderItemService.deleteOrderItemsByOrderId(orderId);
    }

    // 删除单个明细
    @DeleteMapping("/delete/{id}")
    public int deleteOrderItem(@PathVariable Long id) {
        return orderItemService.deleteOrderItem(id);
    }
}
