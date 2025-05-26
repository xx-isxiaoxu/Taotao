package taotaomall.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import taotaomall.model.Cart;

@Mapper
public interface CartDao {
    int insertCart(Cart cart);

    // 根据用户ID和商品ID查询购物车项
    Cart getCartByUserAndGoods(@Param("userId") Long userId, @Param("goodsId") Integer goodsId, @Param("specs") String specs);

    // 更新购物车项（数量、价格、规格等）
    int updateCart(@Param("quantity") Integer quantity, @Param("goodsPrice") Double goodsPrice, @Param("id") Integer id, @Param("specs") String specs);

    // 查询用户所有购物车项
    List<Cart> getAllCart(@Param("userId") Long userId);

    // 删除单个购物车项
    int deleteOne(@Param("id") Integer id);
    // 清空用户购物车
    int deleteAll(@Param("userId") Long userId);
}
