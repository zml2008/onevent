package com.zachsthings.onevent;

/**
 * Event created to test child handler lists
 */
public class TestSubEvent extends TestEvent {
    private static final HandlerList HANDLERS = new HandlerList(TestEvent.getHandlerList());

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
