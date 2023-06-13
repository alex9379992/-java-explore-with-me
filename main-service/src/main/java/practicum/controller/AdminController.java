package practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import practicum.service.CategoriesService;
import practicum.service.CompilationService;
import practicum.service.EventService;
import practicum.service.UserService;
import ru.practicum.category.CategoryDto;
import ru.practicum.compilation.CompilationDto;
import ru.practicum.compilation.NewCompilationDto;
import ru.practicum.compilation.UpdateCompilationRequest;
import ru.practicum.event.EventDto;
import ru.practicum.event.UpdateEventRequest;
import ru.practicum.user.UserDto;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Valid CategoryDto category) {
        log.info("Admin API: Вызван метод addCategory");
        return categoriesService.addCategory(category);
    }


    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Admin API: Вызван метод deleteCategory " + catId);
        categoriesService.deleteCategory(catId);
    }


    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@RequestBody CategoryDto category, @PathVariable Long catId) {
        log.info("Admin API: Вызван метод updateCategory " + catId);
        return categoriesService.update(category, catId);
    }

    @GetMapping("/events")
    public List<EventDto> searchEvents(@RequestParam(name = "users", required = false) List<Long> userIds,
                                       @RequestParam(name = "states", required = false) List<String> states,
                                       @RequestParam(name = "categories", required = false) List<Long> categories,
                                       @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                       @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                       @RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @RequestParam(name = "size", defaultValue = "10") Integer size, HttpServletRequest request) {
        log.info("Admin API: Вызван метод searchEvents ");
        return eventService.searchEvents(userIds, states, categories, rangeStart, rangeEnd, from, size, request);
    }

    @PatchMapping("events/{eventId}")
    public EventDto updateEventByAdmin(@PathVariable Long eventId, @Valid @RequestBody UpdateEventRequest event) {
        log.info("Admin: Вызван метод updateEventByAdmin " + eventId);
        return eventService.updateEvent(eventId, event);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Valid UserDto user) {
        log.info("Admin: Вызван метод addUser");
        return userService.addUser(user);
    }


    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(name = "ids", required = false) List<Long> userIds,
                                  @RequestParam(name = "from", defaultValue = "0")
                                  Integer from, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Admin: Вызван метод getUsers {}, size {}, from {}", userIds, from, size);
        return userService.getUsers(userIds, from, size);
    }


    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Admin: Вызван метод deleteUser " + userId);
        userService.deleteUser(userId);
    }


    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto compilation) {
        log.info("Admin: Вызван метод addCompilation ");
        return compilationService.addCompilation(compilation);
    }


    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Admin: Вызван метод deleteCompilation " + compId);
        compilationService.deleteCompilation(compId);
    }


    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId, @RequestBody UpdateCompilationRequest compil) {
        log.info("Admin: Вызван метод updateCompilation " + compId);
        return compilationService.updateCompilation(compId, compil);
    }


}
