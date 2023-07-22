package practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import practicum.service.CategoriesService;
import practicum.service.CompilationService;
import practicum.service.EventService;
import practicum.util.EventsSortedBy;
import ru.practicum.category.CategoryDto;
import ru.practicum.compilation.CompilationDto;
import ru.practicum.event.EventDto;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PublicApiController {

    private final CompilationService compilationService;
    private final CategoriesService categoriesService;
    private final EventService eventService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Public API: Вызван метод getCategories с параметрами from {}, size {}", from, size);
        return ResponseEntity.ok().body(categoriesService.getCategories(from, size));
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long catId) {
        log.debug("Public API: Вызван метод getCategoryById, catId {}", catId);
        return ResponseEntity.ok().body(categoriesService.getCategoryById(catId));
    }

    @GetMapping("events")
    public ResponseEntity<List<EventDto>> getEventsFiltered(@RequestParam(name = "text", required = false) String text,
                                                            @RequestParam(name = "categories", required = false) List<Long> categoriesIds,
                                                            @RequestParam(name = "paid", required = false) Boolean paid,
                                                            @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                            @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                                            @RequestParam(name = "sort", required = false) EventsSortedBy sorted,
                                                            @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                            @RequestParam(name = "size", defaultValue = "10") Integer size, HttpServletRequest request) {
        log.debug("Public API: Вызван метод getEventsFiltered");
        return ResponseEntity.ok().body(eventService.getEvents(text, categoriesIds, paid, rangeStart, rangeEnd,
                onlyAvailable, sorted, from, size, request));
    }

    @GetMapping("events/{id}")
    public ResponseEntity<EventDto> getFullEventById(@PathVariable Long id, HttpServletRequest request) {
        log.debug("Public API: Вызван метод getCategoryById, id {}", id);
        return ResponseEntity.ok().body(eventService.getEventById(id, request));
    }

    @GetMapping("compilations/{compId}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable Long compId) {
        log.debug("Public API: Вызван метод getCompilationById for id {}", compId);
        return ResponseEntity.ok().body(compilationService.getCompilationById(compId));
    }


    @GetMapping("compilations")
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Public API: Вызван метод getCompilations with parameters: pinned {} from {} size {} ", pinned, from, size);
        return ResponseEntity.ok().body(compilationService.getCompilations(pinned, from, size));
    }
}
