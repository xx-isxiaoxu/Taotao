package taotaomall.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Override
    public void sendSms(String phone, String code) {
        // 假短信：只打印日志，不发真实短信
        logger.info("【开发环境】向手机号 {} 发送验证码：{}", phone, code);
        // 生产环境可在这里接入真实短信API
    }
}
