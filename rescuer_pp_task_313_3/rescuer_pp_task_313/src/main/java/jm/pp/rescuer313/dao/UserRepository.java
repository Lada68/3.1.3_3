package jm.pp.rescuer313.dao;

import jm.pp.rescuer313.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

//    User findById(int id);
//
//
//    @Override
//    void deleteById(Integer integer);
}
