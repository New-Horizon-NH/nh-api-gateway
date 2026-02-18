package com.newhorizon.nhapigateway.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum RouteRetentionEnum {
    PUBLIC("public"),
    PRIVATE("private");

    private final String retention;
}
