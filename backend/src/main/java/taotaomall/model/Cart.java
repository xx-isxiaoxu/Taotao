package taotaomall.model;

import lombok.Data;
import java.util.Date;

@Data
public class Cart {
    private Integer id;
    private Long userId;           // bigint
    private Integer goodsId;       // int
    private Integer goodsDetailId; // int
    private String goodsName;      // varchar(255)
    private String goodsImage;     // varchar(255)
    private Double goodsPrice;     // decimal(10,2)
    private String specs;          // text (JSON字符串)
    private Integer quantity;      // int
    private Date createTime;       // datetime
    private Date updateTime;       // datetime
}
