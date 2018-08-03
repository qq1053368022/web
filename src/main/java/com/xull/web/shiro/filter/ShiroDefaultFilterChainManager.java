package com.xull.web.shiro.filter;

import org.apache.shiro.config.Ini;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.Nameable;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.SimpleNamedFilterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShiroDefaultFilterChainManager extends DefaultFilterChainManager {
    private static final transient Logger log = LoggerFactory.getLogger(ShiroFilterFactoryBean.class);
    private Map<String, String> filterChainDefinitionMap = null;
    private String loginUrl;
    private String successUrl;
    private String unauthorizedUrl;

    public ShiroDefaultFilterChainManager() {
        setFilters(new LinkedHashMap<String, Filter>());
        setFilterChains(new LinkedHashMap<String, NamedFilterList>());
        addDefaultFilters(false);
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }

    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public void setCustomFilters(Map<String, Filter> customFilters) {
        for (Map.Entry<String, Filter> entry : customFilters.entrySet()) {
            addFilter(entry.getKey(),entry.getValue(),false);
        }
    }

    @Override
    protected void initFilter(Filter filter) {
//        super.initFilter(filter);
    }

    public void setDefaultFilterChainDefinitions(String definitions) {
        Ini ini = new Ini();
        ini.load(definitions);
        Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
        setFilterChainDefinitionMap(section);
    }

    @PostConstruct
    public void init() {
        Map<String, Filter> filters = getFilters();
        if (!CollectionUtils.isEmpty(filters)) {
            for (Map.Entry<String, Filter> entry : filters.entrySet()) {
                String name = entry.getKey();
                Filter filter = entry.getValue();
                if (StringUtils.hasText(loginUrl) && (filter instanceof AccessControlFilter)) {
                    AccessControlFilter accessControlFilter = (AccessControlFilter) filter;
                    String existingLoginUrl = accessControlFilter.getLoginUrl();
                    if (AccessControlFilter.DEFAULT_LOGIN_URL.equals(existingLoginUrl)) {
                        accessControlFilter.setLoginUrl(loginUrl);
                    }
                }
                if (StringUtils.hasText(successUrl) && (filter instanceof AuthenticationFilter)) {
                    AuthenticationFilter authenticationFilter = (AuthenticationFilter) filter;
                    String existingSuccessUrl = authenticationFilter.getSuccessUrl();
                    if (AuthenticationFilter.DEFAULT_SUCCESS_URL.equals(existingSuccessUrl)) {
                        authenticationFilter.setSuccessUrl(successUrl);
                    }
                }
                if (StringUtils.hasText(unauthorizedUrl) && (filter instanceof AuthorizationFilter)) {
                    AuthorizationFilter authorizationFilter = (AuthorizationFilter) filter;
                    String existingUnauthorizedUrl = authorizationFilter.getUnauthorizedUrl();
                    if (existingUnauthorizedUrl == null) {
                        authorizationFilter.setUnauthorizedUrl(unauthorizedUrl);
                    }
                }

                if (filter instanceof Nameable) {
                    ((Nameable) filter).setName(name);
                }

            }
        }
        Map<String, String> chains = getFilterChainDefinitionMap();
        if (!CollectionUtils.isEmpty(chains)) {
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefintion = entry.getValue();
                createChain(url,chainDefintion);
            }
        }
    }

    public FilterChain proxy(FilterChain original, List<String> chainNames) {
        NamedFilterList configured = new SimpleNamedFilterList(chainNames.toString());
        for (String chainName : chainNames) {
            configured.addAll(getChain(chainName));
        }
        return configured.proxy(original);
    }

}
