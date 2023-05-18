package ru.practicum.mapper;

import ru.practicum.EndpointHit;
import ru.practicum.model.HitEntity;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    EndpointHit toEndpointHit(HitEntity hitEntity);

    HitEntity toHitEntity(EndpointHit endpointHit);
}
