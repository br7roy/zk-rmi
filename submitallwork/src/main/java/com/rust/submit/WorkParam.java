package com.rust.submit;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.Lists;

/**
 * @author Rust
 */
public class WorkParam {

	private String issue_id; //bdId
	private String spent_on; // 时间
	private String hours; // hour
	private String comments; // 评论
	private String activity_id; //
	private String needContinue;// 继续
	private List<BasicNameValuePair> list;


	private WorkParam(WorkParamBuilder builder) {
		list = Lists.newArrayList();

		BasicNameValuePair bnv = new BasicNameValuePair("utf8", "");
		list.add(bnv);
		// String token = WorkSupport.touchToken();
		bnv = new BasicNameValuePair("authenticity_token",
				"mWSqJxFxMvoQ7KbnFlId5Z1B5dFX9rJ3fiZhrSosqRKremQuoceR2KlhsbuX2bsJlBOVbaknFV2b0Lh+4itoBQ==");
		list.add(bnv);
bnv= new BasicNameValuePair("back_url", "http://badao.pinganfu" +
		".net/time_entries?utf8=✓&f[]=spent_on&op[spent_on]=t&f[]=user_id&op[user_id"
		+ "]==&v[user_id][]=me&f[]=&c[]=project&c[]=spent_on&c[]=user&c[]=activity&c[]=issue&c" +
		"[]=comments&c[]=hours");
		list.add(bnv);
		bnv= new BasicNameValuePair("time_entry[project_id]", "");
		list.add(bnv);
		bnv= new BasicNameValuePair("time_entry[issue_id]", builder.issue_id);
		list.add(bnv);
		bnv= new BasicNameValuePair("time_entry[spent_on]", builder.spent_on);
		list.add(bnv);
		bnv= new BasicNameValuePair("time_entry[hours]", builder.hours);
		list.add(bnv);
		bnv= new BasicNameValuePair("time_entry[comments]", builder.comments);
		list.add(bnv);
		bnv= new BasicNameValuePair("time_entry[activity_id]", builder.activity_id);
		list.add(bnv);
		bnv = new BasicNameValuePair("time_entry[continue]", builder.needContinue);
		list.add(bnv);
	}

	public static WorkParamBuilder newBuilder() {
		return new WorkParamBuilder();
	}

	public List<? extends BasicNameValuePair> getList() {
		return list;
	}

	public static class WorkParamBuilder {
		private String issue_id; //bdId
		private String spent_on; // 时间
		private String hours; // hour
		private String comments; // 评论
		private String activity_id; //
		private String needContinue;// 继续

		public WorkParamBuilder addId(String issue_id) {
			this.issue_id = issue_id;
			return this;
		}

		public WorkParamBuilder addSpent_on(String spent_on) {
			this.spent_on = spent_on;
			return this;
		}

		public WorkParamBuilder addHours(String hours) {
			this.hours = hours;
			return this;
		}

		public WorkParamBuilder addComments(String comments) {
			this.comments = comments;
			return this;
		}

		public WorkParamBuilder addActivity_id(String activity_id) {
			this.activity_id = activity_id;
			return this;
		}

		public WorkParamBuilder addNeedContinue(String needContinue) {
			this.needContinue = needContinue;
			return this;
		}



		public WorkParam build() {
			if (StringUtils.isEmpty(issue_id)) {
				throw new WorkException("issueId can not be null");
			}

			if (StringUtils.isEmpty(spent_on)) {
				spent_on = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault()).format(LocalDateTime.now());
			}
			hours=StringUtils.defaultIfBlank(hours, "2");
			comments=StringUtils.defaultIfBlank(comments, "开发");
			activity_id=StringUtils.defaultIfBlank(activity_id, "64");
			needContinue=StringUtils.defaultIfBlank(needContinue, "添加并继续");
			return new WorkParam(this);
		}
	}

}
