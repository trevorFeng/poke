package com.poke.pokeMessage.bo;

import com.poke.common.bean.domain.mysql.User;
import com.poke.pokeMessage.socket.BaseServer;
import com.poke.pokeMessage.socket.socketImpl.NiuniuSocket;
import lombok.Data;

@Data
public class Task {

    /**
     * 房间id
     */
    private Integer roomId;


    /**
     * 游戏的任务标志
     */
    private String flag;


    /**
     * 玩家id
     */
    private Integer playId;

    /**
     * 搶莊的倍數
     */
    private Integer qiangZhuangBeiShu;

    /**
     * 下注的倍數
     */
    private Integer xiaZhuBeiShu;

    /**
     * 总共的倒计时时间
     */
    private Integer totalCountDown;

    /**
     * 牛牛倒计时的标志
     */
    private String niuniuCountDownFg;

    /**
     * 倒计时的时间
     */
    private Integer countDown;

    /**
     * 是否开启好友管理
     */
    private Boolean isFriendManage;

    /**
     * 加入房间的玩家是否是房主的好友
     */
    private Boolean roomAuthFriendAllow;

    private BaseServer socket;

    private User joinUser;



    /**
     * 用户离开的任务
     *
     * @param roomId
     * @param playerId
     * @return
     */
    public static Task getLeave(Integer roomId, Integer playerId) {
        Task task = new Task();
        task.roomId = roomId;
        task.playId = playerId;
        task.flag = TaskFlag.LEAVE;
        return task;
    }

    /**
     * 断开连接的任务
     *
     * @param roomId
     * @param playerId
     * @return
     */
    public static Task getNiuniuDisConnection(Integer roomId, Integer playerId) {
        Task task = new Task();
        task.roomId = roomId;
        task.playId = playerId;
        task.flag = TaskFlag.DIS_CONNECTION;
        return task;
    }


    /**
     * 牛牛加入房间的任务
     *
     * @param roomId
     * @param isFriendManage
     * @param roomAuthFriendAllow
     * @param socket
     * @param joinUser
     * @return
     */
    public static Task getNiuniuJoinRoom(Integer roomId, Boolean isFriendManage, Boolean roomAuthFriendAllow, NiuniuSocket socket, User joinUser) {
        Task task = new Task();
        task.roomId = roomId;
        task.flag = TaskFlag.JOIN_ROOM;
        task.playId = joinUser.getId();
        task.isFriendManage = isFriendManage;
        task.roomAuthFriendAllow = roomAuthFriendAllow;
        task.socket = socket;
        task.joinUser = joinUser;
        return task;
    }

    /**
     * 牛牛准备的任务
     *
     * @param roomId
     * @return
     */
    public static Task getNiuniuReady(Integer roomId, Integer userId) {
        Task task = new Task();
        task.flag = TaskFlag.READY;
        task.roomId = roomId;
        task.playId = userId;
        return task;
    }

    /**
     * @param time
     * @param roomId
     * @return
     */
    public static Task getNiuniuCountDown(Integer time, Integer roomId, String niuniuCountDownFg ,Integer totalCountDown) {
        Task task = new Task();
        task.roomId = roomId;
        task.countDown = time;
        task.flag = TaskFlag.COUNT_DOWN;
        task.niuniuCountDownFg = niuniuCountDownFg;
        task.totalCountDown = totalCountDown;
        return task;
    }


    public static Task getNiuniuFaPai4(Integer roomId) {
        Task task = new Task();
        task.roomId = roomId;
        task.flag = TaskFlag.FA_PAI_4;
        return task;
    }

    public static Task getNiuniuDefaultXiaZhu(Integer roomId) {
        Task task = new Task();
        task.roomId = roomId;
        task.flag = TaskFlag.DEFAULT_XIA_ZHU;
        return task;
    }

    public static Task getNiuniuFaPai1(Integer roomId) {
        Task task = new Task();
        task.roomId = roomId;
        task.flag = TaskFlag.FA_PAI_1;
        return task;
    }

    public static Task getNiuniuQiangZhuang(Integer roomId, Integer userId, Integer qiangZhuangBeiShu) {
        Task task = new Task();
        task.roomId = roomId;
        task.playId = userId;
        task.flag = TaskFlag.QIANG_ZHAUNG;
        task.qiangZhuangBeiShu = qiangZhuangBeiShu;
        return task;
    }

    public static Task getNiuniuSelectZhuangJia(Integer roomId) {
        Task task = new Task();
        task.roomId = roomId;
        task.flag = TaskFlag.SELECT_ZHUANG_JIA;
        return task;
    }

    public static Task getNiuniuXiaZhu(Integer roomId, Integer userId, Integer xiaZhuBeiShu) {
        Task task = new Task();
        task.roomId = roomId;
        task.playId = userId;
        task.flag = TaskFlag.XIA_ZHU;
        task.xiaZhuBeiShu = xiaZhuBeiShu;
        return task;
    }

    public static Task getNiuniuTanPai(Integer roomId, Integer userId) {
        Task task = new Task();
        task.roomId = roomId;
        task.playId = userId;
        task.flag = TaskFlag.TAN_PAI;
        return task;
    }

    public static Task getStopOrContinue(Integer roomId) {
        Task task = new Task();
        task.roomId = roomId;
        task.flag = TaskFlag.STOP_OR_CONTINUE;
        return task;
    }
}
