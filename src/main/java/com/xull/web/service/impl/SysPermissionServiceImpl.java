package com.xull.web.service.impl;

import com.xull.web.entity.SysPermission;
import com.xull.web.repository.SysPermissionRepository;
import com.xull.web.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Transactional
@Service("sysPermissionService")
public class SysPermissionServiceImpl extends BaseServiceImpl<SysPermission,Long> implements SysPermissionService {

    @Autowired
    SysPermissionRepository sysPermissionRepository;

    @Override
    @PostConstruct
    public void setBaseMapper() {
        super.setBaseMapper(sysPermissionRepository);
    }


}
