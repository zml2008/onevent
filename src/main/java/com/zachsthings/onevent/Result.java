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

public enum Result {
    /**
     * Deny the event. Depending on the event, the action indicated by the event
     * will either not take place or will be reverted. Some actions may not be
     * denied.
     */
    DENY(false),
    /**
     * Neither deny nor allow the event. The server will proceed with its normal
     * handling.
     */
    DEFAULT(null),
    /**
     * Allow / Force the event. The action indicated by the event will take
     * place if possible, even if the server would not normally allow the
     * action. Some actions may not be allowed.
     */
    ALLOW(true);

    private Boolean result;

    private Result(Boolean result) {
        this.result = result;
    }

    public Boolean getResult() {
        return result;
    }
}
