package jm.pp.rescuer313.dto;

import jm.pp.rescuer313.model.Role;

public class RoleDto {
    private Integer id;
    private String name;

    public RoleDto(Role role){
        this.id = role.getId();
        this.name = role.getName();
    }

    public RoleDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public RoleDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
