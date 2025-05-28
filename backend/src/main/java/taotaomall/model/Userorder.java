package taotaomall.model;

import lombok.Data;
import java.sql.Timestamp;
import java.util.List;
import taotaomall.model.OrderItem;

@Data
public class Userorder {
    private Long id;                // 订单ID
    private String orderNo;         // 订单编号
    private Long userId;            // 用户ID
    private Double totalAmount;     // 订单总金额
    private String status;          // 订单状态
    private String address;         // 收货地址
    private String receiver;        // 收货人
    private String phone;           // 联系电话
    private Timestamp createTime;   // 创建时间
    private Timestamp payTime;      // 支付时间
    private Timestamp cancelTime;   // 取消时间
    private Timestamp finishTime;   // 完成时间
    private String remark;          // 订单备注
    private List<OrderItem> orderItemList;
    private Long expireTime;        // 新增字段

    // 无参构造方法
    public Userorder() {
    }

    // 创建订单的构造方法
    public Userorder(Long userId, Double totalAmount, String address, String receiver, String phone) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.address = address;
        this.receiver = receiver;
        this.phone = phone;
        this.status = "unpaid";  // 设置初始状态为未支付
        this.createTime = new Timestamp(System.currentTimeMillis());  // 设置创建时间
        this.setExpireTime(System.currentTimeMillis() + 3 * 60 * 1000); // 15分钟后过期
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
