package taotaomall.service.impl;

import taotaomall.dao.OrderDao;
import taotaomall.model.Cart; // 导入 Cart 实体类，请根据实际包路径修改
import com.github.pagehelper.PageInfo; // 导入 PageInfo 类
import com.github.pagehelper.PageHelper; // 导入 PageHelper 类
import java.util.List; // 导入 List 类
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taotaomall.dao.CartDao;
import taotaomall.dao.GoodsDao;
import taotaomall.model.Goods;
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
    public Cart insertCart(Cart cart) {
        Cart cartJdbc = cartDao.getCartByUGid(cart.getUid(),cart.getGoodid());
        Goods goods = goodsDao.getGoodById(cart.getGoodid());
        if(cartJdbc!=null)
        {
            int number = cart.getNumber() + cartJdbc.getNumber();
            int price = number * goods.getGprice();
            cartDao.updateCart(number,price,cartJdbc.getId());
            cart.setNumber(number);
            cart.setPrice(price);
            cart.setId(cartJdbc.getId());
        }
        else
        {
            int price = cart.getNumber() * goods.getGprice();
            cart.setPrice(price);
            cartDao.insertCart(cart);
        }
        return cart;
    }

    @Override
    public PageInfo<Cart> getAllCart(Integer uid, Integer pageNum, Integer pageSize) {
        //开启分页
        PageHelper.startPage(pageNum,pageSize);
        List<Cart> cartList = cartDao.getAllCart(uid);
        PageInfo<Cart> pageInfo = new PageInfo<>(cartList);
        return pageInfo;
    }

    @Override
    public Cart modifyNumber(Integer uid, Integer gid, Integer type) {
        Cart cartJdbc = cartDao.getCartByUGid(uid,gid);
        Goods goods = goodsDao.getGoodById(gid);
        if(cartJdbc!=null)
        {
            int number = cartJdbc.getNumber() + 1 * (type);
            cartJdbc.setNumber(number);
            int price = number * goods.getGprice();
            cartJdbc.setPrice(price);
            cartDao.updateCart(number,price,cartJdbc.getId());
            return cartJdbc;
        }
        return null;
    }

    @Override
    public int deleteOne(Integer id) {
        return cartDao.deleteOne(id);
    }

    @Override
    public int deleteAll(Integer uid) {
        return cartDao.deleteAll(uid);
    }

    @Override
    public int payCart(Integer uid) {
        List<Cart> cartList = cartDao.getAllCart(uid);
        if(cartList.size()==0)
        {
            return 0;
        }
        //插入订单列表
        for(Cart cart : cartList)
        {
            Userorder userorder = new Userorder(cart.getNumber(),cart.getPrice(),cart.getGoodsname(),cart.getUid());
            orderDao.InsertOrder(userorder);
    }
       //删除购物车
        return cartDao.deleteAll(uid);
    }
}