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
    @GetMapping("/all")
    public Result<PageInfo<Cart>> getAllCart(@RequestParam(name = "uid") Integer uid,
                             @RequestParam(name = "pageNum") Integer pageNum,
                             @RequestParam(name = "pageSize") Integer pageSize)
    {
        PageInfo<Cart> pageInfo = cartService.getAllCart(uid,pageNum,pageSize);
        return Result.success(pageInfo);
    }

    @PostMapping("/insert")
    public Result<Cart> insertCart(@RequestBody Cart cart)
    {
        Cart cart1 = cartService.insertCart(cart);
        return Result.success(cart1);
    }


    @PutMapping("/addone")
    public Result<Cart> addCart(@RequestParam(name = "uid") Integer uid,
                          @RequestParam(name = "gid") Integer gid)
    {
        Cart cart = cartService.modifyNumber(uid,gid,1);
        return Result.success(cart);
    }

    @PutMapping("/subone")
    public Result<Cart> subCart(@RequestParam(name = "uid") Integer uid,
                          @RequestParam(name = "gid") Integer gid)
    {
        Cart cart = cartService.modifyNumber(uid,gid,-1);
        return Result.success(cart);
    }

    @DeleteMapping("/deleteone/{id}")
    public Result<Void> deldeteOne(@PathVariable Integer id)
    {
        int res = cartService.deleteOne(id);
        if(res >= 1)
        {
            return Result.success();
        }
        else
        {
            return Result.failure(ResultCodeEnum.FAIL,"删除失败！");
        }
    }

    @DeleteMapping("/deleteall/{uid}")
    public Result<Void> deldeteAll(@PathVariable Integer uid)
    {
        int res = cartService.deleteAll(uid);
        if(res >= 1)
        {
            return Result.success();
        }
        else
        {
            return Result.failure(ResultCodeEnum.FAIL,"清空失败！");
        }
    }

    @PostMapping("/pay/{uid}")
    public Result payAll(@PathVariable Integer uid)
    {
        int res = cartService.payCart(uid);
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
