package com.blogvista.backend.controller;

import com.blogvista.backend.service.RoleService;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostConstruct
    private void saveRolesInDb() {
        roleService.saveRolesInDb();
    }
}
