package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@Valid @RequestBody EndpointHit endpointHit) {
        service.saveHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStatistics(@RequestParam(name = "start", required = false) String start,
                                         @RequestParam(name = "end") String end,
                                         @RequestParam(name = "uris", required = false) List<String> uris,
                                         @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.debug("Stats: Вызван метод getStatistics с параметрами {}, {}, {}, {} :", start, end, uris, unique);
        return service.getStatistics(start, end, uris, unique);
    }
}
