package com.xull.web.service.impl;

import com.xull.web.entity.SysUrlFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShiroFilterChainManager {

    @Autowired
    private DefaultFilterChainManager defaultFilterChainManager;

    private Map<String ,NamedFilterList > defaultFilterChains;

    @PostConstruct
    public void init() {
        defaultFilterChains = new HashMap<String, NamedFilterList>(defaultFilterChainManager.getFilterChains());
    }

    public void initFilterChains(List<SysUrlFilter> urlFilterList) {
        defaultFilterChainManager.getFilterChains().clear();
        if (defaultFilterChains != null) {
            defaultFilterChainManager.getFilterChains().putAll(defaultFilterChains);
        }
        for (SysUrlFilter urlFilter : urlFilterList) {
            String url = urlFilter.getUrl();
            if (!StringUtils.isEmpty(urlFilter.getRoles())) {
                defaultFilterChainManager.addToChain(url,"roles",urlFilter.getRoles());
            }
            if (!StringUtils.isEmpty(urlFilter.getPermissions())) {
                defaultFilterChainManager.addToChain(url,"perms",urlFilter.getPermissions());
            }
        }
    }

}
