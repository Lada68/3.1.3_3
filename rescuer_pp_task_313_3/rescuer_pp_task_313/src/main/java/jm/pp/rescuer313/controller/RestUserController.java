package jm.pp.rescuer313.controller;

import jm.pp.rescuer313.ExeptionHandler.DataInfoHandler;
import jm.pp.rescuer313.ExeptionHandler.UserWithSuchLoginExist;
import jm.pp.rescuer313.dto.UserDto;
import jm.pp.rescuer313.model.User;
import jm.pp.rescuer313.service.UserMergeService;
import jm.pp.rescuer313.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/")
public class RestUserController {
    private final UserService userService;
    private final UserMergeService userMergeService;
    private final SmartValidator validator;


    @Autowired
    public RestUserController(UserService userService, UserMergeService userMergeService, SmartValidator validator) {
        this.userService = userService;
        this.userMergeService = userMergeService;
        this.validator = validator;
    }

    @GetMapping("/users")
    public ResponseEntity<Set<UserDto>> getAll() {
        return ResponseEntity.ok(userService.findAllUsers().stream()
                .map(UserDto::new).collect(Collectors.toSet()));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> get(@PathVariable("id") int id) {
        return ResponseEntity.ok(new UserDto(userService.findUserById(id)));
    }

    //    @PostMapping("/users")
//    public ResponseEntity<DataInfoHandler> apiAddNewUser(@Valid @RequestBody User user,
//                                                         BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            String error = getErrorsFromBindingResult(bindingResult);
//            return new ResponseEntity<>(new DataInfoHandler(error), HttpStatus.BAD_REQUEST);
//        }
//        try {
//            userService.addNewUser(user);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (DataIntegrityViolationException e) {
//            throw new UserWithSuchLoginExist("User with such login Exist");
//        }
//    }
    @PostMapping("/users")
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(new UserDto(userService.addNewUser(userMergeService.mergeCreate(userDto))));

    }

    @PutMapping("/users")
    public ResponseEntity<?> update(@RequestBody UserDto user,
                                    BindingResult bindingResult) {
        User updated = userMergeService.mergeUpdate(user);

        validator.validate(updated, bindingResult);
        if (bindingResult.hasErrors()) {
            String error = getErrorsFromBindingResult(bindingResult);
            return ResponseEntity.badRequest().body(new DataInfoHandler(error));
        }
        try {
            return ResponseEntity.ok(userService.updateUser(updated));
        } catch (DataIntegrityViolationException e) {
            throw new UserWithSuchLoginExist("User with such login Exist");
        }
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<DataInfoHandler> apiDeleteUser(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(new DataInfoHandler("User was deleted"));
    }

    private String getErrorsFromBindingResult(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
    }

}
