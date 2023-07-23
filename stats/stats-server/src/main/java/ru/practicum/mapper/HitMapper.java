package ru.practicum.mapper;


import ru.practicum.model.HitEntity;
import ru.practicum.stat.EndpointHit;

public class HitMapper {

    public static HitEntity toEntity(EndpointHit endpointHit) {
        HitEntity hitEntity = new HitEntity();
        hitEntity.setIp(endpointHit.getIp());
        hitEntity.setApp(endpointHit.getApp());
        hitEntity.setTimestamp(endpointHit.getTimestamp());
        hitEntity.setUri(endpointHit.getUri());
        return hitEntity;
    }
}
