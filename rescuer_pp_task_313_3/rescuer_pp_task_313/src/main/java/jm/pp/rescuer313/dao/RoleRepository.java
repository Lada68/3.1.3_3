package jm.pp.rescuer313.dao;


import jm.pp.rescuer313.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
//    @Override
//    Optional<Role> findById(Integer integer);
}
