package org.acme.rest.client.rest;

import org.acme.rest.client.RequestUUIDHeaderFactory;
import org.acme.rest.client.mappers.DataItemMapper;
import org.acme.rest.client.models.DataItem;
import org.acme.rest.client.models.DynamicEpochValues;
import org.acme.rest.client.services.DynamicEpochValuesService;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/data-sources")
@RegisterClientHeaders(RequestUUIDHeaderFactory.class)
public class DynamicEpochValuesResource {

    @Inject
    DynamicEpochValuesService dynamicEpochValuesService;

    DataItemMapper dataItemMapper = DataItemMapper.INSTANCE;

    @GET
    public List<DataItem> getData() {
        return dynamicEpochValuesService.getData().stream()
                .map(dataItemMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @POST
    public Response addData(List<DynamicEpochValues> dynamicEpochValues) {
        dynamicEpochValuesService.addData(dynamicEpochValues);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/values")
    public List<DataItem> getDataByValue(@QueryParam("value") Integer value, @QueryParam("user_id") Long userId) {
        return dynamicEpochValuesService.getDataByValue(value, userId);
    }

    @GET
    @Path("average/user")
    public Integer getAverageValue(@QueryParam("user_id") Long userId) {
        return dynamicEpochValuesService.getAverageValue(userId);
    }

}
