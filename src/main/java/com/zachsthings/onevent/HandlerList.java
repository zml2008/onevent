/*
 * Copyright 2012 zml2008
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zachsthings.onevent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

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
    private final EnumMap<Order, ArrayList<ListenerRegistration>> handlerSlots;

    private final CopyOnWriteArrayList<HandlerList> children = new CopyOnWriteArrayList<HandlerList>(); // Not modified that much, it's fine
    private final HandlerList parent;

    /**
     * List of all HandlerLists which have been created, for use in bakeAll()
     */
    private static final ArrayList<HandlerList> ALL_LISTS = new ArrayList<HandlerList>();

    /**
     * Bake all handler lists. Best used just after all normal event
     * registration is complete, ie just after all plugins are loaded if you're
     * using fevents in a plugin system.
     */
    public static void bakeAll() {
        for (HandlerList h : ALL_LISTS) {
            h.bake();
        }
    }

    public static void unregisterAll() {
        for (HandlerList h : ALL_LISTS) {
            for (List<ListenerRegistration> regs : h.handlerSlots.values()) {
                regs.clear();
            }
            h.dirty();
        }
    }

    public static void unregisterAll(Object plugin) {
        for (HandlerList h : ALL_LISTS) {
            h.unregister(plugin);
        }
    }

    public HandlerList() {
        this(null);
    }

    /**
     * Create a new handler list and initialize using EventPriority The
     * HandlerList is then added to meta-list for use in bakeAll()
     */
    public HandlerList(HandlerList parent) {
        handlerSlots = new EnumMap<Order, ArrayList<ListenerRegistration>>(Order.class);
        for (Order o : Order.values()) {
            handlerSlots.put(o, new ArrayList<ListenerRegistration>());
        }
        ALL_LISTS.add(this);

        this.parent = parent;
        if (parent != null) {
            parent.addChild(this);
        }
    }


    /**
     * Register a new listener in this handler list
     *
     * @param listener listener to register
     */
    public void register(ListenerRegistration listener) {
        if (handlerSlots.get(listener.getOrder()).contains(listener)) {
            throw new IllegalStateException("This listener is already registered to priority " + listener.getOrder().toString());
        }
        dirty();
        handlerSlots.get(listener.getOrder()).add(listener);
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
        if (handlerSlots.get(listener.getOrder()).contains(listener)) {
            dirty();
            handlerSlots.get(listener.getOrder()).remove(listener);
        }
    }

    public void unregister(Object owner) {
        boolean changed = false;
        for (List<ListenerRegistration> list : handlerSlots.values()) {
            for (ListIterator<ListenerRegistration> i = list.listIterator(); i.hasNext();) {
                if (i.next().getOwner().equals(owner)) {
                    i.remove();
                    changed = true;
                }
            }
        }
        if (changed) {
            dirty();
        }
    }

    /**
     * Bake HashMap and ArrayLists to 2d array - does nothing if not necessary
     */
    public ListenerRegistration[] bake() {
        ListenerRegistration[] handlers = this.handlers;
        if (handlers != null) {
            return handlers; // don't re-bake when still valid
        }
        List<ListenerRegistration> entries = new ArrayList<ListenerRegistration>();
        for (Order order : Order.values()) {
            addToEntries(entries, order);
        }
        this.handlers = handlers = entries.toArray(new ListenerRegistration[entries.size()]);
        return handlers;
    }

    private void dirty() {
        this.handlers = null;
        if (children.size() > 0) {
            for (int i = 0; i < children.size(); ++i) {
                children.get(i).dirty();
            }
        }
    }

    private void addToEntries(List<ListenerRegistration> entries, Order order) {
        List<ListenerRegistration> entry = handlerSlots.get(order);
        if (entry != null) {
            entries.addAll(entry);
        }

        if (parent != null) {
            parent.addToEntries(entries, order);
        }
    }

    public ListenerRegistration[] getRegisteredListeners() {
        ListenerRegistration[] handlers = this.handlers;
        if (handlers == null) {
            handlers = bake();
        }
        return handlers;
    }

    private void addChild(HandlerList child) {
        children.add(child);
    }
}
