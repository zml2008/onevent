package com.zachsthings.onevent;

/**
 * @author lahwran
 */
public class ListenerRegistration {
    private final EventExecutor executor;
    private final Order orderSlot;
    private final Object owner;

    /**
     * @param executor Listener this registration represents
     * @param orderSlot Order position this registration is in
     * @param owner object that created this registration
     */
    public ListenerRegistration(final EventExecutor executor, final Order orderSlot, final Object owner) {
        this.executor = executor;
        this.orderSlot = orderSlot;
        this.owner = owner;
    }

    /**
     * Gets the listener for this registration
     *
     * @return Registered Listener
     */
    public EventExecutor getExecutor() {
        return executor;
    }

    /**
     * Gets the {@link Object} for this registration
     *
     * @return Registered owner
     */
    public Object getOwner() {
        return owner;
    }

    /**
     * Gets the order slot for this registration
     *
     * @return Registered order
     */
    public Order getOrder() {
        return orderSlot;
    }
}
