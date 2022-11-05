package org.acme.rest.client.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.acme.rest.client.mappers.DataItemMapper;
import org.acme.rest.client.models.DataItem;
import org.acme.rest.client.models.DynamicEpochValues;
import org.acme.rest.client.services.DynamicEpochValuesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static org.acme.rest.client.Constants.DYNAMIC_VALUES;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class DynamicEpochValuesResourceTest {

    @InjectMocks
    private DynamicEpochValuesResource resource;

    @Mock
    private DynamicEpochValuesService dynamicEpochValuesService;

    private final DataItemMapper dataItemMapper = DataItemMapper.INSTANCE;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<DynamicEpochValues> dynamicEpochValues;

    @BeforeEach
    void setup() throws JsonProcessingException {
        dynamicEpochValues = objectMapper.readValue(DYNAMIC_VALUES, new TypeReference<>() {});
    }

    @Test
    void getData() {
        List<DataItem> data = dynamicEpochValues.get(0).getDataSources().get(0).getData();
        when(dynamicEpochValuesService.getData()).thenReturn(data.stream()
                .map(dataItemMapper::mapToDto)
                .collect(Collectors.toList()));
        List<DataItem> dataItems = resource.getData();

        Assertions.assertEquals(5, dataItems.size());
    }

    @Test
    void addData() {
        doNothing().when(dynamicEpochValuesService).addData(anyList());

        try(Response response = resource.addData(dynamicEpochValues)) {
            Assertions.assertEquals(201, response.getStatus());
        }
    }

    @Test
    void getDataByValue() {
        int value = 75;
        List<DataItem> data = dynamicEpochValues.get(0).getDataSources().get(0).getData().stream()
                .filter(item -> item.getValue() == value)
                .collect(Collectors.toList());
        when(dynamicEpochValuesService.getDataByValue(value, 123456L)).thenReturn(data);
        List<DataItem> dataItems = resource.getDataByValue(value, 123456L);

        Assertions.assertEquals(2, dataItems.size());
    }

    @Test
    void getAverageValue() {
        when(dynamicEpochValuesService.getAverageValue(anyLong())).thenReturn(74);
        Integer averageValue = resource.getAverageValue(123456L);

        Assertions.assertEquals(74, averageValue);
    }
}
