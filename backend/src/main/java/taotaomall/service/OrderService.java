package taotaomall.service;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import taotaomall.model.Userorder;

@Service
public interface OrderService {
    PageInfo<Userorder> getAllOrder(Integer uid, Integer pageNum, Integer pageSize);
    int deleteOneOrder(Integer id);
    int deleteAllOrder(Integer uid);
}
