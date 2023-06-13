package practicum.util;

import practicum.model.LocationEntity;
import ru.practicum.event.LocationDto;

public class LocationMapper {

    public static LocationDto toDto(LocationEntity entity) {
        LocationDto locationDto = new LocationDto();
        locationDto.setLon(entity.getLon());
        locationDto.setLat(entity.getLat());
        return locationDto;
    }

    public static LocationEntity toEntity(LocationDto dto) {
        LocationEntity entity = new LocationEntity();
        entity.setLon(dto.getLon());
        entity.setLat(dto.getLat());
        return entity;
    }

}
