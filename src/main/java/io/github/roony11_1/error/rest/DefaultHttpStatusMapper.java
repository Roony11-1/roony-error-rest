package io.github.roony11_1.error.rest;

import io.github.roony11_1.error.core.ErrorCategory;
import io.github.roony11_1.error.core.StandardErrorCategories;

import java.util.HashMap;
import java.util.Map;

public final class DefaultHttpStatusMapper implements IHttpStatusMapper
{
    private static final int FALLBACK_STATUS = 500;

    private final Map<ErrorCategory, Integer> statusMap;

    public DefaultHttpStatusMapper()
    {
        this(Map.of());
    }

    public DefaultHttpStatusMapper(Map<ErrorCategory, Integer> customMappings)
    {
        this.statusMap = new HashMap<>(defaultMappings());
        this.statusMap.putAll(customMappings);
    }

    @Override
    public int getStatus(ErrorCategory category)
    {
        return statusMap.getOrDefault(category, FALLBACK_STATUS);
    }

    private static Map<ErrorCategory, Integer> defaultMappings()
    {
        return Map.of(
            StandardErrorCategories.NOT_FOUND, 404,
            StandardErrorCategories.ALREADY_EXISTS, 409,
            StandardErrorCategories.INVALID_INPUT, 400,
            StandardErrorCategories.UNAUTHORIZED, 401,
            StandardErrorCategories.FORBIDDEN, 403,
            StandardErrorCategories.INTERNAL_ERROR, 500,
            StandardErrorCategories.CONFLICT, 409,
            StandardErrorCategories.SERVICE_UNAVAILABLE, 503
        );
    }
}