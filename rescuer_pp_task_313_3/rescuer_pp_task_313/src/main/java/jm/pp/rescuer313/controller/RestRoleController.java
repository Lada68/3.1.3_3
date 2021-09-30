package jm.pp.rescuer313.controller;

import jm.pp.rescuer313.dao.RoleRepository;
import jm.pp.rescuer313.dto.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
public class RestRoleController {
    private final RoleRepository roleRepository;

    public RestRoleController(@Autowired RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getRoles() {
        return ResponseEntity.ok(roleRepository.findAll().stream().map(RoleDto::new).collect(Collectors.toList()));
    }
}
