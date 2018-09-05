package com.xull.web.service.impl;

import com.xull.web.entity.SysPermission;
import com.xull.web.repository.BaseRepository;
import com.xull.web.repository.PermissionRepository;
import com.xull.web.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Transactional
@Service("sysPermissionService")
public class SysPermissionServiceImpl extends BaseServiceImpl<SysPermission,String > implements SysPermissionService {

    @Autowired
    PermissionRepository sysPermissionRepository;


    @Override
    public BaseRepository<SysPermission, String> getBaseRepository() {
        return this.sysPermissionRepository;
    }
}
