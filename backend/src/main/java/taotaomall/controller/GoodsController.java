package taotaomall.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import taotaomall.model.Goods;
import taotaomall.service.GoodsService;
import taotaomall.utils.Result;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 分页获取所有商品
     */
    @GetMapping("/all")
    public Result<PageInfo<Goods>> getAllGoods(@RequestParam(name = "pageNum") Integer pageNum,
                              @RequestParam(name = "pageSize") Integer pageSize) {
        PageInfo<Goods> pageInfo = goodsService.getAllgoods(pageNum, pageSize); // 修改这里
        return Result.success(pageInfo);
    }

    /**
     * 获取商品详情
     */
    @GetMapping("/detail/{gid}")
    public Result<Goods> getGoodsDetail(@PathVariable(name = "gid") Integer gid) {
        Goods goods = goodsService.getGoodById(gid);
        return Result.success(goods);
    }

    /**
     * 搜索商品
     */
    @GetMapping("/search")
    public Result<PageInfo<Goods>> searchGoods(@RequestParam(name = "gname") String gname,
                            @RequestParam(name = "pageNum") Integer pageNum,
                            @RequestParam(name = "pageSize") Integer pageSize) {
        PageInfo<Goods> pageInfo = goodsService.searchGoodsByName(gname, pageNum, pageSize); // 修改这里
        return Result.success(pageInfo);
    }
}