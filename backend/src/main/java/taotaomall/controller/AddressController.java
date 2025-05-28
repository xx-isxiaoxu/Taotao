package taotaomall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taotaomall.model.Address;
import taotaomall.service.AddressService;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // 添加地址
    @PostMapping
    public ResponseEntity<Address> addAddress(@RequestBody Address address) {
        return ResponseEntity.ok(addressService.addAddress(address));
    }

    // 更新地址
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateAddress(@PathVariable Long id, @RequestBody Address address) {
        address.setId(id);
        return ResponseEntity.ok(addressService.updateAddress(address));
    }

    // 删除地址
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteAddress(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.deleteAddress(id));
    }

    // 获取用户的所有地址
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getUserAddresses(userId));
    }

    // 获取单个地址详情
    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    // 设置默认地址
    @PutMapping("/default/{userId}/{addressId}")
    public ResponseEntity<Boolean> setDefaultAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.setDefaultAddress(userId, addressId));
    }
} 