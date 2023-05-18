package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.mapper.Mapper;
import ru.practicum.repository.HitRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;



@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final HitRepository repository;
    private final Mapper mapper;

    @Override
    public void saveHit(EndpointHit endpointHit) {
        repository.save(mapper.toHitEntity(endpointHit));
    }

    @Override
    public List<ViewStats> getStatistics(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startDate = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), dateTimeFormatter);
        LocalDateTime endDate   = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), dateTimeFormatter);
        if (unique) {
            if (uris != null && !uris.isEmpty()) {
                return repository.findAllByTimestampBetweenAndUriInUnique(startDate, endDate, uris);
            } else {
                return repository.findAllByTimestampBetweenUnique(startDate, endDate);
            }
        } else {
            if (uris != null && !uris.isEmpty()) {
                return repository.findAllByTimestampBetweenAndUriIn(startDate, endDate, uris);
            } else {
                return repository.findAllByTimestampBetween(startDate, endDate);
            }
        }
    }
}
