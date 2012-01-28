package com.zachsthings.onevent;

public interface EventExecutor {
	public void execute(Event event) throws EventException;
}
