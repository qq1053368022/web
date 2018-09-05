package com.xull.web.service.impl;


import com.xull.web.entity.SysUrlFilter;
import com.xull.web.repository.BaseRepository;
import com.xull.web.repository.UrlFilterRepository;
import com.xull.web.service.SysUrlFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SysUrlFilterServiceImpl extends BaseServiceImpl<SysUrlFilter,String > implements SysUrlFilterService {
    @Autowired
    private UrlFilterRepository sysRoleRepository;

    @Autowired
    private ShiroFilterChainManager shiroFilterChainManager;



    @PostConstruct
    public void initFilterChain() {
        shiroFilterChainManager.initFilterChains(this.findAll());
    }

    @Override
    public BaseRepository<SysUrlFilter, String> getBaseRepository() {
        return this.sysRoleRepository;
    }
}
