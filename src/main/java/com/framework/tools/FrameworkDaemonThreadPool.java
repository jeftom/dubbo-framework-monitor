package com.framework.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 通用线程池——守护线程
 * Created by guoyongzheng on 15/3/30.
 */
public final class FrameworkDaemonThreadPool {

    /**
     * cached thread pool : 线程被设置成守护线程，将在主线程结束时被强制关闭
     */
    private static final ExecutorService es = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    /**
     * 私有化构造函数，不许实例化
     */
    private FrameworkDaemonThreadPool() {
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
}
