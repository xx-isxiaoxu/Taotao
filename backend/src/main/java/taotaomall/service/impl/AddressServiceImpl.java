package taotaomall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taotaomall.dao.AddressDao;
import taotaomall.model.Address;
import taotaomall.service.AddressService;
import java.sql.Timestamp;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    @Override
    @Transactional
    public Address addAddress(Address address) {
        // 如果是默认地址，先将其他地址设为非默认
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            addressDao.setDefaultAddress(address.getUserId(), null);
        }
        
        // 设置创建和更新时间
        Timestamp now = new Timestamp(System.currentTimeMillis());
        address.setCreateTime(now);
        address.setUpdateTime(now);
        
        addressDao.insertAddress(address);
        return address;
    }

    @Override
    @Transactional
    public boolean updateAddress(Address address) {
        // 如果是默认地址，先将其他地址设为非默认
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            addressDao.setDefaultAddress(address.getUserId(), address.getId());
        }
        
        // 更新时间
        address.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        
        return addressDao.updateAddress(address) > 0;
    }

    @Override
    public boolean deleteAddress(Long id) {
        return addressDao.deleteAddress(id) > 0;
    }

    @Override
    public List<Address> getUserAddresses(Long userId) {
        return addressDao.getAddressesByUserId(userId);
    }

    @Override
    public Address getAddressById(Long id) {
        return addressDao.getAddressById(id);
    }

    @Override
    @Transactional
    public boolean setDefaultAddress(Long userId, Long addressId) {
        return addressDao.setDefaultAddress(userId, addressId) > 0;
    }
} 