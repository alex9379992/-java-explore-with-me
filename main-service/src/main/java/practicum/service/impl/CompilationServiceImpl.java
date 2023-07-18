package practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import practicum.exception.NotFoundException;
import practicum.model.CompilationEntity;
import practicum.model.EventEntity;
import practicum.repository.CompilationRepository;
import practicum.repository.EventRepository;
import practicum.service.CompilationService;
import practicum.util.CompilationMapper;
import ru.practicum.compilation.CompilationDto;
import ru.practicum.compilation.NewCompilationDto;
import ru.practicum.compilation.UpdateCompilationRequest;


import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper = new CompilationMapper();

    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Page<CompilationEntity> compilationEntities = compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size, Sort.by("id")));
        return compilationEntities.stream().map(compilationMapper::toDto).collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(Long id) {
        CompilationEntity compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Такой подборки нет " + id));
        return compilationMapper.toDto(compilation);
    }

    @Transactional
    @Override
    public CompilationDto addCompilation(NewCompilationDto compilation) {
        if (compilation.getEvents() != null && compilation.getEvents().size() != 0) {
            Set<Long> eventIds = compilation.getEvents();
            Set<EventEntity> events = new HashSet<>(eventRepository.findAllByIdIn(eventIds));
            CompilationEntity compilationEntity = compilationMapper.toEntity(compilation);
            compilationEntity.setEvents(events);
            CompilationEntity savedCompil = compilationRepository.save(compilationEntity);
            return compilationMapper.toDto(savedCompil);
        }
        CompilationEntity fromDto = compilationMapper.toEntity(compilation);
        CompilationEntity savedCompil = compilationRepository.save(fromDto);
        return compilationMapper.toDto(savedCompil);
    }

    @Transactional
    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Такой подборки нет " + compId));
        compilationRepository.deleteById(compId);
    }


    @Transactional
    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest compilation) {
        CompilationEntity compilationFromDb = compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Такой подборки нет " + compId));
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            Set<Long> eventIds = compilation.getEvents();
            Set<EventEntity> events = new HashSet<>(eventRepository.findAllByIdIn(eventIds));
            compilationFromDb.setEvents(events);
        }
        if (compilation.getPinned() != null) {
            compilationFromDb.setPinned(compilation.getPinned());
        }
        if (compilation.getTitle() != null) {
            compilationFromDb.setTitle(compilation.getTitle());
        }
        CompilationEntity updated = compilationRepository.save(compilationFromDb);
        return compilationMapper.toDto(updated);
    }
}
