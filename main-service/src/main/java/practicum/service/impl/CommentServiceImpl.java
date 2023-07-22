package practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practicum.exception.NotFoundException;
import practicum.model.CommentEntity;
import practicum.model.EventEntity;
import practicum.model.ParticipationRequestEntity;
import practicum.model.UserEntity;
import practicum.repository.CommentRepository;
import practicum.repository.EventRepository;
import practicum.repository.ParticipationRequestsRepository;
import practicum.repository.UserRepository;
import practicum.service.CommentService;
import practicum.util.CommentMapper;
import ru.practicum.comment.CommentDto;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;

    private final EventRepository eventsRepository;

    private final CommentRepository commentRepository;

    private final ParticipationRequestsRepository participationRequestsRepository;

    private final CommentMapper commentMapper = new CommentMapper();

    @Override
    @Transactional
    public CommentDto addComment(Long eventId, CommentDto commentDto, Long userId) {
        UserEntity author = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет"));
        EventEntity event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Такого события нет"));
        Optional<ParticipationRequestEntity> request = participationRequestsRepository.findParticipationRequestByRequesterIdAndEventId(userId, eventId);
        if (request.isEmpty()) {
            throw new NotFoundException("Вы не участвовали в данном событии " + eventId);
        }

        CommentEntity comment = commentMapper.toEntity(commentDto);
        comment.setAuthor(author);
        comment.setEvent(event);
        CommentEntity savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    public List<CommentDto> getAllUserComments(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет"));
        List<CommentEntity> comments = commentRepository.findAllByAuthorId(userId);
        if (comments.size() == 0) {
            return Collections.emptyList();
        }
        return comments.stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long comId, CommentDto comment) {
        CommentEntity savedComment = commentRepository.findById(comId).orElseThrow(() -> new NotFoundException("Такого комментария нет"));
        savedComment.setText(comment.getText());
        savedComment.setCreatedOn(LocalDateTime.now());
        return commentMapper.toDto(commentRepository.save(savedComment));
    }

    @Override
    public CommentDto getCommentById(Long comId) {
        CommentEntity savedComment = commentRepository.findById(comId).orElseThrow(() -> new NotFoundException("Такого комментария нет"));
        return commentMapper.toDto(commentRepository.save(savedComment));
    }

    @Override
    public List<CommentDto> getAllComments() {
        return commentRepository.findAll().stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long userId, Long comId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет "
                + userId));
        commentRepository.findById(comId).orElseThrow(() ->
                new NotFoundException("Такого комментария нет " + comId));
        commentRepository.deleteById(comId);
    }
}
