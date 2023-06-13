package ru.practicum.mapper;

import ru.practicum.model.HitEntity;
import ru.practicum.stat.EndpointHit;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    EndpointHit toEndpointHit(HitEntity hitEntity);

    HitEntity toHitEntity(EndpointHit endpointHit);
}
