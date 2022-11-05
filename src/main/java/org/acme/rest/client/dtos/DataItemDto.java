package org.acme.rest.client.dtos;

import lombok.Data;

@Data
public class DataItemDto {

    private Long startTimestampUnix;
    private Long endTimestampUnix;
    private Long createdAtUnix;
    private Integer dynamicValueType;
    private Integer value;
    private String valueType;
    private Integer authenticationToken;
    private Integer datasource;
}
