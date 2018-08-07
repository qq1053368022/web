package com.xull.web.shiro.filter;


import com.xull.web.shiro.jcaptcha.JCaptcha;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.imageio.ImageIO;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 生成验证码
 */
@Component
public class JCaptchaFilter extends OncePerRequestFilter {
    @Async
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setDateHeader("Expires", 0L);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        /**
         * 通过调用 SecurityUtils.getSubject().getSession()可初始化session，
         * 避免验证验证码时获取不到session对象
         */
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
//        String id = request.getRequestedSessionId();
        String id = session.getId().toString();
        //凡是调用过该函数,便给会话jCaptchaType属性赋值true,以便下一次验证时候,识别到该属性，以执行验证。
        session.setAttribute("jCaptchaType",true);

        BufferedImage bi = JCaptcha.captchaService.getImageChallengeForID(id);

        ServletOutputStream out = response.getOutputStream();

        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

}
