package taotaomall.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import taotaomall.dao.OrderDao;
import taotaomall.model.Userorder;
import taotaomall.service.OrderService;
import taotaomall.service.impl.OrderServiceImpl;
import taotaomall.utils.PayUtil;
import taotaomall.utils.Result;
import taotaomall.utils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import taotaomall.utils.JWTUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/api/alipay")
public class AliPayController {
    @Autowired
    private PayUtil payUtil;
    @Autowired
    private OrderService orderService;

    private Userorder userorder = null;
    private String tokens = "";
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private OrderDao orderDao;

   @ResponseBody
   @PostMapping("/pay")
   public String alipay(@RequestBody Userorder orderModel) throws AlipayApiException {
       Userorder order = orderService.getOrderById(orderModel.getId());
       if (order == null) {
           return "订单不存在";
       }
       Float orderValue = order.getTotalAmount().floatValue();

       // 生成唯一订单号
       String orderNo = System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "");
       order.setOrderNo(orderNo);
       orderService.updateOrder(order);

       // 商品名
       String subject = "淘淘商城订单-" + orderNo;

       // 设置 return_url
       String returnUrl = "http://localhost:5173/#/order/result/" + order.getId();

       // 调用支付宝工具类
       String paymentForm = payUtil.sendRequestToAlipay(orderNo, orderValue, subject, returnUrl);

       return paymentForm;
   }

    //    当我们支付完成之后跳转这个请求并携带参数，我们将里面的订单id接收到，通过订单id查询订单信息，信息包括支付是否成功等
    @PostMapping("/toSuccess")
    public String returns(String out_trade_no) throws ParseException {
        String query = payUtil.query(out_trade_no);
        System.out.println("==>" + query);
        JSONObject jsonObject = JSONObject.parseObject(query);
        Object o = jsonObject.get("alipay_trade_query_response");
        Map map = (Map) o;
        System.out.println(map);
        Object s = map.get("trade_status");
        if (s.equals("TRADE_SUCCESS")) {
            //当支付成功之后要执行的操作
            System.out.println("订单支付成功");
            long now = System.currentTimeMillis();
            orderDao.updateOrderPayStatus(out_trade_no, "paid", new java.sql.Timestamp(now));
            System.out.println(s);
            return "redirect:http://localhost:5173/#/paysuccess";
        } else {
//            支付失败要执行的操作
            System.out.println("订单支付失败");
            return "index";
        }
    }

    /*
参数1：订单号
参数2：订单金额
参数3：订单名称
 */

}