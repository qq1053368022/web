package com.xull.web.shiro.filter;

import com.xull.web.entity.SysUser;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

    /**
     * 登陆判断用户是否已经登陆，如果已经登陆将登陆用户退出，以免卡在登陆界面无法跳转。
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                String account = this.getUsername(request);
                Subject subject = this.getSubject(request, response);
                String username = (String) subject.getPrincipal();
                if (account != null && username != null) {
                    subject.logout();
                }
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }
    /**
     * 用于验证码验证的 Shiro 拦截器在用于身份认证的拦截器之前运行
     * @author ur0638
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        if(request.getAttribute(getFailureKeyAttribute()) != null) {
            return true;
        }
        return super.onAccessDenied(request, response, mappedValue);
    }
}
