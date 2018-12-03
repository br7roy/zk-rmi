package com.rust.submit.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @author Rust
 */
public class ThreadUtil {

	private static List<ExecutorService> es = new ArrayList<>();
	private static List<ScheduledExecutorService> ses = new ArrayList<>();

	public static ScheduledExecutorService getScheduleEs() {
		ScheduledExecutorService scheduledExecutorService =
				Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("HeartBeatAck-%d").setDaemon(true).setThreadFactory(Thread::new).build());
		ses.add(scheduledExecutorService);
		return scheduledExecutorService;
	}

	public static ExecutorService getEs() {
		ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0,
				TimeUnit.MINUTES, new LinkedBlockingQueue<>(100));
		es.add(executorService);
		return executorService;
	}

	public static void shutdownAll() {
		es.forEach(ExecutorService::shutdownNow);
		ses.forEach(ScheduledExecutorService::shutdownNow);
	}
}
