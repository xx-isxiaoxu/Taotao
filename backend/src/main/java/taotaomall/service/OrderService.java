package taotaomall.service;

import com.github.pagehelper.PageInfo;
import taotaomall.model.Userorder;
import taotaomall.utils.Result;

import java.util.Map;

public interface OrderService {
    // 分页查询用户所有订单
    PageInfo<Userorder> getAllOrder(Long userId, Integer pageNum, Integer pageSize);

    // 创建订单
    int insertOrder(Userorder order);

    // 查询单个订单详情
    Userorder getOrderById(Long orderId);

    // 删除单个订单
    int deleteOneOrder(Long id);

    // 删除用户所有订单
    int deleteAllOrder(Long userId);

    Result<Map<String, Long>> insertOrderReturnId(Userorder order);

    // 新增
    Result<?> payOrder(Long orderId);
}
