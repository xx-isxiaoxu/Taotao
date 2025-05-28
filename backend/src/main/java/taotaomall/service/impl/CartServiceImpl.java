package taotaomall.service.impl;

import taotaomall.dao.OrderDao;
import taotaomall.model.Cart;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageHelper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taotaomall.dao.CartDao;
import taotaomall.dao.GoodsDao;
import taotaomall.model.Userorder;
import taotaomall.service.CartService;

@Service
public class CartServiceImpl implements CartService {
    @Autowired(required = false)
    CartDao cartDao;
    @Autowired(required = false)
    GoodsDao goodsDao;
    @Autowired(required = false)
    OrderDao orderDao;

    @Override
    public int insertCart(Cart cart) {
        // 检查是否已存在相同商品+规格的购物车项
        Cart exist = cartDao.getCartByUserAndGoods(cart.getUserId(), cart.getGoodsId(), cart.getSpecs());
        if (exist != null) {
            // 已存在则更新数量
            exist.setQuantity(exist.getQuantity() + cart.getQuantity());
            exist.setGoodsPrice(cart.getGoodsPrice()); // 可根据实际业务调整
            return cartDao.updateCart(exist.getQuantity(), exist.getGoodsPrice(), exist.getId(), exist.getSpecs());
        } else {
            return cartDao.insertCart(cart);
        }
    }

    @Override
    public Cart getCartByUserAndGoods(Long userId, Integer goodsId, String specs) {
        return cartDao.getCartByUserAndGoods(userId, goodsId, specs);
    }

    @Override
    public int updateCart(Cart cart) {
        return cartDao.updateCart(cart.getQuantity(), cart.getGoodsPrice(), cart.getId(), cart.getSpecs());
    }

    @Override
    public PageInfo<Cart> getAllCart(Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Cart> cartList = cartDao.getAllCart(userId);
        return new PageInfo<>(cartList);
    }

    @Override
    public int deleteOne(Integer id) {
        return cartDao.deleteOne(id);
    }

    @Override
    public int deleteAll(Long userId) {
        return cartDao.deleteAll(userId);
    }

    @Override
    public int payCart(Long userId) {
        List<Cart> cartList = cartDao.getAllCart(userId);
        if(cartList.size() == 0) {
            return 0;
        }
        // 插入订单列表
        for(Cart cart : cartList) {
            Userorder userorder = new Userorder(
                cart.getUserId(),
                cart.getGoodsPrice() * cart.getQuantity(),
                "测试地址",
                "测试收货人",
                "13800000000"
            );
            orderDao.InsertOrder(userorder);
        }
        // 删除购物车
        return cartDao.deleteAll(userId);
    }
}