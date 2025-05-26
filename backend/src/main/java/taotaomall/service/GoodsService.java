package taotaomall.service;

import com.github.pagehelper.PageInfo;
import taotaomall.model.Goods;

import java.util.List;

public interface GoodsService {
    /**
     * 分页获取所有商品
     */
    PageInfo<Goods> getAllgoods(Integer pageNum, Integer pageSize);

    /**
     * 根据ID获取商品详情
     */
    Goods getGoodById(Integer gid);

    /**
     * 分页搜索商品
     */
    PageInfo<Goods> searchGoodsByName(String gname, Integer pageNum, Integer pageSize);

    /**
     * 获取随机6个新品商品
     */
    List<Goods> getNewGoodsRandom6();

    /**
     * 获取随机8个热门商品
     */
    List<Goods> getHotGoodsRandom8();
}
