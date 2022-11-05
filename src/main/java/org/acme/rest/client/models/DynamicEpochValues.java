package org.acme.rest.client.models;

import lombok.Data;

import java.util.List;

@Data
public class DynamicEpochValues {

    private Long authenticationToken;
    private List<DataSource> dataSources;
}
