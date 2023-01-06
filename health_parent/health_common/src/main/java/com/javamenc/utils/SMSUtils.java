package com.javamenc.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 短信发送工具类
 */
public class SMSUtils {
    public static final String VALIDATE_CODE = "SMS_205406138";     //发送短信验证码
    public static final String ORDER_NOTICE = "SMS_205392263";      //体检预约成功通知

    public static final String QUICK_LOGIN_VALIDATE_CODE = "SMS_181211518";//发送短信验证码
    /**
     * 发送短信
     * @param templateCode      短信模板
     * @param phoneNumbers      手机号
     * @param param             参数 ${code}
     * @throws ClientException
     */
    public static void sendShortMessage(String templateCode, String phoneNumbers, String param) throws ClientException {
        // 设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化ascClient需要的几个参数
        final String product = "Dysmsapi";                    // 短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";        // 短信API产品域名（接口地址固定，无需修改）

        // 替换成你的AK
        final String accessKeyId = "LTAI4G5TnMoyuAtVSRrj2qoR";                    // 你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = "Oqn3cegG9liBkrT54mxSxtOin1fMO6";          // 你的accessKeySecret，参考本文档步骤2

        // 初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);                    // 使用post提交
        request.setPhoneNumbers(phoneNumbers);                 // 必填:待发送手机号
        request.setSignName("大永弟健康系统");                        // 必填:短信签名-可在短信控制台中找到
        request.setTemplateCode(templateCode);                 // 必填:短信模板-可在短信控制台中找到

        request.setTemplateParam("{\"code\":\"" + param + "\"}");

        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        System.out.println(sendSmsResponse.getMessage());

        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            // 请求成功
            System.out.println("请求成功");
        }
    }
}
