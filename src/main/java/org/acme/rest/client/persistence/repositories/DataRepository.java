package org.acme.rest.client.persistence.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.acme.rest.client.persistence.entities.DataItemEntity;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class DataRepository implements PanacheRepositoryBase<DataItemEntity, Long> {

    public List<DataItemEntity> getByValue(Integer value, Long userId) {
        return list("#DataItemEntity.getByValue", value, userId);
    }

    public List<DataItemEntity> getByUser(Long userId) {
        return list("#DataItemEntity.getByUser", userId);
    }

}
