package taotaomall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taotaomall.dao.OrderDao;
import taotaomall.dao.OrderItemDao;
import taotaomall.model.OrderItem;
import taotaomall.model.Userorder;
import taotaomall.service.OrderService;
import taotaomall.utils.Result;
import taotaomall.utils.ResultCodeEnum;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Override
    public PageInfo<Userorder> getAllOrder(Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Userorder> userorderList = orderDao.getAllOrder(userId);
        return new PageInfo<>(userorderList);
    }

    @Override
    public int insertOrder(Userorder order) {
        return orderDao.InsertOrder(order);
    }

    @Override
    public Userorder getOrderById(Long orderId) {
        Userorder order = orderDao.getOrderById(orderId);
        if (order != null) {
            // 手动查明细并set
            List<OrderItem> itemList = orderItemDao.getOrderItemsByOrderId(orderId);
            order.setOrderItemList(itemList);
        }
        return order;
    }

    @Override
    @Transactional
    public int deleteOneOrder(Long id) {
        // 先删明细
        orderItemDao.deleteOrderItemsByOrderId(id);
        // 再删主表
        return orderDao.deleteOneOrder(id);
    }

    @Override
    public int deleteAllOrder(Long userId) {
        return orderDao.deleteAllOrder(userId);
    }

    @Override
    @Transactional
    public Result<Map<String, Long>> insertOrderReturnId(Userorder order) {
        if (order.getExpireTime() == null) {
            order.setExpireTime(System.currentTimeMillis() + 15 * 60 * 1000); // 15分钟
        }
        if (order.getCreateTime() == null) {
            order.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));
        }
        int result = orderDao.InsertOrder(order);
        if (result > 0) {
            Long orderId = order.getId();
            for (OrderItem item : order.getOrderItemList()) {
                item.setOrderId(orderId);
                if (item.getCreateTime() == null) {
                    item.setCreateTime(new java.sql.Timestamp(System.currentTimeMillis()));
                }
            }
            int itemResult = orderItemDao.batchInsertOrderItems(order.getOrderItemList());
            if (itemResult == order.getOrderItemList().size()) {
                Map<String, Long> map = new HashMap<>();
                map.put("id", orderId);
                return Result.success(map);
            } else {
                throw new RuntimeException("插入订单明细失败");
            }
        } else {
            return Result.failure(ResultCodeEnum.valueOf("插入订单失败"));
        }
    }

    @Override
    public Result<?> payOrder(Long orderId) {
        // 1. 查询订单
        Userorder order = orderDao.getOrderById(orderId);
        if (order == null) {
            return Result.failure(ResultCodeEnum.NOT_FOUND, "订单不存在");
        }
        // 2. 判断是否已支付或已超时
        if (!"unpaid".equals(order.getStatus())) {
            return Result.failure(ResultCodeEnum.FAIL, "订单状态异常，无法支付");
        }
        long now = System.currentTimeMillis();
        if (order.getExpireTime() != null && now > order.getExpireTime()) {
            return Result.failure(ResultCodeEnum.FAIL, "订单已超时，无法支付");
        }
        // 3. 更新状态和支付时间
        int updated = orderDao.updateOrderPayStatus(orderId, "paid", new java.sql.Timestamp(now));
        if (updated > 0) {
            return Result.success();
        } else {
            return Result.failure(ResultCodeEnum.FAIL, "支付失败");
        }
    }
}
