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
 * Order of event listener calls.
 * <p/>
 * Odd-numbered slots are called even when events are marked "not propogating".
 * If an event stops propogating partway through an even slot, that slot will
 * not cease execution, but future even slots will not be called.
 *
 * @author lahwran
 */
public enum Order {
    /**
     * Called before all other handlers. Should be used for high-priority event
     * canceling.
     */
    EARLIEST(0, false),

    /**
     * Called after "Earliest" handlers and before "Early" handlers. Is called
     * even when event has been canceled. Should generally be used to uncancel
     * events canceled in Earliest.
     */
    EARLY_IGNORE_CANCELLED(1, true),

    /**
     * Called after "Earliest" handlers. Should generally be used for low
     * priority event canceling.
     */
    EARLY(2, false),

    /**
     * Called after "Early" handlers and before "Default" handlers. Is called
     * even when event has been canceled. This is for general-purpose always-run
     * events.
     */
    DEFAULT_IGNORE_CANCELLED(3, true),
    /**
     * Default call, for general purpose handlers
     */
    DEFAULT(4, false),

    /**
     * Called after "Default" handlers and before "Late" handlers. Is called
     * even when event has been canceled.
     */
    LATE_IGNORE_CANCELLED(5, true),

    /**
     * Called after "Default" handlers.
     */
    LATE(6, false),

    /**
     * Called after "Late" handlers and before "Latest" handlers. Is called even
     * when event has been canceled.
     */
    LATEST_IGNORE_CANCELLED(7, true),

    /**
     * Called after "Late" handlers.
     */
    LATEST(8, false),

    /**
     * Called after "Latest" handlers. No changes to the event should be made in
     * this order slot (though it is not enforced). Is called even when event
     * has been cancelled.
     */
    MONITOR(9, true);

    private final int index;
    private final boolean ignoreCancelled;

    Order(int index, boolean ignoreCancelled) {
        this.index = index;
        this.ignoreCancelled = ignoreCancelled;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     *
     * @return whether this Order ignores cancellation status
     */
    public boolean ignoresCancelled() {
        return ignoreCancelled;
    }
}
