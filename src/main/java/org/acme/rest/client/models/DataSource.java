package org.acme.rest.client.models;

import lombok.Data;

import java.util.List;

@Data
public class DataSource {

    private Integer dataSource;
    private List<DataItem> data;
}
