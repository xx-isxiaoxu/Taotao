package taotaomall.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import taotaomall.model.Goods;

import java.util.List;

@Mapper
public interface GoodsDao {
    @Select("SELECT * FROM goods")
    List<Goods> getAllgoods();
    /**
     * 根据商品ID获取商品
     */
    @Select("SELECT * FROM goods WHERE gid = #{gid}")
    Goods getGoodById(Integer gid);
    /**
     * 根据商品名称搜索商品
     */
    @Select("SELECT * FROM goods WHERE gname LIKE CONCAT('%', #{gname}, '%')")
    List<Goods> searchGoodsByName(String gname);
}
