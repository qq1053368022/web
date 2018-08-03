package com.xull.web.shiro.filter;

import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ShiroPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {
    private ShiroDefaultFilterChainManager shiroDefaultFilterChainManager;

    public void setShiroDefaultFilterChainManager(ShiroDefaultFilterChainManager shiroDefaultFilterChainManager) {
        this.shiroDefaultFilterChainManager = shiroDefaultFilterChainManager;
        setFilterChainManager(shiroDefaultFilterChainManager);
    }

    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }
        String requestURI = getPathWithinApplication(request);
        List<String> chainName = new ArrayList<String>();
        for (String pathPattern : filterChainManager.getChainNames()) {
            if (pathMatches(pathPattern, requestURI)) {
                chainName.add(pathPattern);
                break;
//                if ("anon".equals(filterChainManager.getChain(pathPattern).get(0).toString())) {
//                    break;
//                }
            }
        }
        if (chainName.size() == 0) {
            return null;
        }
        return shiroDefaultFilterChainManager.proxy(originalChain,chainName);
    }
}
