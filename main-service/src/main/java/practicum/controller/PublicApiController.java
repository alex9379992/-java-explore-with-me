package practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practicum.service.CategoriesService;
import practicum.service.CommentService;
import practicum.service.CompilationService;
import practicum.service.EventService;
import practicum.enums.EventsSortedBy;
import ru.practicum.category.CategoryDto;
import ru.practicum.comment.CommentDto;
import ru.practicum.compilation.CompilationDto;
import ru.practicum.event.EventDto;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PublicApiController {

    private final CompilationService compilationService;
    private final CategoriesService categoriesService;
    private final EventService eventService;
    private final CommentService commentService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Public API: method called getCategories с параметрами from {}, size {}", from, size);
        return ResponseEntity.ok().body(categoriesService.getCategories(from, size));
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long catId) {
        log.debug("Public API: method called getCategoryById, catId {}", catId);
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
        log.debug("Public API: method called getEventsFiltered");
        return ResponseEntity.ok().body(eventService.getEvents(text, categoriesIds, paid, rangeStart, rangeEnd,
                onlyAvailable, sorted, from, size, request));
    }

    @GetMapping("events/{id}")
    public ResponseEntity<EventDto> getFullEventById(@PathVariable Long id, HttpServletRequest request) {
        log.debug("Public API: method called getCategoryById, id {}", id);
        return ResponseEntity.ok().body(eventService.getEventById(id, request));
    }

    @GetMapping("compilations/{compId}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable Long compId) {
        log.debug("Public API: method called getCompilationById for id {}", compId);
        return ResponseEntity.ok().body(compilationService.getCompilationById(compId));
    }

    @GetMapping("compilations")
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Public API: method called getCompilations with parameters: pinned {} from {} size {} ", pinned, from, size);
        return ResponseEntity.ok().body(compilationService.getCompilations(pinned, from, size));
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> getAllComments() {
        log.debug("Public API: method called ");
        return ResponseEntity.ok().body(commentService.getAllComments());
    }

    @PostMapping("/events/{eventId}/comment/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentDto> addComment(@PathVariable Long eventId, @Valid @RequestBody CommentDto comment,
                                 @PathVariable Long userId) {
        log.info("Private: method called addComment, userId eventId {} {}", userId, eventId);
        return ResponseEntity.created(URI.create("/comment")).body(commentService.addComment(eventId, comment, userId));
    }

    @GetMapping("/comments/all/{userId}")
    public ResponseEntity<List<CommentDto>> getAllUserComments(@PathVariable Long userId) {
        log.info("Private: method called getAllUserComments, userId {}", userId);
        return ResponseEntity.ok().body(commentService.getAllUserComments(userId));
    }

    @GetMapping("/comments/{comId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long comId) {
        log.debug("Public API: method called getCommentById parameters: comId {}", comId);
        return ResponseEntity.ok().body(commentService.getCommentById(comId));
    }
}
