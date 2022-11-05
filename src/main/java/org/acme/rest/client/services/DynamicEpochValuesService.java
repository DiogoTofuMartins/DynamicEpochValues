package org.acme.rest.client.services;

import org.acme.rest.client.dtos.DataItemDto;
import org.acme.rest.client.mappers.DataItemMapper;
import org.acme.rest.client.models.DataItem;
import org.acme.rest.client.models.DynamicEpochValues;
import org.acme.rest.client.persistence.entities.DataItemEntity;
import org.acme.rest.client.persistence.repositories.DataRepository;
import org.acme.rest.client.rest.exception.BadRequestException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@ApplicationScoped
public class DynamicEpochValuesService {

    DataItemMapper dataItemMapper = DataItemMapper.INSTANCE;

    @Inject
    DataRepository dataRepository;

    @Transactional
    public List<DataItemDto> getData() {
        return dataRepository.streamAll()
                .map(dataItemMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addData(List<DynamicEpochValues> dynamicEpochValues) {
        List<DataItemEntity> entityList = new ArrayList<>();

        dynamicEpochValues.forEach(dynamicEpochValue -> dynamicEpochValue.getDataSources().forEach(user -> {
            AtomicReference<DataItemEntity> entity = new AtomicReference<>();
            user.getData().forEach(dataItem -> {
                entity.set(dataItemMapper.mapToEntity(dataItemMapper.mapToDto(dataItem)));
                entity.get().setAuthenticationToken(dynamicEpochValue.getAuthenticationToken());
                entity.get().setDatasource(user.getDataSource());
                entityList.add(entity.get());
            });
        }));

        dataRepository.persist(entityList);
    }

    @Transactional
    public List<DataItem> getDataByValue(Integer value, Long userId) {
        return dataRepository.getByValue(value, userId).stream()
                .map(entity -> dataItemMapper.mapToResponse(dataItemMapper.mapEntityToDto(entity)))
                .collect(Collectors.toList());
    }

    @Transactional
    public Integer getAverageValue(Long userId) {
        List<DataItemDto> dtos = dataRepository.getByUser(userId).stream()
                .map(dataItemMapper::mapEntityToDto)
                .collect(Collectors.toList());
        if (dtos.isEmpty()) {
            throw new BadRequestException("User don't have any data associated!");
        }
        AtomicReference<Integer> average = new AtomicReference<>(0);

        dtos.forEach(dto -> {
            average.getAndUpdate(v -> v + dto.getValue());
        });
        return (average.get() / dtos.size());
    }
}
