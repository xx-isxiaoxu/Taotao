package taotaomall.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import taotaomall.model.Cart;
import taotaomall.service.CartService;
import taotaomall.utils.Result;
import taotaomall.utils.ResultCodeEnum;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    CartService cartService;

    // 查询用户所有购物车项（分页）
    @GetMapping("/all")
    public Result<PageInfo<Cart>> getAllCart(@RequestParam(name = "userId") Long userId,
                                             @RequestParam(name = "pageNum") Integer pageNum,
                                             @RequestParam(name = "pageSize") Integer pageSize) {
        PageInfo<Cart> pageInfo = cartService.getAllCart(userId, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    // 新增购物车项
    @PostMapping("/insert")
    public Result<Integer> insertCart(@RequestBody Cart cart) {
        int res = cartService.insertCart(cart);
        return res > 0 ? Result.success(res) : Result.failure(ResultCodeEnum.FAIL, "添加失败！");
    }

    // 更新购物车项
    @PutMapping("/update")
    public Result<Integer> updateCart(@RequestBody Cart cart) {
        int res = cartService.updateCart(cart);
        return res > 0 ? Result.success(res) : Result.failure(ResultCodeEnum.FAIL, "更新失败！");
    }

    // 删除单个购物车项
    @DeleteMapping("/deleteone/{id}")
    public Result<Void> deleteOne(@PathVariable Integer id) {
        int res = cartService.deleteOne(id);
        return res > 0 ? Result.success() : Result.failure(ResultCodeEnum.FAIL, "删除失败！");
    }

    // 清空用户购物车
    @DeleteMapping("/deleteall/{userId}")
    public Result<Void> deleteAll(@PathVariable Long userId) {
        int res = cartService.deleteAll(userId);
        return res > 0 ? Result.success() : Result.failure(ResultCodeEnum.FAIL, "清空失败！");
    }

    @PutMapping("/addone")
    public Result<Integer> addCart(@RequestParam(name = "userId") Long userId,
                          @RequestParam(name = "goodsId") Integer goodsId,
                          @RequestParam(name = "specs") String specs)
    {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setGoodsId(goodsId);
        cart.setSpecs(specs);
        cart.setQuantity(1);
        int res = cartService.insertCart(cart);
        return res > 0 ? Result.success(res) : Result.failure(ResultCodeEnum.FAIL, "添加失败！");
    }

    @PutMapping("/subone")
    public Result<Integer> subCart(@RequestParam(name = "userId") Long userId,
                          @RequestParam(name = "goodsId") Integer goodsId,
                          @RequestParam(name = "specs") String specs)
    {
        Cart exist = cartService.getCartByUserAndGoods(userId, goodsId, specs);
        if (exist != null) {
            exist.setQuantity(exist.getQuantity() - 1);
            if (exist.getQuantity() < 1) exist.setQuantity(1);
            int res = cartService.updateCart(exist);
            return res > 0 ? Result.success(res) : Result.failure(ResultCodeEnum.FAIL, "更新失败！");
        }
        return Result.failure(ResultCodeEnum.FAIL, "商品不存在！");
    }

    @PostMapping("/pay/{userId}")
    public Result payAll(@PathVariable Long userId)
    {
        int res = cartService.payCart(userId);
        if(res >= 1)
        {
            return Result.success();
        }
        else
        {
            return Result.failure(ResultCodeEnum.FAIL,"结算失败！");
        }
    }
}
