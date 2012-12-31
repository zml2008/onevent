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
 * Interface for events that can be cancelled.
 */
public interface Cancellable {
    /**
     * If an event stops propogating (ie, is cancelled) partway through an even
     * slot, that slot will not cease execution, but future even slots will not
     * be called.
     *
     * @param cancelled True to set event canceled, False to uncancel event.
     */
    public void setCancelled(boolean cancelled);

    /**
     * Get event canceled state.
     *
     * @return whether event is cancelled
     */
    public boolean isCancelled();
}
