package com.xull.web.repository;


import com.xull.web.entity.SysRole;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<SysRole,String > {
    Optional<SysRole> findByName(String name);
}
