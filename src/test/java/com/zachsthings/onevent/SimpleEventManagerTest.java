package com.zachsthings.onevent;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * Test for SimpleEventManager
 */
public class SimpleEventManagerTest {
    protected static final Logger LOGGER = Logger.getLogger(SimpleEventManagerTest.class.getCanonicalName());

	@Test
	public void testEventCalling() {
        final SimpleEventManager eventManager = new SimpleEventManager(LOGGER);
		final TestListener testListener = new TestListener();
		eventManager.registerEvents(testListener, this);
		eventManager.callEvent(new TestEvent());
		assertTrue(testListener.hasBeenCalled());
        HandlerList.unregisterAll();
	}

    @Test
    public void testSubEventCalling() {
        final EventManager eventManager = new SimpleEventManager(LOGGER);
        final TestSubListener testListener = new TestSubListener();
        eventManager.registerEvents(testListener, this);
        eventManager.callEvent(new TestEvent());
        eventManager.callEvent(new TestSubEvent());

        assertEquals(2, testListener.getParentCallCount());
        assertEquals(1, testListener.getChildCallCount());

        HandlerList.unregisterAll();
    }

	@Test
	public void testEventPriorities() {
        final EventManager eventManager = new SimpleEventManager(LOGGER);
		final List<Order> calledOrders = new ArrayList<Order>();

		for (final Order order : Order.values()) {
			eventManager.registerEvent(TestEvent.class, order, new EventExecutor() {
				public void execute(Event event) throws EventException {
					calledOrders.add(order);
				}
			}, this);
		}
		eventManager.callEvent(new TestEvent());

		assertEquals(calledOrders.size(), Order.values().length);
		for (Order order : Order.values()) {
			assertTrue("Order not contained in results list! ", calledOrders.indexOf(order) >= 0);
			assertEquals(calledOrders.get(order.getIndex()), order);
		}

        HandlerList.unregisterAll();
	}
}
