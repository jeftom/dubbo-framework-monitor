package com.nfsq.framework.tools;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * 通用线程池——非守护线程
 * Created by guoyongzheng on 15/3/30.
 */
public final class FrameworkThreadPool {

    /**
     * thread pool : 线程被设置成非守护线程，主线程结束时未完成任务理论上还会继续运行
     */
    //private static final ExecutorService es = Executors.newFixedThreadPool(5);
    private static final ExecutorService es = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    /**
     * 私有化构造函数，不许实例化
     */
    private FrameworkThreadPool() {
        // do nothing
    }

    /**
     * 执行
     *
     * @param r
     */
    public static void execute(Runnable r) {
        es.execute(r);
    }

    /**
     * 执行
     * @param c
     * @param <T>
     * @return
     */
    public static <T> Future<T> submit(Callable<T> c) { return es.submit(c);}

}
