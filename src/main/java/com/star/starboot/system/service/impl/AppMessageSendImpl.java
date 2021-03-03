package com.star.starboot.system.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.notify.Notify;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.dto.GtReq;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
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
import java.lang.reflect.Field;
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
public class AppMessageSendImpl implements MessageSend {
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

            // 选择通知模板
            TransmissionTemplate template = new TransmissionTemplate();
            template.setAppId(appId);
            template.setAppkey(appKey);
            // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
            template.setTransmissionType(2);
            template.setTransmissionContent(JSONUtil.toJsonStr(pushMessage));

            // 第三方厂商推送
            template.set3rdNotifyInfo(get3rdNotifyInfo(pushMessage.getTitle(),pushMessage.getAppMessage(),pushMessage));
            // ios消息推送
            template.setAPNInfo(getAPNPayload(pushMessage.getTitle(),pushMessage.getAppMessage(),"1",pushMessage));

            SingleMessage message = new SingleMessage();
            message.setStrategyJson("{\"default\":4,\"ios\":4,\"st\":4}");
            message.setOffline(true);
            message.setOfflineExpireTime(1000 * 60 * 60 * 24);  // 时间单位为毫秒
            message.setData(template);

            // 获取推送目标
            List<Target> targets = new ArrayList();
            for (UsersDto user : list) {
                if(!StringUtils.isEmpty(user.getClientId())) {
                    Target target = new Target();
                    target.setAppId(appId);
                    target.setClientId(user.getClientId());
                    targets.add(target);
                    batch.add(message,target);
                }
            }
        }
    }

    /**
     * 第三方厂商通知
     *
     * @param title   标题
     * @param content 正文
     * @param payload 附带属性
     * @return
     */
    private static Notify get3rdNotifyInfo(String title, String content, Message payload) {
        String intent = "intent:#Intent;launchFlags=0x04000000;action=android.intent.action.oppopush;package=com.qcnt.eps;component=com.qcnt.eps/io.dcloud.PandoraEntry;S.UP-OL-SU=true;S.title=" + title + ";S.content=" + content + ";S.payload="+ JSON.toJSONString(payload) +";end";
        Notify notify = new Notify();
        notify.setTitle(title);
        notify.setContent(content);
        notify.setType(GtReq.NotifyInfo.Type._intent);
        notify.setIntent(intent);
        notify.setPayload(JSON.toJSONString(payload));
        return notify;
    }

    /**
     * IOS的APNs消息
     *
     * @param title
     * @param body
     * @param badge
     * @param pushMessage
     * @return
     */
    private static APNPayload getAPNPayload(String title, String body, String badge, Message pushMessage) {
        APNPayload payload = new APNPayload();
        // 在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        if (badge != null && badge.trim().length() > 0) {
            payload.setAutoBadge(badge);
        }
        // 需要设置为0，否则前端receive事件会进入两次
        payload.setContentAvailable(0);
        // ios 12.0 以上可以使用 Dictionary 类型的 sound
        payload.setSound("default");
        if (pushMessage != null) {
            Class cls = pushMessage.getClass();
            Field[] fields = cls.getDeclaredFields();
            for(int i=0; i<fields.length; i++){
                Field f = fields[i];
                f.setAccessible(true);
                try {
                    payload.addCustomMsg(f.getName(), f.get(pushMessage));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        payload.setAlertMsg(getDictionaryAlertMsg(title, pushMessage.getSecondTitle(), body)); // 字典模式使用APNPayload.DictionaryAlertMsg

//      // 设置语音播报类型，int类型，0.不可用 1.播放body 2.播放自定义文本
//      payload.setVoicePlayType(2);
//      // 设置语音播报内容，String类型，非必须参数，用户自定义播放内容，仅在voicePlayMessage=2时生效
//      // 注：当"定义类型"=2, "定义内容"为空时则忽略不播放
//      payload.setVoicePlayMessage("定义内容");
//
//      // 添加多媒体资源
//      payload.addMultiMedia(new MultiMedia().setResType(MultiMedia.MediaType.pic).setResUrl("资源文件地址").setOnlyWifi(true));
        return payload;
    }

    /**
     * IOS通知提示样式
     *
     * @param title
     * @param body
     * @return
     */
    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(String title, String secondTitle, String body) {
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody(body);
//      alertMsg.setActionLocKey("显示关闭和查看两个按钮的消息");
//      alertMsg.setLocKey("loc-key1");
//      alertMsg.addLocArg("loc-ary1");
//      alertMsg.setLaunchImage("调用已经在应用程序中绑定的图形文件名");
        // iOS8.2以上版本支持
        alertMsg.setSubtitle(secondTitle);
        alertMsg.setTitle(title);
//      alertMsg.setTitleLocKey("自定义通知标题");
//      alertMsg.addTitleLocArg("自定义通知标题组");
        return alertMsg;
    }
}
