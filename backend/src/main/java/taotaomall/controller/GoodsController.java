package taotaomall.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import taotaomall.model.Goods;
import taotaomall.service.GoodsService;
import taotaomall.utils.Result;

import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    
    @Autowired
    private GoodsService goodsService;

    /**
     * 分页获取所有商品
     */
    @GetMapping("/all")
    public Result getAllGoods(@RequestParam(name = "pageNum") Integer pageNum,
                              @RequestParam(name = "pageSize") Integer pageSize) {
        PageInfo<Goods> pageInfo = goodsService.getAllgoods(pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 获取商品详情
     */
    @GetMapping("/detail/{gid}")
    public Result getGoodsDetail(@PathVariable(name = "gid") Integer gid) {
        Goods goods = goodsService.getGoodById(gid);
        return Result.success(goods);
    }

    /**
     * 搜索商品
     */
    @GetMapping("/search")
    public Result searchGoods(@RequestParam(name = "gname") String gname,
                            @RequestParam(name = "pageNum") Integer pageNum,
                            @RequestParam(name = "pageSize") Integer pageSize) {
        PageInfo<Goods> pageInfo = goodsService.searchGoodsByName(gname, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    /**
     * 获取新品上架商品
     */
    @GetMapping("/new")
    public Result getNewGoods() {
        List<Goods> goodsList = goodsService.getNewGoodsRandom6();
        return Result.success(goodsList);
    }

    /**
     * 获取热门推荐商品
     */
    @GetMapping("/hot")
    public Result getHotGoods() {
        List<Goods> goodsList = goodsService.getHotGoodsRandom8();
        return Result.success(goodsList);
    }
}
