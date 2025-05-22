package taotaomall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taotaomall.dao.OrderDao;
import taotaomall.model.Userorder;
import taotaomall.service.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired(required = false)
    OrderDao orderDao;
    @Override
    public PageInfo<Userorder> getAllOrder(Integer uid, Integer pageNum, Integer pageSize) {
        //开启分页
        PageHelper.startPage(pageNum,pageSize);
        List<Userorder> userorderList = orderDao.getAllOrder(uid);
        PageInfo<Userorder> pageInfo = new PageInfo<>(userorderList);
        return pageInfo;
    }

    @Override
    public int deleteOneOrder(Integer id) {
        return orderDao.deleteOneOrder(id);
    }

    @Override
    public int deleteAllOrder(Integer uid) {
        return orderDao.deleteAllOrder(uid);
    }
}
