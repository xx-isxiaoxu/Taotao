package taotaomall.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import taotaomall.model.Goods;

import java.util.List;

@Mapper
public interface GoodsDao {
    List<Goods> getAllgoods();
    Goods getGoodById(Integer gid);
    List<Goods> searchGoodsByName(String gname);
    List<Goods> getNewGoodsRandom6();
    List<Goods> getHotGoodsRandom8();
}
