package com.xull.web.repository;


import com.xull.web.entity.SysUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<SysUser,String > {
    Optional<SysUser> findByUsername(String username);

    @Query(value = "select t from sys_user t where  t.fusername=:name or t.femail=:name ",nativeQuery=true)
    Optional<SysUser> userLogin(@Param("name") String name);
}
