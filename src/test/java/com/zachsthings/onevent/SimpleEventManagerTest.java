package com.zachsthings.onevent;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author zml2008
 */
public class SimpleEventManagerTest {
	protected EventManager eventManager;
    protected static final Logger logger = Logger.getLogger(SimpleEventManagerTest.class.getCanonicalName());

	@Before
	public void setUp() {
		eventManager = new SimpleEventManager(logger);
	}

	@Test
	public void testEventCalling() {
		final TestListener testListener = new TestListener();
		eventManager.registerEvents(testListener, this);
		eventManager.callEvent(new TestEvent());
		assertTrue(testListener.hasBeenCalled());
	}
	
	@Test
	public void testEventPriorities() {
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
	}
}
