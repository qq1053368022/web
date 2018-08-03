package com.xull.web.repository;


import com.xull.web.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysRoleRepository extends JpaRepository<SysRole,Long> {
    Optional<SysRole> findByName(String name);
}
