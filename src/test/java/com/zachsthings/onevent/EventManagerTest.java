package com.zachsthings.onevent;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Test for the EventManager
 */
public class EventManagerTest {

	@Test
	public void testEventCalling() {
		final TestListener testListener = new TestListener();
		EventManager.registerEvents(testListener, this);
		EventManager.callEvent(new TestEvent());
		assertTrue(testListener.hasBeenCalled());
        HandlerList.unregisterAll();
	}

    @Test
    public void testSubEventCalling() {
        final TestSubListener testListener = new TestSubListener();
        EventManager.registerEvents(testListener, this);
        EventManager.callEvent(new TestEvent());
        EventManager.callEvent(new TestSubEvent());

        assertEquals(2, testListener.getParentCallCount());
        assertEquals(1, testListener.getChildCallCount());

        HandlerList.unregisterAll();
    }

	@Test
	public void testEventPriorities() {
		final List<Order> calledOrders = new ArrayList<Order>();

		for (final Order order : Order.values()) {
			EventManager.registerEvent(TestEvent.class, order, new EventExecutor() {
				public void execute(Event event) throws EventException {
					calledOrders.add(order);
				}
			}, this);
		}
		EventManager.callEvent(new TestEvent());

		assertEquals(calledOrders.size(), Order.values().length);
		for (Order order : Order.values()) {
			assertTrue("Order not contained in results list! ", calledOrders.indexOf(order) >= 0);
			assertEquals(calledOrders.get(order.getIndex()), order);
		}

        HandlerList.unregisterAll();
	}
}
