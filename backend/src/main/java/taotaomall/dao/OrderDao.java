package taotaomall.dao;

import org.apache.ibatis.annotations.Mapper;
import taotaomall.model.Userorder;

import java.util.List;

@Mapper
public interface OrderDao {
    int InsertOrder(Userorder order);
    List<Userorder> getAllOrder(Long userId);
    Userorder getOrderById(Long orderId);
    int deleteOneOrder(Long id);
    int deleteAllOrder(Long userId);
    int updateOrderPayStatus(String orderNo, String status, java.sql.Timestamp payTime);
    int deleteOrderItemsByOrderId(Long orderId);
    int updateOrder(Userorder order);
}
