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
