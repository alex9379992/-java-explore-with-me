package practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.exception.AlreadyExistsException;
import practicum.exception.NotFoundException;
import practicum.model.EventEntity;
import practicum.model.ParticipationRequestEntity;
import practicum.model.UserEntity;
import practicum.repository.EventRepository;
import practicum.repository.ParticipationRequestsRepository;
import practicum.repository.UserRepository;
import practicum.service.RequestService;
import practicum.mappers.ParticipationRequestMapper;
import ru.practicum.event.State;
import ru.practicum.request.EventRequestStatusUpdateRequest;
import ru.practicum.request.EventRequestStatusUpdateResult;
import ru.practicum.request.ParticipationRequestDto;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestsServiceImpl implements RequestService {

    private final UserRepository userRepository;

    private final EventRepository eventsRepository;

    private final ParticipationRequestsRepository participationRequestsRepository;
    private final ParticipationRequestMapper participationRequestMapper = new ParticipationRequestMapper();

    @Transactional
    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        UserEntity requester = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет "
                + userId));
        EventEntity event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Такого события нет "
                + eventId));
        ParticipationRequestEntity request = new ParticipationRequestEntity(LocalDateTime.now(), event, requester, State.PENDING);
        Optional<ParticipationRequestEntity> requests = participationRequestsRepository.findByRequesterIdAndEventId(userId, eventId);
        if (requests.isPresent()) {
            throw new AlreadyExistsException("Нельзя добавить повторный запрос: userId {}, eventId {} " + userId + eventId);
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new AlreadyExistsException("Инициатор события не может добавить запрос на участие в своём событии " + userId);
        }
        if (!(event.getState().equals(State.PUBLISHED))) {
            throw new AlreadyExistsException("Нельзя участвовать в неопубликованном событии");
        }
        int limit = event.getParticipantLimit();
        if (limit != 0) {
            if (limit == event.getConfirmedRequests()) {
                throw new AlreadyExistsException("У события достигнут лимит запросов на участие: " + limit);
            }
        } else {
            request.setState(State.CONFIRMED);
        }

        if (!event.getRequestModeration()) {
            request.setState(State.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        ParticipationRequestEntity savedRequest = participationRequestsRepository.save(request);
        return participationRequestMapper.toDto(savedRequest);
    }


    @Override
    public List<ParticipationRequestDto> getUserRequestsInForeignEvents(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Такого пользователя нет");
        }
        List<ParticipationRequestEntity> requests = participationRequestsRepository.findAllByRequesterIdInForeignEvents(userId);
        return requests.stream().map(participationRequestMapper::toDto).collect(Collectors.toList());
    }


    @Override
    public List<ParticipationRequestDto> getUserRequestsInEvent(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Такого пользователя нет");
        }
        if (!eventsRepository.existsById(eventId)) {
            throw new NotFoundException("Такого события нет");
        }
        List<ParticipationRequestEntity> requests = participationRequestsRepository.findAllUserRequestsInEvent(userId, eventId);
        return requests.stream().map(participationRequestMapper::toDto).collect(Collectors.toList());
    }


    @Transactional
    @Override
    public ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Такого пользователя нет");
        }
        ParticipationRequestEntity request = participationRequestsRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Такой заявки нет " + requestId));
        request.setState(State.CANCELED);
        return participationRequestMapper.toDto(participationRequestsRepository.save(request));
    }


    @Transactional
    @Override
    public EventRequestStatusUpdateResult confirmOrRejectRequestsByUser(Long userId, Long eventId,
                                                                        EventRequestStatusUpdateRequest request) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Такого пользователя нет");
        }
        EventEntity event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Такого события нет "
                + eventId));
        if (event.getParticipantLimit() == 0 && !event.getRequestModeration()) {
            throw new AlreadyExistsException("Подтверждение заявки не требуется " + eventId);
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new AlreadyExistsException("Превышен лимит подтвержденных заявок " + eventId);
        }
        List<Long> requestIds = request.getRequestIds();
        String status = request.getStatus();
        List<ParticipationRequestEntity> requests = requestIds.stream().map((id) -> participationRequestsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Такой заявки нет "
                        + id))).collect(Collectors.toList());
        List<ParticipationRequestEntity> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestEntity> rejectedRequests = new ArrayList<>();
        List<ParticipationRequestEntity> updatedRequests = new ArrayList<>();
        for (ParticipationRequestEntity req : requests) {
            if (status.equals("CONFIRMED") && req.getState().equals(State.PENDING)) {
                if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                    req.setState(State.REJECTED);
                    updatedRequests.add(req);
                    rejectedRequests.add(req);
                }
                req.setState(State.CONFIRMED);
                updatedRequests.add(req);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                confirmedRequests.add(req);
            }
            if (status.equals("REJECTED") && req.getState().equals(State.PENDING)) {
                req.setState(State.REJECTED);
                updatedRequests.add(req);
                rejectedRequests.add(req);
            }
        }
        participationRequestsRepository.saveAll(updatedRequests);
        eventsRepository.save(event);
        List<ParticipationRequestDto> con = confirmedRequests.stream().map(participationRequestMapper::toDto).collect(Collectors.toList());
        List<ParticipationRequestDto> rej = rejectedRequests.stream().map(participationRequestMapper::toDto).collect(Collectors.toList());
        return participationRequestMapper.toUpdateResult(con, rej);
    }
}
