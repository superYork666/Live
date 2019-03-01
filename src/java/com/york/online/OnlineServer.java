/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.york.online;

import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author as
 */
@ServerEndpoint("/onlineServer")
public class OnlineServer {
    private Session session;//当前的会话对象 通过session 服务器向客户端发送消息
    //一个静态的集合存储所有客户端实例
    public static Vector<OnlineServer> clients = new Vector<OnlineServer>();//Vector因为有synchronized保证线程安全
    
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        clients.add(this);
        System.out.println("A new connect");
    }
    @OnClose
    public void onClose(){
        clients.remove(this);
        System.out.println("A connect!");
    }
    @OnMessage
    public void onMessage(String message, Session session){
        //群发消息
        for(OnlineServer clients : clients){
            try {
                clients.session.getBasicRemote().sendText(message);
            } catch (IOException ex) {
                System.out.println("Wrong in sending message");
            }
        }
    }
}
