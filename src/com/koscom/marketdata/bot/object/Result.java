package com.koscom.marketdata.bot.object;

public class Result<T> extends DefaultObject {

	private boolean ok;
	
	private T result;

	/**
	 * @param ok
	 * @param result
	 */
	public Result(boolean ok, T result) {
		super();
		this.ok = ok;
		this.result = result;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
}
