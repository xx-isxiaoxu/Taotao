package taotaomall.dao;

import taotaomall.model.GoodsDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsDetailDao {
    // 根据商品ID查询详情
    GoodsDetail selectByGoodsId(@Param("goodsId") Integer goodsId);

    // 新增商品详情
    int insertGoodsDetail(GoodsDetail goodsDetail);

    // 更新商品详情
    int updateGoodsDetail(GoodsDetail goodsDetail);

    // 你可以根据需要添加更多方法
}
