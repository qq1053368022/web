package com.xull.web.shiro.myRealm;

import com.xull.web.entity.SysRole;
import com.xull.web.entity.SysUser;
import com.xull.web.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

import java.util.Collection;

public class UserRealm extends AuthorizingRealm {

    private SysUserService sysUserService;

    public UserRealm(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) super.getAvailablePrincipal(principalCollection);
        SysUser user = sysUserService.findByUsername(username);
        if (user != null) {
            SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
            for (SysRole role : user.getRoleList()) {
                authorizationInfo.addRole(role.getName());
                authorizationInfo.addStringPermissions(role.getPermissionsName());
            }
            return authorizationInfo;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username=(String)authenticationToken.getPrincipal();
        SysUser user = sysUserService.findByUsername(username);
        /*
        //检验用户是否已经登陆，若已经登陆，将已经登陆的用户的session注销

        //获取凭证管理器
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        //获取session管理器
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
        //获取所有session对象
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();

        for (Session session : sessions) {
            if (username.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
                sessionManager.getSessionDAO().delete(session);
            }
        }
        */
        if (user == null) {
            throw new UnknownAccountException();
        } else {
            SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(
                    user.getUsername(),
                    user.getPassword(),
                    ByteSource.Util.bytes(user.getCredentialsSalt()),
                    getName()
            );

           return info;
        }

    }
}
