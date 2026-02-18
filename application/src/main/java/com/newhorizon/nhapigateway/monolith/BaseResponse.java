package com.newhorizon.nhapigateway.monolith;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@Data
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    @Builder.Default
    private Status status = Status.SUCCESS;
    @Builder.Default
    private String responseMessage = ResponseCodesEnum.OK.getDescription();
    @Builder.Default
    private Integer responseCode = ResponseCodesEnum.OK.getErrorCode();

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public enum Status {
        SUCCESS("OK"),
        WARNING("WARNING"),
        FAILURE("KO");

        private final String statusName;
    }
}
