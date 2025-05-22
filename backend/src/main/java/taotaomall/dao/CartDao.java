package taotaomall.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import taotaomall.model.Cart;

@Mapper
public interface CartDao {
    int insertCart(Cart cart);

    Cart getCartByUGid(Integer uid,Integer gid);

    int updateCart(Integer number,Integer price,Integer id);

    List<Cart> getAllCart(Integer uid);

    int deleteOne(Integer id);
    int deleteAll(Integer uid);
}
