package practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practicum.service.*;
import ru.practicum.category.CategoryDto;
import ru.practicum.comment.CommentDto;
import ru.practicum.compilation.CompilationDto;
import ru.practicum.compilation.NewCompilationDto;
import ru.practicum.compilation.UpdateCompilationRequest;
import ru.practicum.event.EventDto;
import ru.practicum.event.UpdateEventRequest;
import ru.practicum.user.UserDto;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final CategoriesService categoriesService;
    private final CompilationService compilationService;
    private final EventService eventService;
    private final UserService userService;
    private final CommentService commentService;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @Valid CategoryDto category) {
        log.debug("Admin API: method called addCategory");
        return ResponseEntity.created(URI.create("/categories")).body(categoriesService.addCategory(category));
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.debug("Admin API: method called deleteCategory " + catId);
        categoriesService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto category, @PathVariable Long catId) {
        log.debug("Admin API: method called updateCategory " + catId);
        return ResponseEntity.ok().body(categoriesService.update(category, catId));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> searchEvents(@RequestParam(name = "users", required = false) List<Long> userIds,
                                                       @RequestParam(name = "states", required = false) List<String> states,
                                                       @RequestParam(name = "categories", required = false) List<Long> categories,
                                                       @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                       @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                       @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                       @RequestParam(name = "size", defaultValue = "10") Integer size, HttpServletRequest request) {
        log.debug("Admin API: method called searchEvents ");
        return ResponseEntity.ok().
                body(eventService.searchEvents(userIds, states, categories, rangeStart, rangeEnd, from, size, request));
    }

    @PatchMapping("events/{eventId}")
    public ResponseEntity<EventDto> updateEventByAdmin(@PathVariable Long eventId, @Valid @RequestBody UpdateEventRequest event) {
        log.debug("Admin API: method called updateEventByAdmin " + eventId);
        return ResponseEntity.ok().body(eventService.updateEvent(eventId, event));
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid UserDto user) {
        log.debug("Admin API: method called addUser");
        return ResponseEntity.created(URI.create("/users")).body(userService.addUser(user));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(name = "ids", required = false) List<Long> userIds,
                                                  @RequestParam(name = "from", defaultValue = "0")
                                                  Integer from, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Admin API: method called getUsers {}, size {}, from {}", userIds, from, size);
        return ResponseEntity.ok().body(userService.getUsers(userIds, from, size));
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.debug("Admin API: method called deleteUser " + userId);
        userService.deleteUser(userId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CompilationDto> addCompilation(@Valid @RequestBody NewCompilationDto compilation) {
        log.debug("Admin API: method called addCompilation ");
        return ResponseEntity.created(URI.create("/compilation")).body(compilationService.addCompilation(compilation));
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.debug("Admin API: method called deleteCompilation " + compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@PathVariable Long compId, @RequestBody UpdateCompilationRequest compil) {
        log.debug("Admin API: method called updateCompilation " + compId);
        return ResponseEntity.ok().body(compilationService.updateCompilation(compId, compil));
    }

    @DeleteMapping("comments/{userId}/{comId}")
    public void deleteComment(@PathVariable Long userId, @PathVariable Long comId) {
        log.info("Private API: method called deleteComment, userId {} {} ", userId, comId);
        commentService.deleteComment(userId, comId);
    }

    @PatchMapping("/comments/{comId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long comId, @Valid @RequestBody CommentDto comment) {
        log.info("Admin API: method called updateComment {}", comId);
        return ResponseEntity.ok().body(commentService.updateComment(comId, comment));
    }
}
