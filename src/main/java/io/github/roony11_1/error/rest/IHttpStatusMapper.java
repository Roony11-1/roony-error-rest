package io.github.roony11_1.error.rest;

import io.github.roony11_1.error.core.ErrorCategory;

public interface IHttpStatusMapper
{
    int getStatus(ErrorCategory category);
}