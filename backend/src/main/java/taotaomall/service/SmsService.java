package taotaomall.service;

public interface SmsService {
    void sendSms(String phone, String code);
}