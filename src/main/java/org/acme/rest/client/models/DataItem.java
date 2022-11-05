package org.acme.rest.client.models;

import lombok.Data;

@Data
public class DataItem {

    private Long startTimestampUnix;
    private Long endTimestampUnix;
    private Long createdAtUnix;
    private Integer dynamicValueType;
    private Integer value;
    private String valueType;
}
