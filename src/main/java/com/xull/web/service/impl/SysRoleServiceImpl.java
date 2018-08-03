package com.xull.web.service.impl;

import com.xull.web.entity.SysRole;
import com.xull.web.repository.SysRoleRepository;
import com.xull.web.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service("sysRoleService")
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole,Long> implements SysRoleService {
    @Autowired
    SysRoleRepository sysRoleRepository;

    @Override
    @PostConstruct
    public void setBaseMapper() {
        super.setBaseMapper(sysRoleRepository);
    }

    @Override
    public SysRole findByName(String name) {
        Optional<SysRole> optional =  sysRoleRepository.findByName(name);
        if (optional.isPresent()){
            return optional.get();
        }else {
            return null;
        }
    }

}
