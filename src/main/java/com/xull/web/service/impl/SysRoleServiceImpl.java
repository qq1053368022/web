package com.xull.web.service.impl;

import com.xull.web.entity.SysRole;
import com.xull.web.repository.BaseRepository;
import com.xull.web.repository.RoleRepository;
import com.xull.web.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service("sysRoleService")
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole,String > implements SysRoleService {
    @Autowired
    RoleRepository sysRoleRepository;


    @Override
    public SysRole findByName(String name) {
        Optional<SysRole> optional =  sysRoleRepository.findByName(name);
        if (optional.isPresent()){
            return optional.get();
        }else {
            return null;
        }
    }

    @Override
    @PostConstruct
    public BaseRepository<SysRole, String> getBaseRepository() {
        return this.sysRoleRepository;
    }
}
