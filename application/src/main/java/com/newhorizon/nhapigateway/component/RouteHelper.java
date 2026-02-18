package com.newhorizon.nhapigateway.component;

import com.newhorizon.nhapigateway.dto.RouteDTO;
import com.newhorizon.nhapigateway.enums.RouteRetentionEnum;
import org.springframework.http.HttpMethod;

import java.util.List;

import static java.util.Objects.isNull;

public interface RouteHelper {
    List<String> getRoutesWithRetention(HttpMethod httpMethod, RouteRetentionEnum retentionEnum);

    List<RouteDTO> getRoutes();

    void logRoutesWithRetention(RouteRetentionEnum retentionEnum);

    default void checkArgument(Object argument) {
        if (isNull(argument)) {
            throw new IllegalArgumentException("Argument can not be null");
        }
    }
}
