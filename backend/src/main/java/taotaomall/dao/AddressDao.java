package taotaomall.dao;

import taotaomall.model.Address;
import java.util.List;

public interface AddressDao {
    /**
     * 插入新地址
     * @param address 地址信息
     * @return 影响行数
     */
    int insertAddress(Address address);

    /**
     * 更新地址信息
     * @param address 地址信息
     * @return 影响行数
     */
    int updateAddress(Address address);

    /**
     * 删除地址
     * @param id 地址ID
     * @return 影响行数
     */
    int deleteAddress(Long id);

    /**
     * 根据用户ID查询地址列表
     * @param userId 用户ID
     * @return 地址列表
     */
    List<Address> getAddressesByUserId(Long userId);

    /**
     * 根据ID查询地址
     * @param id 地址ID
     * @return 地址信息
     */
    Address getAddressById(Long id);

    /**
     * 设置默认地址
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return 影响行数
     */
    int setDefaultAddress(Long userId, Long addressId);
}
