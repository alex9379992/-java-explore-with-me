package ru.practicum.service;

import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;


import java.util.List;

public interface StatsService {

    void saveHit(EndpointHit endpointHit);

    List<ViewStats> getStatistics(String start, String end, List<String> uris, boolean unique);
}
