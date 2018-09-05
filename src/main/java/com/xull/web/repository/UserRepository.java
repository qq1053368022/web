package com.xull.web.repository;


import com.xull.web.entity.SysUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<SysUser,String > {
    Optional<SysUser> findByUsername(String username);
}
