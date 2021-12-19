package com.melodicalbuild.maximuslogging.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public final class LoggedEvent {
    private final String message;
    private final Map<String, List<String>> conditions;
    private final Field[] fields;

    public LoggedEvent(@NotNull final String message, @NotNull final Map<String, List<String>> conditions, @Nullable final Field[] fields) {
        this.message = message;
        this.conditions = conditions;
        this.fields = fields;
    }

    /**
     * Get the log message related to this event
     * @return the log message for this event
     */
    @NotNull
    public String getMessage() {
        return message;
    }

    /**
     * Get all conditions for this event
     * @return all conditions for this event
     */
    @NotNull
    public Map<String, List<String>> getConditions() {
        return conditions;
    }

    /**
     * Get the variables to log for a condition
     * @param condition the condition
     * @return list of variables if some are set for this condition, otherwise null
     */
    @Nullable
    public final List<String> getConditions(final String condition) {
        return conditions.get(condition);
    }

    /**
     * Get the fields to serialize for this event
     * @return fields to serialize for this event, null if none set
     */
    @Nullable
    public final Field[] getFields() {
        return fields;
    }
}
