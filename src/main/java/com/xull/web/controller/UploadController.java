package com.xull.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @description:
 * @author: xull
 * @date: 2018-09-14 17:10
 */
@Controller
public class UploadController {
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody String upload(HttpServletRequest request,MultipartFile file) {

        try {
            String uploadDir = request.getSession().getServletContext().getRealPath("/") + "upload/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String filename = UUID.randomUUID() + suffix;
            File serverFile = new File(uploadDir + filename);

            file.transferTo(serverFile);
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败";
        }
        return "上传成功";
    }
}
