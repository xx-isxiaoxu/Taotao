package taotaomall.service;

import com.github.pagehelper.PageInfo;
import taotaomall.model.Cart;
import java.util.List;

public interface CartService {
    int insertCart(Cart cart);
    Cart getCartByUserAndGoods(Long userId, Integer goodsId, String specs);
    int updateCart(Cart cart);
    PageInfo<Cart> getAllCart(Long userId, Integer pageNum, Integer pageSize);
    int deleteOne(Integer id);
    int deleteAll(Long userId);
    int payCart(Long userId);
}