package org.acme.rest.client.persistence.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@NamedQueries({
        @NamedQuery(name = "DataItemEntity.getByValue", query = "from DataItemEntity where heartRate = ?1 and authenticationToken = ?2"),
        @NamedQuery(name = "DataItemEntity.getByUser", query = "from DataItemEntity where authenticationToken = ?1")
})
public class DataItemEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private Long startTimestampUnix;
    private Long endTimestampUnix;
    private Long createdAtUnix;
    private Integer dynamicValueType;
    private Integer heartRate;
    private String valueType;
    private Long authenticationToken;
    private Integer datasource;
}
