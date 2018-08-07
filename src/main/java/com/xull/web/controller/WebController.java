package com.xull.web.controller;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
public class WebController {

    @RequestMapping(value = {"/index","/"})
    public String changeToIndex(Model map) {
        //集合演示
        List<String> list = new ArrayList<String>();
        for(int i=0;i<10;i++){
            list.add("优夺"+i);
        }
        map.addAttribute("lists", list);

        //map集合
        Map<String, String> ma =new HashMap<String,String>();
        ma.put("a", "砖石");
        ma.put("b","黄金");
        ma.put("c","白金");
        map.addAttribute("TestMap", ma);

        //时间
        map.addAttribute("nowTime", new Date().toString());

        //普通方法
        map.addAttribute("name", "World");
        return "index";
    }

//    @RequestMapping(value = "/login",method = RequestMethod.GET)
//    public String login() {
//        System.out.printf("调用一次");
//        return "login";
//    }
//
//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    @ResponseBody
//    public String loginCheck(String username, String password, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
//
//        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//        Subject subject = SecurityUtils.getSubject();
//        ResponseJson responseJson=new ResponseJson();
//        try {
//            subject.login(token);
//        } catch (UnknownAccountException uae) {
//            redirectAttributes.addFlashAttribute("message", "账户不存在");
//        } catch (IncorrectCredentialsException ice) {
//            redirectAttributes.addFlashAttribute("message", "密码错误");
//        }
//        if (subject.isAuthenticated()) {
//            SavedRequest request1 = WebUtils.getAndClearSavedRequest(request);
//            String url = ((request1 == null || request1.getRequestURI() == null) ? request.getContextPath()+"/index" : request1.getRequestUrl());
//            responseJson.setMsg("success");
//            responseJson.setUrl(url);
//            return JSONObject.toJSONString(responseJson);
//        } else {
//            token.clear();
//            Map<String,String > map=(Map) redirectAttributes.getFlashAttributes();
//            responseJson.setMsg("false");
//            responseJson.setReturnStr(map.get("message"));
//            return JSONObject.toJSONString(responseJson);
//        }
//    }

    @RequestMapping(value = "/login")
    public String onLogin(HttpServletRequest request, Model model) {
        String exceptionClassName = (String)request.getAttribute("shiroLoginFailure");
        String error="";
        if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
            error = "用户名/密码错误";
        } else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
            error = "用户名/密码错误";
        }else if (AuthenticationException.class.getName().equals(exceptionClassName)){
            error = "身份认证失败";
        } else if (ExcessiveAttemptsException.class.getName().equals(exceptionClassName)) {
            error = "登陆次数超过限制";
        } else if ("jCaptcha.error".equals(exceptionClassName)){
            error = "验证码错误";
        }else if (exceptionClassName != null) {
            error = "错误:" + exceptionClassName;
        }
        if (error != "") {
            request.setAttribute("jcaptchaEbabled", true);
        }
        model.addAttribute("error", error);
        return "login";
    }

    @RequestMapping(value = "/test/main")
    public String main() {
        return "test/main";
    }

//    @RequestMapping(value = "/img/jCaptcha.jpg")
//    @ResponseBody
//    public void getCaptchaImg(HttpServletRequest request,HttpServletResponse response) throws IOException {
//        response.setDateHeader("Expires", 0L);
//        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
//        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
//        response.setHeader("Pragma", "no-cache");
//        response.setContentType("image/jpeg");
//
//        Long id = System.currentTimeMillis();
//        String myId = id.toString();
//        BufferedImage bufferedImage = JCaptcha.captchaService.getImageChallengeForID(myId);
//        ServletOutputStream outputStream = response.getOutputStream();
//        ImageIO.write(bufferedImage, "jpg", outputStream);
//        try{
//            outputStream.flush();
//        }finally {
//            outputStream.close();
//        }
//    }
}

