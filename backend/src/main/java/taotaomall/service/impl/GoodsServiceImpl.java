package taotaomall.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taotaomall.dao.GoodsDao;
import taotaomall.model.Goods;
import taotaomall.service.GoodsService;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Value("${image.url}")
    private String imgUrl;

    @Override
    public PageInfo<Goods> getAllgoods(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goodsList = goodsDao.getAllgoods();
        for(Goods goods : goodsList) {
            String picpath = goods.getGpicture();
            goods.setGpicture(imgUrl + picpath);
        }
        return new PageInfo<>(goodsList);
    }

    @Override
    public Goods getGoodById(Integer gid) {
        Goods goods = goodsDao.getGoodById(gid);
        if(goods == null) {
            return null;
        }
        String picpath = goods.getGpicture();
        goods.setGpicture(imgUrl + picpath);
        return goods;
    }

    @Override
    public PageInfo<Goods> searchGoodsByName(String gname, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goodsList = goodsDao.searchGoodsByName(gname);
        for(Goods goods : goodsList) {
            String picpath = goods.getGpicture();
            goods.setGpicture(imgUrl + picpath);
        }
        return new PageInfo<>(goodsList);
    }
}