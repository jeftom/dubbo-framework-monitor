package com.nfsq.framework.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 计时秒表类
 * Created by guoyongzheng on 14-12-16.
 */
public class CommonStopWatch {

    /**
     * 时间戳（ns）
     */
    private long start;

    /**
     * 保存暂停记录
     */
    private long pause;

    /**
     * 保存上一个watch的时间信息
     */
    private long lastWatchTime;

    /**
     * 观察记录
     */
    private List<CommonPair<Long, String>> watches;

    /**
     * 构造函数
     */
    public CommonStopWatch() {
        start();
    }

    /**
     * 插入一个中间观察点，不会重置此秒表
     * @return
     */
    public String addWatch() {
        return addWatch(null);
    }

    /**
     * 插入一个中间观察点，不会重置此秒表
     *
     * @return
     */
    public String addWatch(String desc) {
        long now = System.nanoTime();
        //long elapsedTime = now - this.start;
        long elapsedTime = now - this.lastWatchTime;
        String str = desc == null ? "Step " + String.valueOf(this.watches.size()) + " : " : desc + " : ";
        this.watches.add(new CommonPair<>(elapsedTime, str));
        this.lastWatchTime = now;
        return str + format(elapsedTime);
    }

    /**
     * 计时开始
     *
     * @return
     */
    private long start() {
        this.start = System.nanoTime();
        this.lastWatchTime = this.start;
        this.watches = new ArrayList<>();
        return this.start;
    }

    /**
     * 计时
     * @return
     */
    private long elapsedTime() {
        long now = System.nanoTime();
        return now - this.start;
    }

    /**
     * 格式化输出
     * @param elapsedTime
     * @return
     */
    private String format(long elapsedTime) {
        return String.format("%,d ns", elapsedTime);
    }

    /**
     * 暂停计时
     * @return
     */
    public long pause() {
        this.pause = elapsedTime();
        System.out.println("暂停 at : " + System.nanoTime() + ", 时间段已暂存 : " + format(this.pause));
        return this.pause;
    }

    /**
     * 暂停之后重新开始计时
     * @return
     */
    public long restart() {
        long now = System.nanoTime();
        this.start = now - this.pause;
        this.lastWatchTime = this.lastWatchTime + this.pause;
        System.out.println("重新开始 at : " + now + ", 前存时间段为 : " + format(this.pause));
        this.pause = 0L;
        return this.start;
    }

    /**
     * 打印秒表数据
     */
    public long print() {
        //如果记时还在pause当中，需要restart一下才是准的。
        if (this.pause > 0) {
            restart();
        }

        for (CommonPair<Long, String> watch : this.watches) {
           System.out.println(watch.getSecond() + format(watch.getFirst()));
        }
        long now = System.nanoTime();
        long result = now - this.start;
        System.out.println("********* 总耗时 ********* : " + format(result));

        return result;
    }
}
