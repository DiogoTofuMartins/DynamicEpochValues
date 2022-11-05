package org.acme.rest.client.mappers;

import org.acme.rest.client.dtos.DataItemDto;
import org.acme.rest.client.models.DataItem;
import org.acme.rest.client.persistence.entities.DataItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import javax.enterprise.context.ApplicationScoped;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
@ApplicationScoped
public interface DataItemMapper {

    DataItemMapper INSTANCE = Mappers.getMapper(DataItemMapper.class);

    @Mapping(source = "value", target = "heartRate")
    DataItemEntity mapToEntity(DataItemDto dto);

    @Mapping(source = "heartRate", target = "value")
    DataItemDto mapEntityToDto(DataItemEntity entity);

    DataItemDto mapToDto(DataItem item);
    DataItem mapToResponse(DataItemDto dto);
}
