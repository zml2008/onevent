package com.zachsthings.onevent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

/**
 * A list of event handlers, stored per-event. Based on lahwran's fevents.
 */
public class HandlerList {
	/**
	 * Handler array. This field being an array is the key to this system's
	 * speed.
	 */
	private ListenerRegistration[] handlers = null;

	/**
	 * Dynamic handler lists. These are changed using register() and
	 * unregister() and are automatically baked to the handlers array any time
	 * they have changed.
	 */
	private final EnumMap<Order, ArrayList<ListenerRegistration>> handlerslots;

	/**
	 * List of all HandlerLists which have been created, for use in bakeAll()
	 */
	private static ArrayList<HandlerList> alllists = new ArrayList<HandlerList>();

	/**
	 * Bake all handler lists. Best used just after all normal event
	 * registration is complete, ie just after all plugins are loaded if you're
	 * using fevents in a plugin system.
	 */
	public static void bakeAll() {
		for (HandlerList h : alllists) {
			h.bake();
		}
	}

	public static void unregisterAll() {
		for (HandlerList h : alllists) {
			for (List<ListenerRegistration> regs : h.handlerslots.values()) {
				regs.clear();
			}
			h.handlers = null;
		}
	}

	public static void unregisterAll(Object plugin) {
		for (HandlerList h : alllists) {
			h.unregister(plugin);
		}
	}

	/**
	 * Create a new handler list and initialize using EventPriority The
	 * HandlerList is then added to meta-list for use in bakeAll()
	 */
	public HandlerList() {
		handlerslots = new EnumMap<Order, ArrayList<ListenerRegistration>>(Order.class);
		for (Order o : Order.values()) {
			handlerslots.put(o, new ArrayList<ListenerRegistration>());
		}
		alllists.add(this);
	}

	/**
	 * Register a new listener in this handler list
	 *
	 * @param listener listener to register
	 */
	public void register(ListenerRegistration listener) {
		if (handlerslots.get(listener.getOrder()).contains(listener)) {
			throw new IllegalStateException("This listener is already registered to priority " + listener.getOrder().toString());
		}
		handlers = null;
		handlerslots.get(listener.getOrder()).add(listener);
	}

	public void registerAll(Collection<ListenerRegistration> listeners) {
		for (ListenerRegistration listener : listeners) {
			register(listener);
		}
	}

	/**
	 * Remove a listener from a specific order slot
	 *
	 * @param listener listener to remove
	 */
	public void unregister(ListenerRegistration listener) {
		if (handlerslots.get(listener.getOrder()).contains(listener)) {
			handlers = null;
			handlerslots.get(listener.getOrder()).remove(listener);
		}
	}

	public void unregister(Object owner) {
		boolean changed = false;
		for (List<ListenerRegistration> list : handlerslots.values()) {
			for (ListIterator<ListenerRegistration> i = list.listIterator(); i.hasNext();) {
				if (i.next().getOwner().equals(owner)) {
					i.remove();
					changed = true;
				}
			}
		}
		if (changed) {
			handlers = null;
		}
	}

	/**
	 * Bake HashMap and ArrayLists to 2d array - does nothing if not necessary
	 */
	public void bake() {
		if (handlers != null) {
			return; // don't re-bake when still valid
		}
		List<ListenerRegistration> entries = new ArrayList<ListenerRegistration>();
		for (Entry<Order, ArrayList<ListenerRegistration>> entry : handlerslots.entrySet()) {
			entries.addAll(entry.getValue());
		}
		handlers = entries.toArray(new ListenerRegistration[entries.size()]);
	}

	public ListenerRegistration[] getRegisteredListeners() {
		if (handlers == null) {
			bake();
		}

		return handlers;
	}

	public static HandlerList create() {
		return new HandlerList();
	}
}