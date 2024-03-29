package practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.exception.AlreadyExistsException;
import practicum.exception.NotFoundException;
import practicum.model.UserEntity;
import practicum.repository.UserRepository;
import practicum.service.UserService;
import practicum.mappers.UserMapper;
import ru.practicum.user.UserDto;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper = new UserMapper();

    @Transactional
    @Override
    public UserDto addUser(UserDto user) {
        if (userRepository.existsByName(user.getName())) {
            throw new AlreadyExistsException("Пользователь с таким именем уже существует: " + user.getName());
        }
        UserEntity savedUser = userRepository.save(userMapper.toEntity(user));
        return userMapper.toDto(savedUser);
    }

    @Override
    public List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id").ascending());
        List<UserEntity> users = userRepository.findAllByUserIdIn(pageRequest, userIds).getContent();
        return users.stream().map(userMapper::toDto).sorted(Comparator.comparing(UserDto::getId)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Такого пользователя нет " + id);
        }
        userRepository.deleteById(id);
    }
}
