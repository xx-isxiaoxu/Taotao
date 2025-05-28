package taotaomall.model;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class OrderItem {
    private Long id;                // 明细ID
    private Long orderId;           // 订单ID
    private Long goodsId;           // 商品ID
    private String goodsName;       // 商品名称
    private String goodsImage;      // 商品图片
    private Double goodsPrice;      // 商品单价
    private Integer quantity;       // 购买数量
    private String specs;           // 商品规格参数
    private Timestamp createTime;   // 创建时间
} 