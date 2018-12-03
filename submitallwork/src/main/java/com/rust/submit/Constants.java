/**
 * 壹钱包 Copyright (c) 2013-2018 壹钱包版权所有.
 */
package com.rust.submit;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.rust.submit.loader.ServiceLoader;
import com.rust.submit.loader.ServiceLoader.ServiceTypeEnm;

/**
 * @author Rust
 */
public interface Constants {

	String ISSUE_ID = "135353";
	// 切分的每次最小的小时数
	int SPLIT_LIMIT_HOURS = 2; // submit hour every time


	enum ScheduleJobTimeEnum {

		// 提交工时
		SUBMIT_WORK(1, TimeUnit.DAYS, "提交工时") {
			@Override
			public long intervalMilSec() {

				return ScheduleJobTimeEnum.SUBMIT_WORK.timeUnit.toMillis(ScheduleJobTimeEnum.SUBMIT_WORK.intervalNumber);
			}

			@Override
			public LocalDateTime firstStartupTime() {
				return LocalDateTime.now(Clock.systemUTC()).with(ChronoField.HOUR_OF_DAY, 21).with(ChronoField.MINUTE_OF_HOUR, 30);
			}
		},

		// 心跳
		HEART_BEAT(27, TimeUnit.MINUTES, "心跳") {
			@Override
			public long intervalMilSec() {
				return ScheduleJobTimeEnum.HEART_BEAT.timeUnit.toMillis(ScheduleJobTimeEnum.HEART_BEAT.intervalNumber);
			}

			@Override
			public LocalDateTime firstStartupTime() {
				return LocalDateTime.now();
			}
		},
		;
		private final int intervalNumber; // 间隔
		private final TimeUnit timeUnit; // 间隔时间类型
		private final String desc;

		ScheduleJobTimeEnum(int intervalNumber, TimeUnit timeUnit, String desc) {
			this.intervalNumber = intervalNumber;
			this.timeUnit = timeUnit;
			this.desc = desc;
		}

		public int getIntervalNumber() {
			return intervalNumber;
		}

		public TimeUnit getTimeUnit() {
			return timeUnit;
		}

		public String getDesc() {
			return desc;
		}

		// 毫秒级别的间隔时间
		public abstract long intervalMilSec();

		// 第一次启动的指定时间
		public abstract LocalDateTime firstStartupTime();

		@Override
		public String toString() {
			return "[" + getDesc() + "]";
		}
	}

	Constants DEFAULT = new ConstantsImpl();
	Map<String, String> MAP = Maps.newHashMap();

	static ServiceLoader.ServiceTypeEnm matchEnum(String type) {
		Preconditions.checkNotNull(type, "type can not be null");
		if ("1".equals(type)) {
			return ServiceTypeEnm.SCHEDULE;
		} else if ("2".equals(type)) {
			return ServiceTypeEnm.SINGLE;
		} else {
			throw new WorkException("type is illegal");
		}
	}

	void initMap();

	default String getVal(String arg) {
		return MAP.get(arg);
	}
	class ConstantsImpl implements Constants {
		@Override
		public void initMap() {
			MAP.put("9", "开发编码");
			MAP.put("40", "团队工作");
			MAP.put("38", "发布支持");
			MAP.put("39", "产线事件");
			MAP.put("60", "需求分析");
			MAP.put("61", "评审会议");
			MAP.put("62", "详细设计");
			MAP.put("63", "文档撰写");
			MAP.put("64", "开发自测");
			MAP.put("65", "联调测试");
			MAP.put("66", "性能优化");
			MAP.put("67", "数据分析");
			MAP.put("68", "项目管理");
			MAP.put("69", "技术调研");
			MAP.put("16", "测试-评审");
			MAP.put("17", "测试-测试设计");
			MAP.put("18", "测试-冒烟测试");
			MAP.put("20", "测试-系统测试");
			MAP.put("19", "测试-回归测试");
			MAP.put("21", "测试-绿灯测试");
			MAP.put("22", "测试-性能测试");
			MAP.put("25", "测试-专题测试");
			MAP.put("23", "测试-测试开发");
			MAP.put("122", "测试-效能提升");
		}
	}


	 enum CallerTypeEnum{
	//	这个类列举了所有支持的查询类型

		// 查询列表
		QUERY_ISS_LIST();

		CallerTypeEnum() {
		}


	}

	enum Vip{
		VIP("10.28.11.159","autologin=09d7c2e22e061cc160ff4c6b3e257611c27459f5; sidebar_hide=hide; " +
				"_redmine_session" +
				"=UVFDNkZhTnBEZGZUWlFjeEZPVHlrbDJPc0dlQ25vTjA1WVhhWHpXZEcwY3EwbEJ2TGVubjVVbUlUd2xYYzA3TEwySUlsN3pQKzRDMVZ3Q1pzeVFnSmxDZ3ZkYnNhaFFKb3ZoNDFSamFaMHd2WW5nNUJCSHBBZS9JOGVYdTQ0VGowYWk3b0ZzbUNMN2dQNXJHSHJhQ2JGVHRlTE5EdDNHSXJHZ2ZEVHVIbmdieFZkYm05dkdHRmJWMEpGQU1UQUd6YjFONnMvYmdKaTN0bDJ6ZUxiUGJKcWQ5dGxLanNJVTlaWnVmc0xWTFltUGpWVXJVdVRJYVJYT2gwam1IeDBwNjZiRjJ0VXdWemhmdEt3aWIwTGZhYm0weWdEMU5VMUNZSDVkdEdNWjMvUzZVL01CaExYK0xVRERORG9hQzVNQkQ3RXFhOXQ4SG1GNVNLc1Y1K2JKa3Z4cnNZTTZESzZOQ2JvOG9WRVNva1JnVjBWRmVjUW4wcmhpb3pQeVUrTFVwTVdkM0Q4YjlnNThLaG83WFF3djQ5cG94Zm0rZVBlakpiUWJHa09oV2hZZmZMYUV3UGtEdDZPcHlmYW1JZndSNTJtaENYWkR0d0NsUkt4cEtQVkRrNlpvMVI0Si9IeUVQTmhWaStadXMybTdwcWFKcktrVlhLWnlNMzNFSjdxR0dYdy9pazBGSWxlcE9XVzJwbS9DUlp3c3oyUlc3VWI0eXdTR3Jwd0ZBaWlzUVEwanNYZnJZZ2xCS216NXZqWXVyOUxDZjZrcFZWdTlFQzF6RjJkWS9SQT09LS1zbTQ1QjlqMnhUMXNuOUxZenVCdm9RPT0%3D--d5f6e2c06f5b5a11c6c1f594e671e5609555facd") ,;
		public final String ip;
		public final String cookie;

		Vip(String ip, String cookie) {
			this.ip = ip;
			this.cookie = cookie;
		}
	}


}
