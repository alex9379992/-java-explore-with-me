package practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practicum.service.EventService;
import practicum.service.RequestService;
import ru.practicum.event.EventDto;
import ru.practicum.event.NewEventDto;
import ru.practicum.event.UpdateEventRequest;
import ru.practicum.request.EventRequestStatusUpdateRequest;
import ru.practicum.request.EventRequestStatusUpdateResult;
import ru.practicum.request.ParticipationRequestDto;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("users")
public class PrivateApiController {

    private final RequestService requestService;
    private final EventService eventService;

    @PostMapping("{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EventDto> createEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto event) {
        log.debug("Private API: method called createEvent, userId {} ", userId);
        return ResponseEntity.created(URI.create("/events")).body(eventService.createEvent(userId, event));
    }

    @GetMapping("{userId}/events")
    public ResponseEntity<List<EventDto>> getEventsByUserWithPage(@PathVariable Long userId,
                                                  @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Private API: method called getEventsByUserWithPage, userId, from, size {} {} {}", userId, from, size);
        return ResponseEntity.ok().body(eventService.getEventsByUserId(userId, from, size));
    }

    @GetMapping("{userId}/events/{eventId}")
    public ResponseEntity<EventDto> getEventByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.debug("Private API: method called getEventByUser, userId, eventId {} {}", userId, eventId);
        return ResponseEntity.ok().body(eventService.getEventByUserId(userId, eventId));
    }

    @PatchMapping("{userId}/events/{eventId}")
    public ResponseEntity<EventDto> updateEventByUser(@PathVariable Long userId,
                                      @PathVariable Long eventId,
                                      @Valid @RequestBody UpdateEventRequest event) {
        log.debug("Private API: method called updateEventByUser, userId eventId {} {}", userId, eventId);
        return ResponseEntity.ok().body(eventService.updateEventByUser(userId, eventId, event));
    }

    @GetMapping("{userId}/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getUserRequestsInEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.debug("Private API: method called getUserRequestsInEvent, userId eventId {} {}", userId, eventId);
        return ResponseEntity.ok().body(requestService.getUserRequestsInEvent(userId, eventId));
    }

    @PatchMapping("{userId}/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> confirmOrRejectRequestsByUser(@PathVariable Long userId, @PathVariable
    Long eventId, @Valid @RequestBody EventRequestStatusUpdateRequest request) {
        log.debug("Private API: method called confirmOrRejectRequestsByUser, userId eventId {} {}", userId, eventId);
        return ResponseEntity.ok().body(requestService.confirmOrRejectRequestsByUser(userId, eventId, request));
    }

    @GetMapping("{userId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getUserRequestsInForeignEvents(@PathVariable Long userId) {
        log.debug("Private API: method called getUserRequestsInForeignEvents, userId {}", userId);
        return ResponseEntity.ok().body(requestService.getUserRequestsInForeignEvents(userId));
    }

    @PostMapping("{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ParticipationRequestDto> addRequest(@PathVariable Long userId, @RequestParam(name = "eventId") Long eventId) {
        log.debug("Private API: method called addRequest, userId {}", userId);
        return ResponseEntity.created(URI.create("/requests")).body(requestService.addRequest(userId, eventId));
    }

    @PatchMapping("{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequestByUser(@PathVariable Long userId, @PathVariable Long requestId) {
        log.debug("Private API: method called cancelRequestByUser, userId requestId {} {}", userId, requestId);
        return ResponseEntity.ok().body(requestService.cancelRequestByUser(userId, requestId));
    }
}
