package com.xull.web.repository;


import com.xull.web.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser,Long> {
    Optional<SysUser> findByUsername(String username);
}
