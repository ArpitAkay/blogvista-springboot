package com.blogvista.backend.service_impl;

import com.blogvista.backend.entity.Role;
import com.blogvista.backend.repository.RoleRepository;
import com.blogvista.backend.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleInfoServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleInfoServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void saveRolesInDb() {
        List<Role> roleList = List.of(new Role(101, "ROLE_ADMIN"),
                new Role(102, "ROLE_USER")
                );

        roleRepository.saveAll(roleList);
    }
}
