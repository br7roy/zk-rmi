package com.rust.submit;

/**
 * @author Rust
 */
public class WorkException extends RuntimeException {
	private static final long serialVersionUID = -8645660990513794177L;

	public WorkException(String issueId_can_not_be_null) {
		super(issueId_can_not_be_null);
	}

	public WorkException(String timerTaskFail, Exception e) {
		super(timerTaskFail, e);
	}
}
