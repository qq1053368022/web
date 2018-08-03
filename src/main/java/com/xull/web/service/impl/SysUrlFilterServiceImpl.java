package com.xull.web.service.impl;


import com.xull.web.entity.SysUrlFilter;
import com.xull.web.repository.SysUrlFilterRepository;
import com.xull.web.service.SysUrlFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SysUrlFilterServiceImpl extends BaseServiceImpl<SysUrlFilter,Long> implements SysUrlFilterService {
    @Autowired
    private SysUrlFilterRepository sysRoleRepository;

    @Autowired
    private ShiroFilterChainManager shiroFilterChainManager;

    @Override
    @Autowired
    public void setBaseMapper() {
        super.setBaseMapper(sysRoleRepository);
    }

    @PostConstruct
    public void initFilterChain() {
        shiroFilterChainManager.initFilterChains(this.findAll());
    }
}
