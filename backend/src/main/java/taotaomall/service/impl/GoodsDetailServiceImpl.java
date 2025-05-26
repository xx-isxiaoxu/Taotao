package taotaomall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taotaomall.dao.GoodsDetailDao;
import taotaomall.model.GoodsDetail;
import taotaomall.service.GoodsDetailService;

@Service
public class GoodsDetailServiceImpl implements GoodsDetailService {

    @Autowired
    private GoodsDetailDao goodsDetailDao;

    @Override
    public GoodsDetail getByGoodsId(Integer goodsId) {
        return goodsDetailDao.selectByGoodsId(goodsId);
    }

    @Override
    public int addGoodsDetail(GoodsDetail goodsDetail) {
        return goodsDetailDao.insertGoodsDetail(goodsDetail);
    }

    @Override
    public int updateGoodsDetail(GoodsDetail goodsDetail) {
        return goodsDetailDao.updateGoodsDetail(goodsDetail);
    }
}
