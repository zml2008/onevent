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
 * Represents a callable event.
 */
public abstract class Event {

    /**
     * Stores cancelled status. Will be false unless a subclass publishes
     * setCancelled.
     */
    protected boolean cancelled = false;

    /**
     * Get the static handler list of this event subclass.
     *
     * @return HandlerList to call event with
     */
    public abstract HandlerList getHandlers();

    /**
     * Get event type name.
     *
     * @return event name
     */
    protected String getEventName() {
        return getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return getEventName() + " (" + this.getClass().getName() + ")";
    }

    /**
     * Set cancelled status. Events which wish to be cancellable should
     * implement Cancellable and implement setCancelled as:
     *
     * <pre>
     * public void setCancelled(boolean cancelled) {
     *     super.setCancelled(cancelled);
     * }
     * </pre>
     *
     * @param cancelled True to cancel event
     */
    protected void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Returning true will prevent calling any even Order slots.
     *
     * @see Order
     * @return false if the event is propagating; events which do not implement
     *         Cancellable should never return true here
     */
    public boolean isCancelled() {
        return cancelled;
    }
}
