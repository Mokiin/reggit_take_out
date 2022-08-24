package com.mokiin.reggie.controller;

import com.mokiin.reggie.conmmon.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Mokiin
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        log.info("bastpath {}",basePath);
        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 使用UUID重新生成文件名
        String fileName = UUID.randomUUID().toString() + suffix;
        // 创建一个目录对象
        File dir = new File(basePath);
        // 判断当前目录是否存在
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            // 将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     * @return
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        // 输入流，通过输入流读取文件内容
        FileInputStream fileInputStream = null;
        // 输出流，通过输出流将文件写回到浏览器
        ServletOutputStream outputStream = null;
        try {
            // 输入流，通过输入流读取文件内容
            fileInputStream = new FileInputStream(new File(basePath + name));
            // 输出流，通过输出流将文件写回到浏览器
            outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
