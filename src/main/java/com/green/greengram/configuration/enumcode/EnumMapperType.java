package com.green.greengram.configuration.enumcode;

import com.fasterxml.jackson.annotation.JsonValue;

public interface EnumMapperType {
    String getCode();
    @JsonValue
    String getValue();
}
