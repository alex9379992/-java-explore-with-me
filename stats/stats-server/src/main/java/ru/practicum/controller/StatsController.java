package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatsService;
import ru.practicum.stat.EndpointHit;
import ru.practicum.stat.ViewStats;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService service;


    @PostMapping("/hit")
    public void saveHit(@Valid @RequestBody EndpointHit endpointHit) {
        log.info("Пришел запрос на сохранине статистики");
        service.saveHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStatistics(@RequestParam String start, @RequestParam String end,
                                         @RequestParam(required = false) List<String> uris,
                                         @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Пришел запрос на полуение статистики");
        return service.getStatistics(start, end, uris, unique);
    }
}
