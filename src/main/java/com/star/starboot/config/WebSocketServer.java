package com.star.starboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * All rights Reserved, Designed By www.xpyvip.top
 *
 * @version V1.0
 * @Package com.star.starboot.config
 * @Description: WebSocketServer
 * @Author: xpy
 * @Date: Created in 2020年12月24日 7:37 下午
 */
@ServerEndpoint("/websocket/{userId}")
@Component
@Slf4j
public class WebSocketServer {
    // 静态变量，记录当前在线连接数
    private static int onlineCount = 0;
    // concurrent包的线程安全set ,用来存放每个客户端对应的WebSocket对象
    private static CopyOnWriteArrayList<WebSocketServer> webSocketSet= new CopyOnWriteArrayList<>();
    // 与客户端的连接对话，通过他 给客户端发送消息
    private Session session;
    private String userId="";

    @OnOpen
    public void onOpen(Session session, @PathParam("userId")String userId){
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount(); // 在线人数加1
        log.info("开始监听："+ userId+",当前在线人数："+getOnlineCount());
        this.userId = userId;
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this); // 从set中删除
        subOnlineCount(); // 在线人数减1
        log.info("有一连接关闭，当前在线人数为："+ getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session){
        log.info("收到来自窗口"+this.userId+"的信息：" + message);
    }

    @OnError
    public void onError(Session session, Throwable error){
        log.error("websocket发生错误");
        error.printStackTrace();
    }

    public static void sendMsg(List<String> tos, String msg){
        for (String to:tos) {
            for (WebSocketServer webSocketServer :webSocketSet) {
                if(to.equals(webSocketServer.userId)){
                    try {
                        webSocketServer.sendMessage(msg);
                    } catch (IOException e) {
                        log.error("websocket发送失败");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount(){
        return onlineCount;
    }
    public static synchronized void addOnlineCount(){
        WebSocketServer.onlineCount++;
    }
    public static synchronized void subOnlineCount(){
        WebSocketServer.onlineCount--;
    }

}
