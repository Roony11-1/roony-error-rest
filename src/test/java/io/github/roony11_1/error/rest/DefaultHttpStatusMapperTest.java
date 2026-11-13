package io.github.roony11_1.error.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.github.roony11_1.error.core.ErrorCategory;
import io.github.roony11_1.error.core.StandardErrorCategories;

class DefaultHttpStatusMapperTest
{
    private final IHttpStatusMapper mapper = new DefaultHttpStatusMapper();

    @Test
    void mapsStandardCategoriesToHttpStatus()
    {
        assertEquals(404, mapper.getStatus(StandardErrorCategories.NOT_FOUND));
        assertEquals(409, mapper.getStatus(StandardErrorCategories.ALREADY_EXISTS));
        assertEquals(400, mapper.getStatus(StandardErrorCategories.INVALID_INPUT));
        assertEquals(401, mapper.getStatus(StandardErrorCategories.UNAUTHORIZED));
        assertEquals(403, mapper.getStatus(StandardErrorCategories.FORBIDDEN));
        assertEquals(500, mapper.getStatus(StandardErrorCategories.INTERNAL_ERROR));
        assertEquals(409, mapper.getStatus(StandardErrorCategories.CONFLICT));
        assertEquals(503, mapper.getStatus(StandardErrorCategories.SERVICE_UNAVAILABLE));
    }

    @Test
    void returnsInternalErrorForUnmappedCategory()
    {
        var custom = new ErrorCategory()
        {
            @Override public String description() { return "Custom"; }
        };

        assertEquals(500, mapper.getStatus(custom));
    }

    @Test
    void customMappingsOverrideDefaults()
    {
        var custom = new DefaultHttpStatusMapper(
            Map.of(StandardErrorCategories.NOT_FOUND, 410));

        assertEquals(410, custom.getStatus(StandardErrorCategories.NOT_FOUND));
        assertEquals(400, custom.getStatus(StandardErrorCategories.INVALID_INPUT));
    }

    @Test
    void defaultMappingsAreNotMutatedByCustomInstance()
    {
        var custom = new DefaultHttpStatusMapper(
            Map.of(StandardErrorCategories.NOT_FOUND, 410));

        assertEquals(404, new DefaultHttpStatusMapper()
            .getStatus(StandardErrorCategories.NOT_FOUND));
        assertEquals(410, custom.getStatus(StandardErrorCategories.NOT_FOUND));
    }
}