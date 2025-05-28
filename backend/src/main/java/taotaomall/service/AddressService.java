package taotaomall.service;

import taotaomall.model.Address;
import java.util.List;

public interface AddressService {
    // 添加地址
    Address addAddress(Address address);
    
    // 更新地址
    boolean updateAddress(Address address);
    
    // 删除地址
    boolean deleteAddress(Long id);
    
    // 获取用户的所有地址
    List<Address> getUserAddresses(Long userId);
    
    // 获取单个地址详情
    Address getAddressById(Long id);
    
    // 设置默认地址
    boolean setDefaultAddress(Long userId, Long addressId);
} 