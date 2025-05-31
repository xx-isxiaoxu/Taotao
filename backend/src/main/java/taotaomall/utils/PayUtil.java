package taotaomall.utils;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import taotaomall.service.OrderService;

import java.util.Map;

@Slf4j
@Component
public class PayUtil {
    @Autowired
    private OrderService orderService;

    // 支付宝配置信息
        //APPID
    private final String APP_ID = "2021000149621445";
         //应用私钥
    private final String APP_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDbitIQVfFA4GSpSJ9d/nfLzMFnjK6xnGcVh+pcAtPOJ50oZsoalNRPlqe2E+tnkPBM620LsiZSn0wUzIqw30xlb3P9PdNImXeCnlp4NVoByvlz0Q4tNDevuZp1GT4C8Nr45r9pZeIBIaM/t1H1jGA0v6CItCTj8vISAykKYGQWPdq5i/ucZ/pStmu/wWwEXmGLcf2EpQlKA29lfb9xwRCXExL6tDTw3VXbhulriTtLqmObNeuwqps2TyfESuVX/dtsHtbw7ae9VxexzrXmr0uXWUjVe13WJZ7mu9J/SVCfdz2n5m8EDRYgnpXiQyVgn6t+7qzDLXRmDc5oOf6EsAVRAgMBAAECggEAYomJRUm9sdfFdnGhDggT7sYlHHv5QMCpR/GsD8Y5GLORBzwzhAC+1kYRWljQdLiKEowscm5wYu8ar88qhL6ExJZkogpo1ZIEIP1AL+oKTvmyQvhW+5UPVXDh0aMVwS4nqkWvLfhLmyRk6Ek4oRRzbdmAjTsFv8YgPlNqzTx8T7p4YXkblW2gFn6lvXTeDgJgNCYXGpldfx6gfitpeRvpK8d2Ne6oG/axkrLrUETlesdCFsidbNUukmkNUSYCycqkVO2PseRwxJFOXAJNbl9DoE78AvTSDO/CynUgCg64g6GiETLziZDMiFhuemmSkKmjLt+7YtH+wgoHjtLb3xDYkQKBgQD51igOz+eepVK3LTyLwmM6n12FlIgd15jIglVb4GrqR7Oyume2uV8KAGnA/9S2dk6UMg8SafiPhXvZ5y9oie4tPeeiBSy43rIBHEmHhejxMsmHhPTTbNJldqhivASKCycBxKhZLkw1sRikQrsSFtlr+iBuHvq8Z5bXQSk6RGn1LQKBgQDg9VcoOizh+0rvOKMvK1c4PJbYuiiiF+4Udd2kQL41oU5jQk05RkpMts8pPvGONZ3XYeMlXOrzixegGw+9LKIrPzskzsEBc1ygmBuk7Mz8HdFhylxkS1GSuqcs03nOm/QSQzGC/iQtdNCiiAG8QXYTuHnx27zUuyy0tMYE0dEvNQKBgDAh1CHWaBcmXqMvseRzFM7seQYNMZQePy8R/QPwXmi4W6RpSSXo3Kh+n+Ew6HgmnsjqRAEWLW7pFo1KftWzDRHmRUPvn0gWj1CrdHcJk3E0iaL0OH6rDnXBwHsVIgK+V0t96bkUtFX7wnWS7Zt0j3q/+5N7oG6/vk3GHf09xPQZAoGAXcNk1IuK8ZYAIb2Fpr911ybJ6QOOtCEtRrHp8/BYV5ooRg74iQrXJSp9Jj/qBtAqPf+64XPdKi2rZzxgTwUvgr8UM79JiwU+cmF0BEX/GoaDIzNpVxRdp77Mt05d0tQF4ybGmEa9ZYTvAQ+XSZQvVwDpMvnXtwNdGuTONGwPuJECgYAAkUvjkgWGPWgjQVjPnngutv64gABRVmXcOCC3Zr2tRZ1ISkwxrlphiMpI4Je70O2LrXdZYFxivnX1qMG242+hrJttxkkXIRFhXOZl7/UXitGgA22H0iitPbuv1dHl9gyqstZK8FZ/ZoV+OlmnGUbIrTSFjtVF6LbCdgWlwhIysQ==";
        //商户私钥
    private final String CHARSET = "UTF-8";
        //支付宝公钥
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhxB3k48/OCsvOcFSKz7P1oA/lCQkJkWwb716btYi796Hlpd8z2MqkLfa/hFKrribeyuiSJIq2KCasvrLWIPRF1T0Iw0f7u/UIHYD6N7fCwDb21pYR6g/z7qFKJiU5mbx2hVYxzSChpr1CJADxNfRanJ36gbdFv27AyM9AlZErKc7qgPCPxQ8PEGLs69VrCAbkjXm83V4dsgjbMl3u0VXPUEYpyhhBKouCe9wkA8C0FvQNvkSUqj7V8y1v4MB4CL6MvqHJccME1tMI30KskBb/AV1mDTLbTU07brDmB+2PlXrzeioYiI5VHtP0RYLaf33qySVwgbesFtsV8t6tuduRQIDAQAB";
        //沙箱的接口路径
    private final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private final String FORMAT = "JSON";
    private final String SIGN_TYPE = "RSA2";
    private final String NOTIFY_URL = "http://k8968e3e.natappfree.cc/api/alipay/toSuccess";
    private final String RETURN_URL = "http://localhost:5173/api/alipay/toSuccess";

    private AlipayClient alipayClient;

    public String sendRequestToAlipay(String outTradeNo, Float totalAmount, String subject, String returnUrl) throws AlipayApiException {
        alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(NOTIFY_URL);

        String body = "";
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\","
                + "\"total_amount\":\"" + totalAmount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();
        System.out.println("返回的结果是："+result );
        return result;
    }

    //    通过订单编号查询
    public String query(String id){
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", id);
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = null;
        String body=null;
        try {
            response = alipayClient.execute(request);
            body = response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return body;
    }
}
