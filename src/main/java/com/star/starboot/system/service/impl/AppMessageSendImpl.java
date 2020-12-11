package com.star.starboot.system.service.impl;

import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.star.starboot.constant.SystemConstant;
import com.star.starboot.system.dto.UsersDto;
import com.star.starboot.system.entity.Message;
import com.star.starboot.system.service.MessageSend;
import com.star.starboot.system.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.system.service.impl
 * @Description: app消息发送
 * @Author: xpy
 * @Date: Created in 2020年12月04日 10:17 上午
 */
@Service(SystemConstant.MSG_APP)
public class AppMessageSendImpl  implements MessageSend {
    /**
     * STEP1：获取应用基本信息
     */
    @Value("${getui.appid}")
    private String appId = "";
    @Value("${getui.appkey}")
    private String appKey = "";
    @Value("${getui.masterSecret}")
    private String masterSecret = "";
    @Value("${getui.url}")
    private String url = "";

    @Autowired
    private UsersService usersService;

    @Override
    public void sendMsg(List<String> tos, Message msg, String from) {
        if(!StringUtils.isEmpty(tos)&& tos.size()>0){
            IGtPush push = new IGtPush(url, appKey, masterSecret);
            IBatch batch = push.getBatch();

            try {
                generateMessageToSingleList(batch,msg, tos);
                batch.submit();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @params getMessageToSingleList构建单个推送消息
     * @params tos推送角色目标列表
     */
    private void generateMessageToSingleList(IBatch batch,Message pushMessage, List<String> tos) throws Exception {
        if(!StringUtils.isEmpty(tos)&& tos.size()>0) {
            // 获取每个接收人的clientId
            List<UsersDto> list = usersService.getByIds(tos);

            Style0 style = new Style0();
            // STEP2：设置推送标题、推送内容
            style.setTitle(pushMessage.getTitle());
            style.setText(pushMessage.getAppMessage());
            style.setRing(true);  // 设置响铃
            style.setVibrate(true);  // 设置震动

            // STEP4：选择通知模板
            NotificationTemplate template = new NotificationTemplate();
            template.setAppId(appId);
            template.setAppkey(appKey);
            template.setStyle(style);
            // 点击消息打开应用
            template.setTransmissionType(1);
            // 设置是否覆盖消息
            if(!StringUtils.isEmpty(pushMessage.getNotifyId())){
                template.setNotifyid(pushMessage.getNotifyId());
            }

            SingleMessage message = new SingleMessage();
            message.setOffline(true);
            message.setOfflineExpireTime(1000 * 60 * 60 * 24);  // 时间单位为毫秒

            // 获取推送目标
            List<Target> targets = new ArrayList();
            for (UsersDto user : list) {
                if(!StringUtils.isEmpty(user.getClientId())) {

                    template.setAPNInfo(getAPNPayload(pushMessage));
                    message.setData(template);
                    Target target = new Target();
                    target.setAppId(appId);
                    target.setClientId(user.getClientId());
                    targets.add(target);
                    batch.add(message,target);
                }
            }
        }
    }

    private APNPayload getAPNPayload(Message pushMessage) {
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        // 查询该用户所有未读消息数量
//        payload.setAutoBadge("+1");
        // 推送直接带有透传数据
        payload.setContentAvailable(1);
        //ios 12.0 以上可以使用 Dictionary 类型的 sound
//        payload.setSound("default");
//        payload.setCategory("$由客户端定义");
//        payload.addCustomMsg("由客户自定义消息key", "由客户自定义消息value");

        //简单模式APNPayload.SimpleMsg
//        payload.setAlertMsg(new APNPayload.SimpleAlertMsg("hello"));
        //payload.setAlertMsg(getDictionaryAlertMsg());  //字典模式使用APNPayload.DictionaryAlertMsg

        //设置语音播报类型，int类型，0.不可用 1.播放body 2.播放自定义文本
//        payload.setVoicePlayType(2);
        //设置语音播报内容，String类型，非必须参数，用户自定义播放内容，仅在voicePlayMessage=2时生效
        //注：当"定义类型"=2, "定义内容"为空时则忽略不播放
//        payload.setVoicePlayMessage("定义内容");

        // 添加多媒体资源
//        payload.addMultiMedia(new MultiMedia().setResType(MultiMedia.MediaType.pic)
//                .setResUrl("资源文件地址")
//                .setOnlyWifi(true));

        // 设置消息内容
        payload.setAlertMsg(getDictionaryAlertMsg(pushMessage));


        return payload;
    }

    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(Message pushMessage) {
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody(pushMessage.getAppMessage());
//        alertMsg.setActionLocKey("ActionLockey");
//        alertMsg.setLocKey("loc-key1");
//        alertMsg.addLocArg("loc-ary1");
//        alertMsg.setLaunchImage("调用已经在应用程序中绑定的图形文件名");
        // iOS8.2以上版本支持
        alertMsg.setTitle(pushMessage.getTitle());
//        alertMsg.setTitleLocKey("自定义通知标题");
//        alertMsg.addTitleLocArg("自定义通知标题组");
        return alertMsg;
    }
}
