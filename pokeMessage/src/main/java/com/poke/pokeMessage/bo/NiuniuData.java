package com.poke.pokeMessage.bo;

import com.poke.common.bean.bo.PaiXing;
import lombok.Data;

import java.util.*;

@Data
public class NiuniuData extends RoomData {

    /**
     * 抢庄类型
     */
    private Integer robZhuangType;

    /**
     * 基本分数
     */
    private Integer basePoint;

    /**
     * 规则
     */
    private Integer rule;

    /**
     * 下注
     * 1---可下1，2，3，5倍
     * 2---可下1，3，5，8倍
     */
    private Integer xiazhu;

    /**
     * 特殊
     */
    private Set<Integer> special;


    /**
     * 1---顺子牛，5倍
     * 2---五花牛，6倍
     * 3---同花牛，6倍
     * 4---葫芦牛，7倍
     * 5---炸弹牛，8倍
     * 6---五小牛，10倍
     */
    private Set<Integer> paiXing;

    /**
     * 总局数
     */
    private String totalNum;

    /**
     * 当前局数
     */
    private Integer runingNum;

    /**
     * 房间状态
     */
    private String gameStatus;

    /**
     * 房间里的玩家
     */
    private Set<Integer> players = new HashSet<>();

    /**
     * 真正的玩家
     */
    private Set<Integer> realPlayers = new HashSet<>();

    /**
     * 观众
     */
    private Set<Integer> guanZhongs = new HashSet<>();

    /**
     * 掉线的玩家
     */
    private Set<Integer> disConnections = new HashSet<>();

    /**
     * key为runingNum
     */
    private Map<Integer, Set<Integer>> readyPlayMap = new HashMap<>();

    /**
     * 外层key为runingNum,内层key为玩家id，内层value为玩家的牌
     */
    private Map<Integer, Map<Integer, List<String>>> pokesMap = new HashMap<>();

    /**
     * 外层key为runingNum,内层key为玩家id，内层value为抢庄的倍数
     */
    private Map<Integer, Map<Integer, Integer>> qiangZhuangMap = new HashMap<>();

    /**
     * key为runingNum，value为庄家id
     */
    private Map<Integer, Integer> zhuangJiaMap = new HashMap<>();

    /**
     * 外层key为runingNum,内层key为玩家id，内层value为下注的倍数
     */
    private Map<Integer, Map<Integer, Integer>> xiaZhuMap = new HashMap<>();

    /**
     * 外层key为runingNum,内层key为玩家id，内层value为牌型
     */
    private Map<Integer, Map<Integer, PaiXing>> paiXingMap = new HashMap<>();

    /**
     * key为runingNum,value为摊牌玩家id
     */
    private Map<Integer, Set<Integer>> tanPaiMap = new HashMap<>();

    /**
     * 玩家每一句的分数
     */
    private Map<Integer, Map<Integer, Integer>> runingScoreMap = new HashMap<>();

    /**
     * 玩家总分
     */
    private Map<Integer, Integer> totalScoreMap = new HashMap<>();
}
