package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ValidationException;
import ru.practicum.repository.HitRepository;
import ru.practicum.service.StatsService;
import ru.practicum.stat.EndpointHit;
import ru.practicum.stat.ViewStats;
import ru.practicum.mapper.HitMapper;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final HitRepository repository;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void saveHit(EndpointHit endpointHit) {
        repository.save(HitMapper.toEntity(endpointHit));
    }

    @Override
    public List<ViewStats> getStatistics(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startDate;
        LocalDateTime endDate;
        try {
            startDate = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), dateTimeFormatter);
            endDate = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), dateTimeFormatter);
        } catch (Exception e) {
            throw new ValidationException("Wrong date format");
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Wrong start and end dates");
        }
        if (unique) {
            if (uris != null && !uris.isEmpty()) {
                uris.replaceAll(s -> s.replace("[", ""));
                uris.replaceAll(s -> s.replace("]", ""));
                return repository.findAllByTimestampBetweenAndUriInUnique(startDate, endDate, uris);
            } else {
                return repository.findAllByTimestampBetweenUnique(startDate, endDate);
            }
        } else {
            if (uris != null && !uris.isEmpty()) {
                uris.replaceAll(s -> s.replace("[", ""));
                uris.replaceAll(s -> s.replace("]", ""));
                return repository.findAllByTimestampBetweenAndUriIn(startDate, endDate, uris);
            } else {
                return repository.findAllByTimestampBetween(startDate, endDate);
            }
        }
    }
}

