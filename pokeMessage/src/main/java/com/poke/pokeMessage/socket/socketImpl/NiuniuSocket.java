package com.poke.pokeMessage.socket.socketImpl;

import com.poke.common.bean.bo.SocketResult;
import com.poke.common.bean.bo.WebKeys;
import com.poke.common.bean.domain.mysql.Room;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.bean.exception.BizException;
import com.poke.common.util.JsonUtil;
import com.poke.common.util.TokenUtil;
import com.poke.pokeMessage.bo.SocketMessage;
import com.poke.pokeMessage.bo.Task;
import com.poke.pokeMessage.socket.BaseServer;
import com.poke.pokeMessage.socket.decoder.NiuniuDecoder;
import com.poke.pokeMessage.socket.encoder.NiuniuEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 一句话描述该类作用:【牛牛服务端,每次建立链接就新建了一个对象】
 *
 * @author: trevor
 * @create: 2019-03-05 22:29
 **/
@ServerEndpoint(
        value = "/niuniu/{roomId}",
        encoders = {NiuniuEncoder.class},
        decoders = {NiuniuDecoder.class}
)
@Component
@Slf4j
public class NiuniuSocket extends BaseServer {

    public Session session;

    public Integer userId;

    public Integer roomId;

    /**
     * 连接建立成功调用的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") Integer roomId) {
        //roomId合法性检查
        Room room = roomDbClient.findByRoomId(roomId).getData();
        if (room == null) {
            directSendMessage(new SocketResult(507), session);
            close(session);
            return;
        }
        //是否激活,0为未激活,1为激活，2为房间使用完成后关闭，3为房间未使用关闭
        if (!Objects.equals(room.getStatus(), 0) && !Objects.equals(room.getStatus(), 1)) {
            directSendMessage(new SocketResult(506), session);
            close(session);
            return;
        }
        //token合法性检查
        List<String> params = session.getRequestParameterMap().get(WebKeys.TOKEN);
        if (params == null || params.isEmpty()) {
            directSendMessage(new SocketResult(400), session);
            close(session);
            return;
        }
        String token = session.getRequestParameterMap().get(WebKeys.TOKEN).get(0);

        User user = null;
        try {
            user = getUserByToken(token);
        }catch (Exception e) {
            log.error(e.toString());
            directSendMessage(new SocketResult(404), session);
            return;
        }
        if (user == null) {
            directSendMessage(new SocketResult(404), session);
            close(session);
            return;
        }
        //设置最大不活跃时间
        session.setMaxIdleTimeout(1000 * 60 * 45);
        this.roomId = roomId;
        this.userId = user.getId();
        this.session = session;

        Boolean isFriendManage = null;
        Boolean roomAuthFriendAllow = null;
        try {
            //是否开通好友管理功能
            isFriendManage = userDbClient.findByUserId(room.getRoomAuth()).getData().getFriendManageFlag() == 1;
            //玩家是否是房主的好友
            roomAuthFriendAllow = friendsManageDbClient.countFriendByUserIdAndManageId(room.getRoomAuth(), user.getId()).getData();
        }catch (Exception e) {
            log.error(e.toString());
            directSendMessage(new SocketResult(404), session);
            return;
        }
        if (isFriendManage == null || roomAuthFriendAllow == null) {
            directSendMessage(new SocketResult(404), session);
            close(session);
            return;
        }
        Task task = Task.getNiuniuJoinRoom(roomId, isFriendManage, roomAuthFriendAllow, this, user);
        taskQueue.addTask(roomId, task);
    }

    /**
     * 接受用户消息
     */
    @OnMessage
    public void onMessage(SocketMessage socketMessage) {
        if (Objects.equals(socketMessage.getMessageCode(), 1)) {
            niuniuService.dealReadyMessage(roomId, this);
        } else if (Objects.equals(socketMessage.getMessageCode(), 2)) {
            niuniuService.dealQiangZhuangMessage(roomId, this, socketMessage);
        } else if (Objects.equals(socketMessage.getMessageCode(), 3)) {
            niuniuService.dealXiaZhuMessage(roomId, this, socketMessage);
        } else if (Objects.equals(socketMessage.getMessageCode(), 4)) {
            niuniuService.dealTanPaiMessage(roomId, this);
//        }else if (Objects.equals(socketMessage.getMessageCode() ,5)) {
//            playService.dealShuoHuaMessage(roomId ,this ,socketMessage);
//        }else if (Objects.equals(socketMessage.getMessageCode() ,6)) {
//            playService.dealChangeToGuanZhan(roomId ,this);
//        }
        }
    }

    /**
     * 关闭连接调用的方法
     */
    @OnClose
    public void onClose() {
        if (userId == null) {
            gameCore.removeMessageQueue(userId);
            Task task = Task.getNiuniuDisConnection(roomId, userId);
            taskQueue.addTask(roomId, task);
        }
    }

    /**
     * 发生错误时调用的方法
     */
    @OnError
    public void onError(Throwable t) {
        log.error(t.getMessage(), t);
    }

    /**
     * 向客户端发消息
     *
     * @param pack
     */
    public void sendMessage(SocketResult pack) {
        gameCore.addSocketMessage(userId , JsonUtil.toJsonString(pack));
    }

    /**
     * 向客户端发消息
     *
     * @param pack
     */
    public void directSendMessage(SocketResult pack, Session s) {
        RemoteEndpoint.Async async = s.getAsyncRemote();
        if (s.isOpen()) {
            async.sendObject(pack);
        } else {
            close(s);
        }
    }

    /**
     * 向客户端刷消息
     */
    public void flush() {
        try {
            List<String> messages = gameCore.getFiveMessage(userId);
            if (!messages.isEmpty()) {
                StringBuffer stringBuffer = new StringBuffer("[");
                for (String mess : messages) {
                    stringBuffer.append(mess).append(",");
                }
                stringBuffer.setLength(stringBuffer.length() - 1);
                stringBuffer.append("]");
                synchronized (session) {
                    RemoteEndpoint.Async async = session.getAsyncRemote();
                    if (session.isOpen()) {
                        async.sendText(stringBuffer.toString());
                    } else {
                        close(session);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 关闭连接
     *
     * @param session
     */
    public void close(Session session) {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                log.error("close", e.getMessage(), e);
            }
        }
    }

    public void stop() {
        flush();
        //关闭连接
        close(session);
    }


    private User getUserByToken(String token) {
        if (token == null) {
            throw new BizException(-1 ,"未通过身份校验");
        }
        Map<String, Object> claims = TokenUtil.getClaimsFromToken(token);
        String openid = (String) claims.get(WebKeys.OPEN_ID);
        String userid = (String) claims.get("userid");
        if (openid == null || userid == null) {
            throw new BizException(-1 ,"token不合法");
        }
        User user = userDbClient.findByOpenidAndUserId(openid ,Integer.valueOf(userid)).getData();
        return user;
    }

}
