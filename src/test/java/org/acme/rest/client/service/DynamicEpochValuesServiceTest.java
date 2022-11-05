package org.acme.rest.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.acme.rest.client.dtos.DataItemDto;
import org.acme.rest.client.mappers.DataItemMapper;
import org.acme.rest.client.models.DataItem;
import org.acme.rest.client.models.DynamicEpochValues;
import org.acme.rest.client.persistence.entities.DataItemEntity;
import org.acme.rest.client.persistence.repositories.DataRepository;
import org.acme.rest.client.services.DynamicEpochValuesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.acme.rest.client.Constants.DYNAMIC_VALUES;
import static org.acme.rest.client.Constants.DYNAMIC_VALUES_ENTITY;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DynamicEpochValuesServiceTest {

    public static final long USER_ID = 123456L;
    public static final int HEART_RATE = 75;
    @InjectMocks
    private DynamicEpochValuesService dynamicEpochValuesService;

    @Mock
    private DataRepository repository;

    private final DataItemMapper dataItemMapper = DataItemMapper.INSTANCE;
    private static ObjectMapper objectMapper;

    private List<DataItemEntity> dataItemEntities;
    private List<DynamicEpochValues> dynamicEpochValues;

    @BeforeAll
    static void setUpObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @BeforeEach
    void setup() throws JsonProcessingException {
        dataItemEntities = objectMapper.readValue(DYNAMIC_VALUES_ENTITY, new TypeReference<>() {});
        dynamicEpochValues = objectMapper.readValue(DYNAMIC_VALUES, new TypeReference<>() {});
    }

    @Test
    void getData() {
        Answer<Stream> answer = invocation -> Stream.of(dataItemEntities.toArray());
        when(repository.streamAll()).thenAnswer(answer);
        List<DataItemDto> dataItems = dynamicEpochValuesService.getData();

        Assertions.assertEquals(5, dataItems.size());
    }

    @Test
    void addData() {
        doNothing().when(repository).persist(anyList());
        List<DataItemEntity> entityList = getDataItemEntities();

        dynamicEpochValuesService.addData(dynamicEpochValues);

        verify(repository, times(1)).persist(entityList);
    }

    @Test
    void getDataByValue() {
        List<DataItemEntity> data = dataItemEntities.stream()
                .filter(entity -> entity.getHeartRate() == HEART_RATE)
                .collect(Collectors.toList());
        when(repository.getByValue(HEART_RATE, USER_ID)).thenReturn(data);

        List<DataItem> dataItems = dynamicEpochValuesService.getDataByValue(HEART_RATE, USER_ID);
        Assertions.assertEquals(2, dataItems.size());
    }

    @Test
    void getAverageValue() {
        when(repository.getByUser(USER_ID)).thenReturn(dataItemEntities);
        Integer averageValue = dynamicEpochValuesService.getAverageValue(USER_ID);

        Assertions.assertEquals(74, averageValue);
    }

    private List<DataItemEntity> getDataItemEntities() {
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
        return entityList;
    }
}
