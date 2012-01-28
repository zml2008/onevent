package com.zachsthings.onevent;

public enum Result {
	/**
	 * Deny the event. Depending on the event, the action indicated by the event
	 * will either not take place or will be reverted. Some actions may not be
	 * denied.
	 */
	DENY(false),
	/**
	 * Neither deny nor allow the event. The server will proceed with its normal
	 * handling.
	 */
	DEFAULT(null),
	/**
	 * Allow / Force the event. The action indicated by the event will take
	 * place if possible, even if the server would not normally allow the
	 * action. Some actions may not be allowed.
	 */
	ALLOW(true);

	private Boolean result;

	private Result(Boolean result) {
		this.result = result;
	}

	public Boolean getResult() {
		return result;
	}
}
