package taotaomall.service;

import taotaomall.model.GoodsDetail;

public interface GoodsDetailService {
    GoodsDetail getByGoodsId(Integer goodsId);
    int addGoodsDetail(GoodsDetail goodsDetail);
    int updateGoodsDetail(GoodsDetail goodsDetail);
}
