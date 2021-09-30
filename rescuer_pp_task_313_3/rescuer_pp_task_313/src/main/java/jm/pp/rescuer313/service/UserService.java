package jm.pp.rescuer313.service;

import jm.pp.rescuer313.dao.UserRepository;
import jm.pp.rescuer313.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    User addNewUser(User user);
    Set<User> findAllUsers();
    User findUserById(int id);
    void deleteUserById(int id);
    User updateUser(User user);
//    Optional<User> getOne(Integer id);
//
//
//    List<User> findAllUsers();
}



