package com.rust.submit;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Objects;
import com.rust.submit.loader.ServiceLoader.ServiceTypeEnm;

/**
 * @author Rust
 */
public class WorkContext {
	public static final Object watch = new Object();

	private final long firstScheduleStartupTime;
	private final String[] params;
	private long nextExecTime;
	private long heartBeatInterval;
	private long scheduleWorkInterval;
	private long workStartupTime;
	private boolean singleSubmitRet;
	private SingleSubmitParam singleSubmitParam;
	private List<String> singleSubmitList;
	private boolean anyScheduleWorkError;
	private ServiceTypeEnm serviceTypeEnm;
	private transient String cookie;


	public WorkContext(long firstScheduleStartupTime, String[] params,
					   ServiceTypeEnm typeEnm,String cookie) {
		this.firstScheduleStartupTime = firstScheduleStartupTime;
		this.params = params;
		this.serviceTypeEnm = typeEnm;
		this.cookie = cookie;
	}

	public WorkContext(long firstScheduleStartupTime, String[] params,
					   long nextExecTime,ServiceTypeEnm typeEnm,String cookie) {
		this.firstScheduleStartupTime = firstScheduleStartupTime;
		this.params = params;
		this.nextExecTime = nextExecTime;
		this.serviceTypeEnm = typeEnm;
		this.cookie = cookie;
	}


	public ServiceTypeEnm getServiceTypeEnm() {
		return serviceTypeEnm;
	}

	public void setServiceTypeEnm(ServiceTypeEnm serviceTypeEnm) {
		this.serviceTypeEnm = serviceTypeEnm;
	}

	public long getFirstScheduleStartupTime() {
		return firstScheduleStartupTime;
	}

	public long getNextExecTime() {
		return nextExecTime;
	}

	public void setNextExecTime(long nextExecTime) {
		this.nextExecTime = nextExecTime;
	}

	public boolean isAnyScheduleWorkError() {
		return anyScheduleWorkError;
	}

	public void setAnyScheduleWorkError(boolean anyScheduleWorkError) {
		this.anyScheduleWorkError = anyScheduleWorkError;
	}

	public long getHeartBeatInterval() {
		return heartBeatInterval;
	}

	public void setHeartBeatInterval(long heartBeatInterval) {
		this.heartBeatInterval = heartBeatInterval;
	}

	public List<String> getSingleSubmitList() {
		return singleSubmitList;
	}

	public void setSingleSubmitList(List<String> singleSubmitList) {
		this.singleSubmitList = singleSubmitList;
	}

	public long getScheduleWorkInterval() {
		return scheduleWorkInterval;
	}

	public void setScheduleWorkInterval(long scheduleWorkInterval) {
		this.scheduleWorkInterval = scheduleWorkInterval;
	}

	public long getWorkStartupTime() {
		return workStartupTime;
	}

	public void setWorkStartupTime(long workStartupTime) {
		this.workStartupTime = workStartupTime;
	}

	public String[] getParams() {
		return params;
	}

	public boolean isSingleSubmitRet() {
		return singleSubmitRet;
	}

	public void setSingleSubmitRet(boolean singleSubmitRet) {
		this.singleSubmitRet = singleSubmitRet;
	}

	public SingleSubmitParam getSingleSubmitParam() {
		return singleSubmitParam;
	}

	public void setSingleSubmitParam(SingleSubmitParam singleSubmitParam) {
		this.singleSubmitParam = singleSubmitParam;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("firstScheduleStartupTime",
				firstScheduleStartupTime).add("params",
				Arrays.toString(params)).add("nextExecTime", nextExecTime).add("heartBeatInterval", heartBeatInterval).add("scheduleWorkInterval", scheduleWorkInterval).add("workStartupTime", workStartupTime).add("singleSubmitRet", singleSubmitRet).add("singleSubmitParam", singleSubmitParam).toString();
	}

	public static class SingleSubmitParam {

		private String issId;
		private String  spendOn;
		private String hour;

		public String getIssId() {
			return issId;
		}

		public void setIssId(String issId) {
			this.issId = issId;
		}

		public String getSpendOn() {
			return spendOn;
		}

		public void setSpendOn(String spendOn) {
			this.spendOn = spendOn;
		}

		public String getHour() {
			return hour;
		}

		public void setHour(String hour) {
			this.hour = hour;
		}
	}

	public static class LocalWorkRunner {
		private  Throwable t;
		private Thread thread;

		public void setT(Throwable t) {
			this.t = t;
		}

		public void setThread(Thread thread) {
			this.thread = thread;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof LocalWorkRunner))
				return false;
			LocalWorkRunner that = (LocalWorkRunner) o;
			return java.util.Objects.equals(t, that.t) && java.util.Objects.equals(thread, that.thread);
		}

		@Override
		public int hashCode() {
			return java.util.Objects.hash(t, thread);
		}
	}

}
