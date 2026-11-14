package io.github.roony11_1.error.rest;

import io.github.roony11_1.error.core.ErrorCategory;
import io.github.roony11_1.error.core.StandardErrorCategories;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class DefaultHttpStatusMapper implements IHttpStatusMapper
{
    private static final int FALLBACK_STATUS = 500;

    private static final int MIN_STATUS = 100;
    private static final int MAX_STATUS = 599;

    private static final Map<ErrorCategory, Integer> DEFAULT_MAPPINGS = Map.of(
        StandardErrorCategories.NOT_FOUND,           404,
        StandardErrorCategories.ALREADY_EXISTS,      409,
        StandardErrorCategories.INVALID_INPUT,       400,
        StandardErrorCategories.UNAUTHORIZED,        401,
        StandardErrorCategories.FORBIDDEN,           403,
        StandardErrorCategories.INTERNAL_ERROR,      500,
        StandardErrorCategories.CONFLICT,            409,
        StandardErrorCategories.SERVICE_UNAVAILABLE, 503
    );

    private final Map<ErrorCategory, Integer> statusMap;

    public DefaultHttpStatusMapper()
    {
        this(Map.of());
    }

    public DefaultHttpStatusMapper(Map<ErrorCategory, Integer> customMappings)
    {
        Objects.requireNonNull(customMappings, "customMappings");

        validate(customMappings);

        var merged = new HashMap<ErrorCategory, Integer>(DEFAULT_MAPPINGS);
        merged.putAll(customMappings);
        this.statusMap = Map.copyOf(merged);
    }

    @Override
    public int getStatus(ErrorCategory category)
    {
        Objects.requireNonNull(category, "category");
        return statusMap.getOrDefault(category, FALLBACK_STATUS);
    }

    private static void validate(Map<ErrorCategory, Integer> mappings)
    {
        mappings.forEach((category, status) -> 
        {
            Objects.requireNonNull(category, "category en customMappings");
            Objects.requireNonNull(status,   "status para " + category);

            if (status < MIN_STATUS || status > MAX_STATUS)
                throw new IllegalArgumentException(
                    "Status HTTP inválido para " + category + ": " + status
                    + " (esperado en [" + MIN_STATUS + ", " + MAX_STATUS + "])");
        });
    }
}