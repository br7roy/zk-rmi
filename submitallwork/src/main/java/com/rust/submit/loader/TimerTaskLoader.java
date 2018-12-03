package com.rust.submit.loader;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.rust.submit.Constants.CallerTypeEnum;
import com.rust.submit.WorkContext;
import com.rust.submit.WorkContext.LocalWorkRunner;
import com.rust.submit.call.HttpCaller;
import com.rust.submit.support.WorkSupport;
import com.rust.submit.util.DateUtil;
import com.rust.submit.util.ThreadUtil;

import static com.rust.submit.Constants.ScheduleJobTimeEnum.HEART_BEAT;
import static com.rust.submit.Constants.ScheduleJobTimeEnum.SUBMIT_WORK;
import static com.rust.submit.loader.ServiceLoader.ServiceTypeEnm.SCHEDULE;

/**
 * @author Rust
 */
@Slf4j
public class TimerTaskLoader implements AppLoader<String[], WorkContext> {
	private static TimerTaskLoader ONE = new TimerTaskLoader();
	private AtomicInteger cnt = new AtomicInteger(0);
	private WorkContext workContext;

	private TimerTaskLoader() {
	}

	public static void main(String[] args) throws Exception {
		// new TimerTaskLoader().doWork(null);
		// new TimerTaskLoader().setup(null);
	}

	public static TimerTaskLoader getONE() {
		return ONE;
	}

	public WorkContext getWorkContext() {
		return workContext;
	}

	@Override
	public WorkContext doWork(String[] params) throws Exception {
		workContext = setup(params);
		startAllSchedule();
		startHeartBeat();
		cleanup(params);
		return workContext;
	}

	private void cleanup(String[] params) {
		workContext.setWorkStartupTime(DateUtil.now());
		workContext.setSingleSubmitRet(true);
		log.info("Schedule work start up.done. information:" + workContext +
				"input params:" + Arrays.toString(params));
	}

	private WorkContext setup(String[] params) throws Exception {
		// setup first schedule time
		// setup user cookie
		Scanner scanner = new Scanner(new BufferedInputStream(System.in));
		System.out.println("输入用户名");
		String account = scanner.next();
		System.out.println("输入密码");
		String password = scanner.next();
		String cookie = WorkSupport.fetchUserInfo(account,password);
		return new WorkContext(SUBMIT_WORK.firstStartupTime().toInstant(ZoneOffset.of("+8")).toEpochMilli(), params, SUBMIT_WORK.firstStartupTime().toInstant(ZoneOffset.of("+8")).toEpochMilli(), SCHEDULE,cookie);
	}

	private void startAllSchedule() {
		startWorkSubmit();

	}

	private void startWorkSubmit() {
		Timer timer = new Timer();
		prepare(workContext);
		log.info("starting... \r\n" + SUBMIT_WORK + "first start time:" + DateUtil.formatLocalDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(workContext.getFirstScheduleStartupTime()), ZoneId.systemDefault())));
		timer.scheduleAtFixedRate(new LocalWorkScheduler(),
				new Date(workContext.getFirstScheduleStartupTime()),
				SUBMIT_WORK.intervalMilSec());
		workContext.setNextExecTime(workContext.getFirstScheduleStartupTime());
	}

	private void prepare(WorkContext context) {
		// context.setSingleSubmitParam(Constants.ISSUE_ID);
	}

	private void startHeartBeat() {
		workContext.setHeartBeatInterval(HEART_BEAT.intervalMilSec());
		ScheduledExecutorService es = ThreadUtil.getScheduleEs();
		ScheduledFuture<?> scheduledFuture =
				es.scheduleAtFixedRate(touchRunnable(workContext), 0,
						HEART_BEAT.getIntervalNumber(),
						HEART_BEAT.getTimeUnit());
	}

	private Runnable touchRunnable(WorkContext context) {
		return () -> {
			long duration = DateUtil.now() - context.getNextExecTime();
			String pattern =
					"starting...\r\n" + HEART_BEAT + Thread.currentThread().getName() + "," + "exec_count:[%s],next execution " + "interval:[%s]" + HEART_BEAT.getTimeUnit();
			if (duration < 0) {
				long nextTime = HEART_BEAT.getTimeUnit().convert(0 - duration,
						TimeUnit.MILLISECONDS);
				log.info(String.format(pattern, cnt.addAndGet(1), nextTime));
			} else {
				long nextTime = HEART_BEAT.getTimeUnit().convert(duration,
						TimeUnit.MILLISECONDS);
				log.info(String.format(pattern, cnt.addAndGet(1), nextTime));
			}
		};
	}

	class LocalWorkScheduler extends TimerTask {

		LocalWorkScheduler() {
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			String formatNowDate =
					DateUtil.formatLocalDateTime(LocalDateTime.now());
			if (DateUtil.checkDateIfNotNeedSchedule()){
				log.info("current time:" + formatNowDate + "skip submit work");
				return;
			}
			log.info("starting... \r\n" + SUBMIT_WORK + ",current start time " + "at:" + formatNowDate);
			try {
				HttpGet httpGet =
						HttpCaller.DEFAULT.getCallerByName(CallerTypeEnum.QUERY_ISS_LIST);
				CloseableHttpClient httpClient = HttpClients.createDefault();
				HttpResponse response = httpClient.execute(httpGet);
				Object o = HttpCaller.DEFAULT.parseResult(response,
						CallerTypeEnum.QUERY_ISS_LIST);
				workContext.setSingleSubmitList((List<String>) o);
				ServiceLoader.getONE().runningWithTrigger(workContext);
			} catch (Exception e) {
				e.printStackTrace();
				LocalWorkRunner localWorkRunner = new LocalWorkRunner();
				localWorkRunner.setT(e);
				localWorkRunner.setThread(Thread.currentThread());
				workContext.setAnyScheduleWorkError(true);
			}
			log.info("LocalWorkSchedule run end:" + formatNowDate);
			synchronized (WorkContext.watch) {
				workContext.setNextExecTime(workContext.getNextExecTime() + SUBMIT_WORK.intervalMilSec());
			}
		}

	}


}
