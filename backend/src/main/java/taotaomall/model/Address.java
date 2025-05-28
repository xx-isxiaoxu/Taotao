package taotaomall.model;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Address {
    private Long id;                // 地址ID
    private Long userId;            // 用户ID
    private String receiver;        // 收货人
    private String phone;           // 联系电话
    private String province;        // 省份
    private String city;            // 城市
    private String district;        // 区县
    private String detail;          // 详细地址
    private Boolean isDefault;      // 是否默认地址
    private Timestamp createTime;   // 创建时间
    private Timestamp updateTime;   // 更新时间
} 