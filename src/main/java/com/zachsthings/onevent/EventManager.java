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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventManager {
    private static final Logger LOGGER = Logger.getLogger(EventManager.class.getCanonicalName());

    private EventManager() {
    }

    public static <T extends Event> T callEvent(T event) {
        HandlerList handlers = event.getHandlers();
        ListenerRegistration[] listeners = handlers.getRegisteredListeners();

        if (listeners != null) {
            for (ListenerRegistration listener : listeners) {
                try {
                    if (!event.isCancelled() || listener.getOrder().ignoresCancelled()) {
                        listener.getExecutor().execute(event);
                    }
                } catch (Throwable ex) {
                    LOGGER.log(Level.SEVERE, "Could not pass event " + event.getEventName() + " to " + listener.getOwner().getClass().getName(), ex);
                }
            }
        }
        return event;
    }

    public static void registerEvents(Listener listener, Object owner) {
        for (Map.Entry<Class<? extends Event>, Set<ListenerRegistration>> entry : createRegisteredListeners(listener, owner).entrySet()) {
            Class<? extends Event> delegatedClass = getRegistrationClass(entry.getKey());
            if (!entry.getKey().equals(delegatedClass)) {
                LOGGER.severe("Plugin attempted to register delegated event class " + entry.getKey() + ". It should be using " + delegatedClass + "!");
                continue;
            }
            getEventListeners(delegatedClass).registerAll(entry.getValue());
        }
    }

    public static void registerEvent(Class<? extends Event> event, Order priority, EventExecutor executor, Object owner) {
        getEventListeners(event).register(new ListenerRegistration(executor, priority, owner));
    }

    /**
     * Returns the specified event type's HandlerList
     *
     * @param type EventType to lookup
     * @return HandlerList The list of registered handlers for the event.
     */
    private static HandlerList getEventListeners(Class<? extends Event> type) {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    private static Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class) && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalArgumentException("Unable to find handler list for event " + clazz.getName());
            }
        }
    }

    public static Map<Class<? extends Event>, Set<ListenerRegistration>> createRegisteredListeners(final Listener listener, Object plugin) {
        Map<Class<? extends Event>, Set<ListenerRegistration>> ret = new HashMap<Class<? extends Event>, Set<ListenerRegistration>>();
        Method[] methods;
        try {
            methods = listener.getClass().getDeclaredMethods();
        } catch (NoClassDefFoundError e) {
            LOGGER.severe("Plugin " + plugin.getClass().getSimpleName() + " is attempting to register event " + e.getMessage() + ", which does not exist. Ignoring events registered in " + listener.getClass());
            return ret;
        }
        for (final Method method : methods) {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) {
                continue;
            }
            final Class<?> checkClass = method.getParameterTypes()[0];
            Class<? extends Event> eventClass;
            if (!Event.class.isAssignableFrom(checkClass) || method.getParameterTypes().length != 1) {
                LOGGER.severe("Wrong method arguments used for event type registered");
                continue;
            } else {
                eventClass = checkClass.asSubclass(Event.class);
            }
            method.setAccessible(true);
            Set<ListenerRegistration> eventSet = ret.get(eventClass);
            if (eventSet == null) {
                eventSet = new HashSet<ListenerRegistration>();
                ret.put(eventClass, eventSet);
            }
            eventSet.add(new ListenerRegistration(new EventExecutor() {

                public void execute(Event event) throws EventException {
                    try {
                        if (!checkClass.isAssignableFrom(event.getClass())) {
                            throw new EventException("Wrong event type passed to registered method");
                        }
                        method.invoke(listener, event);
                    } catch (Throwable t) {
                        throw new EventException(t);
                    }
                }

            }, eh.order(), plugin));
        }
        return ret;
    }
}
