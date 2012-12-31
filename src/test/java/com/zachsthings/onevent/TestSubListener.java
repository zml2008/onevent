package com.zachsthings.onevent;

/**
 * Test listener for parent-child handler lists
 */
public class TestSubListener implements Listener {
    private int parentCallCount = 0;
    private int childCallCount = 0;

    @EventHandler
    public void onTestEvent(TestEvent event) {
        parentCallCount++;
    }

    @EventHandler
    public void onTestSubEvent(TestSubEvent event) {
        childCallCount++;
    }

    public int getParentCallCount() {
        return parentCallCount;
    }

    public int getChildCallCount() {
        return childCallCount;
    }
}
