package taotaomall.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import taotaomall.model.Userorder;
import taotaomall.service.OrderService;
import taotaomall.utils.Result;
import taotaomall.utils.ResultCodeEnum;

import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    // 创建订单
    @PostMapping("/create")
    public Result<Map<String, Long>> createOrder(@RequestBody Userorder order) {
        return orderService.insertOrderReturnId(order);
    }

    // 分页查询用户所有订单
    @GetMapping("/list")
    public PageInfo<Userorder> getAllOrder(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return orderService.getAllOrder(userId, pageNum, pageSize);
    }

    // 查询单个订单详情
    @GetMapping("/detail/{orderId}")
    public Userorder getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    // 删除单个订单
    @DeleteMapping("/delete/{id}")
    public int deleteOneOrder(@PathVariable Long id) {
        return orderService.deleteOneOrder(id);
    }

    // 删除用户所有订单
    @DeleteMapping("/deleteAll/{userId}")
    public int deleteAllOrder(@PathVariable Long userId) {
        return orderService.deleteAllOrder(userId);
    }

    @PostMapping("/pay/{orderId}")
    public Result<?> payOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String token) {
        // 这里可以加 token 校验逻辑
        return orderService.payOrder(orderId);
    }
}
