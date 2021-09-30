package jm.pp.rescuer313.controller;

import jm.pp.rescuer313.dto.UserDto;
import jm.pp.rescuer313.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/")
@CrossOrigin("*")
public class RestCurrentUserController {
    private final UserService userService;

    public RestCurrentUserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserDto> userDto = authentication instanceof AnonymousAuthenticationToken ? Optional.empty()
                : Optional.ofNullable(userService.findByUsername(authentication.getName())).map(UserDto::new);
        return userDto.map(dto -> {
            dto.setPassword("");
            return dto;
        }).map(ResponseEntity::ok).orElse(ResponseEntity.status(401).build());
    }

}
