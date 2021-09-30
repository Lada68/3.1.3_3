package jm.pp.rescuer313.service;

import jm.pp.rescuer313.dao.RoleRepository;
import jm.pp.rescuer313.dao.UserRepository;
import jm.pp.rescuer313.dto.RoleDto;
import jm.pp.rescuer313.dto.UserDto;
import jm.pp.rescuer313.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserMergeService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SecurityService securityService;

    public UserMergeService(@Autowired UserRepository userRepository,
                            @Autowired RoleRepository roleRepository,
                            @Autowired SecurityService securityService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.securityService = securityService;
    }

    public User mergeCreate(UserDto dto) {
        return new User(dto.getUsername(),
                securityService.getCrypt(dto.getPassword()),
                dto.getName(), dto.getSurname(), dto.getAge(),
                dto.getRoles().stream()
                        .map(RoleDto::getId)
                        .map(roleRepository::getOne)
                        .collect(Collectors.toSet()));
    }

    public User mergeUpdate(UserDto dto) {
        User old = Optional.of(dto.getId()).flatMap(userRepository::findById)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(dto.getId())));
        if (dto.getName() != null) old.setName(dto.getName());
        if (dto.getSurname() != null) old.setSurname(dto.getSurname());
        if (dto.getAge() != null) old.setAge(dto.getAge());
        if (dto.getUsername() != null) old.setUsername(dto.getUsername());
        if (dto.getPassword() != null) old.setPassword(securityService.getCrypt(dto.getPassword()));
        if (dto.getRoles() != null && !dto.getRoles().isEmpty())
            old.setRoles(dto.getRoles().stream().map(RoleDto::getId).map(roleRepository::getOne)
                    .collect(Collectors.toSet()));
        return old;
    }
}
