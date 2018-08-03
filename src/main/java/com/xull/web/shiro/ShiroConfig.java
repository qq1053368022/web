package com.xull.web.shiro;

import com.xull.web.shiro.filter.JCaptchaFilter;
import com.xull.web.shiro.filter.JCaptchaValidateFilter;
import com.xull.web.service.SysUserService;
import com.xull.web.shiro.credentialsMatcher.RetryLimitHashedCredentialsMatcher;
import com.xull.web.shiro.filter.KickoutSessionControlFilter;
import com.xull.web.shiro.filter.MyFormAuthenticationFilter;
import com.xull.web.shiro.filter.ShiroDefaultFilterChainManager;
import com.xull.web.shiro.filter.ShiroPathMatchingFilterChainResolver;
import com.xull.web.shiro.myRealm.UserRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {


    @Bean(name = "lifecycleBeanPostProcessor")
    @ConditionalOnMissingBean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @ConditionalOnMissingBean
    @Bean(name = "defaultAdvisorAutoProxyCreator")
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }



    /**
     * 缓存关联器
     * @return
     */
    @Bean
    @ConditionalOnClass(name = {"org.apache.shiro.cache.ehcache.EhCacheManager"})
    public CacheManager cacheManager() {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:config/ehcache.xml");
        return ehCacheManager;
    }
    /**
     * 凭证匹配器(密码验证)
     * @return
     */
    @Bean
    public CredentialsMatcher credentialsMatcher(CacheManager cacheManager) {
        RetryLimitHashedCredentialsMatcher hashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher(cacheManager);
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(2);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
     * Realm实现
     * @param sysUserService
     * @param credentialsMatcher
     * @return
     */
    @Bean
    @DependsOn("sysUserService")
    public Realm realm(SysUserService sysUserService, CredentialsMatcher credentialsMatcher) {
        UserRealm userRealm = new UserRealm(sysUserService);
        userRealm.setCredentialsMatcher(credentialsMatcher);
        return userRealm;
    }

    /**
     * 会话Cookie模板
     * @return
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    /**
     * rememberMe的Cookie模板
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    /**
     * rememberMe管理器
     * @param rememberMeCookie
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(SimpleCookie rememberMeCookie) {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(rememberMeCookie);
        rememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return rememberMeManager;
    }

    /**
     * 会话Dao
     * @param cacheManager
     * @return
     */
    @Bean
    public SessionDAO sessionDAO(CacheManager cacheManager) {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        //会话ID生成
        Class<? extends SessionIdGenerator> idGenerator = JavaUuidSessionIdGenerator.class;
        if (idGenerator != null) {
            SessionIdGenerator sessionIdGenerator = BeanUtils.instantiate(idGenerator);
            sessionDAO.setSessionIdGenerator(sessionIdGenerator);
        }
        sessionDAO.setCacheManager(cacheManager);
        return sessionDAO;
    }

    /**
     * 会话管理器
     * @param cacheManager
     * @param sessionDAO
     * @param sessionIdCookie
     * @return
     */
    @Bean
    @DependsOn(value = {"cacheManager","sessionDAO","sessionIdCookie","executorServiceSessionValidationScheduler"})
    public SessionManager sessionManager(
            CacheManager cacheManager,
            SessionDAO sessionDAO,
            SimpleCookie sessionIdCookie,
            ExecutorServiceSessionValidationScheduler executorServiceSessionValidationScheduler) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setCacheManager(cacheManager);
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionValidationScheduler(executorServiceSessionValidationScheduler);
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie);
        return sessionManager;
    }


    @Bean
    public ExecutorServiceSessionValidationScheduler executorServiceSessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
        scheduler.setInterval(900000);
        return scheduler;
    }





    /**
     * 安全管理器
     * @param cacheManager
     * @param sessionManager
     * @param rememberMeManager
     * @param realm
     * @return
     */
    @Bean
    public SecurityManager securityManager(
            CacheManager cacheManager,
            SessionManager sessionManager,
            RememberMeManager rememberMeManager,
            Realm realm){
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();

        defaultSecurityManager.setRealm(realm);
        defaultSecurityManager.setCacheManager(cacheManager);
        defaultSecurityManager.setSessionManager(sessionManager);
        defaultSecurityManager.setRememberMeManager(rememberMeManager);
        return defaultSecurityManager;
    }

    /**
     * 相当于调用SecurityUtils.setSecurityManager(securityManager)
     * @param securityManager
     * @return
     */
    @Bean
    @DependsOn("securityManager")
    public MethodInvokingFactoryBean methodInvokingFactoryBean(SecurityManager securityManager) {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        methodInvokingFactoryBean.setArguments(securityManager);
        return methodInvokingFactoryBean;
    }

    /**
     * 踢出登陆用户过滤器
     * @param cacheManager
     * @param sessionManager
     * @return
     */
    @Bean
    public KickoutSessionControlFilter kickoutSessionControlFilter(CacheManager cacheManager, SessionManager sessionManager) {
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        kickoutSessionControlFilter.setCache(cacheManager);
        kickoutSessionControlFilter.setKickoutUrl("/login?kickout=1");
        kickoutSessionControlFilter.setSessionManager(sessionManager);
        return kickoutSessionControlFilter;
    }

    /**
     * 验证码过滤器
     * @return
     */
    @Bean
    public JCaptchaValidateFilter jCaptchaValidateFilter() {
        JCaptchaValidateFilter jCaptchaValidateFilter = new JCaptchaValidateFilter();
        jCaptchaValidateFilter.setJcaptchaEbabled(true);
        jCaptchaValidateFilter.setJcaptchaParam("jcaptchaCode");
        jCaptchaValidateFilter.setFailureKeyAttribute("shiroLoginFailure");
        return jCaptchaValidateFilter;
    }

    /**
     * 表单验证过滤器
     * @return
     */
    @Bean
    public MyFormAuthenticationFilter authenticationFilter() {
        MyFormAuthenticationFilter authenticationFilter = new MyFormAuthenticationFilter();
        authenticationFilter.setFailureKeyAttribute("shiroLoginFailure");
        return authenticationFilter;
    }


    /**
     * 过滤器
     * @param kickoutSessionControlFilter
     * @return
     */
    @Bean
    public ShiroDefaultFilterChainManager shiroDefaultFilterChainManager(
            KickoutSessionControlFilter kickoutSessionControlFilter,
            MyFormAuthenticationFilter authenticationFilter,
            JCaptchaValidateFilter jCaptchaValidateFilter) {
        ShiroDefaultFilterChainManager shiroDefaultFilterChainManager = new ShiroDefaultFilterChainManager();
        shiroDefaultFilterChainManager.setLoginUrl("/login");
        shiroDefaultFilterChainManager.setSuccessUrl("/index");
        Map<String, Filter> filterMap = shiroDefaultFilterChainManager.getFilters();


        filterMap.put("authc", authenticationFilter);
        filterMap.put("kickout", kickoutSessionControlFilter);
        filterMap.put("jCaptcha", jCaptchaValidateFilter);
//        filterMap.put("logout", logoutFilter);
        shiroDefaultFilterChainManager.setCustomFilters(filterMap);
//        shiroDefaultFilterChainManager.setFilters(filterMap);

        Map<String, String> map = new LinkedHashMap<>();
        map.put("/static/**", "anon");
        map.put("/js/**", "anon");
        map.put("/css/**", "anon");
        map.put("/img/**", "anon");
        map.put("/test/**", "anon");
        map.put("/login","jCaptcha,authc");
        map.put("/logout", "logout");
        map.put("/**", "user,kickout");
        shiroDefaultFilterChainManager.setFilterChainDefinitionMap(map);
        return shiroDefaultFilterChainManager;
    }


    @Bean
    public ShiroPathMatchingFilterChainResolver shiroPathMatchingFilterChainResolver(ShiroDefaultFilterChainManager shiroDefaultFilterChainManager) {
        ShiroPathMatchingFilterChainResolver filterChainResolver = new ShiroPathMatchingFilterChainResolver();
        filterChainResolver.setShiroDefaultFilterChainManager(shiroDefaultFilterChainManager);
        return filterChainResolver;
    }

    /**
     * Shiro的Web过滤器
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        return shiroFilterFactoryBean;
    }




//    @Bean
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,KickoutSessionControlFilter kickoutSessionControlFilter) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        shiroFilterFactoryBean.setSuccessUrl("/");
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        Map<String, Filter> filterMap = shiroFilterFactoryBean.getFilters();
//        filterMap.put("kickout", kickoutSessionControlFilter);
//        shiroFilterFactoryBean.setFilters(filterMap);
//        Map<String, String> map = new LinkedHashMap<>();
//        map.put("/js/**", "anon");
//        map.put("/css/**", "anon");
//        map.put("/img/**", "anon");
//        map.put("/test/**", "anon");
//        map.put("/login","authc");
//        map.put("/logout", "logout");
//        map.put("/**", "kickout,user");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
//        return shiroFilterFactoryBean;
//    }


    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(ShiroFilterFactoryBean shiroFilterFactoryBean, ShiroPathMatchingFilterChainResolver shiroPathMatchingFilterChainResolver) throws Exception {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setTargetObject(shiroFilterFactoryBean.getObject());
        methodInvokingFactoryBean.setTargetMethod("setFilterChainResolver");
        methodInvokingFactoryBean.setArguments(shiroPathMatchingFilterChainResolver);
        return methodInvokingFactoryBean;
    }



    @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean filterRegistrationBean1(ShiroFilterFactoryBean shiroFilterFactoryBean ) throws Exception {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.addInitParameter("targetFilterLifecycle", "true");
        filterRegistrationBean.setFilter((Filter)shiroFilterFactoryBean.getObject() );
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setEnabled(true);
        return filterRegistrationBean;
    }




//    @Bean
//    @ConditionalOnMissingBean
//    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(SecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
//        aasa.setSecurityManager(securityManager);
//        return aasa;
//    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean2() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        JCaptchaFilter jCaptchaFilter = new JCaptchaFilter();
        registration.setFilter(jCaptchaFilter);
        registration.addUrlPatterns("/img/jcaptcha.jpg");
        registration.setAsyncSupported(true);
        return registration;
    }


}
