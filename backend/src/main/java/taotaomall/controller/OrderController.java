package taotaomall.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import taotaomall.model.Userorder;
import taotaomall.service.OrderService;
import taotaomall.utils.Result;
import taotaomall.utils.ResultCodeEnum;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/all")
    public Result<PageInfo<Userorder>> getAllOrder(@RequestParam(name = "uid") Integer uid,
                              @RequestParam(name = "pageNum") Integer pageNum,
                              @RequestParam(name = "pageSize") Integer pageSize)
    {
        PageInfo<Userorder> pageInfo = orderService.getAllOrder(uid,pageNum,pageSize);
        return Result.success(pageInfo);
    }

    @DeleteMapping("/deleteone/{id}")
    public Result<Void> deldeteOne(@PathVariable Integer id)
    {
        int res = orderService.deleteOneOrder(id);
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
        int res = orderService.deleteAllOrder(uid);
        if(res >= 1)
        {
            return Result.success();
        }
        else
        {
            return Result.failure(ResultCodeEnum.FAIL,"清空失败！");
        }
    }
}
