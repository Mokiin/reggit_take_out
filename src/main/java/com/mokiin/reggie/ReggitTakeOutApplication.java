package com.mokiin.reggie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Mokiin
 */
@SpringBootApplication
@MapperScan("com.mokiin.reggie.mapper")
@ServletComponentScan
@EnableTransactionManagement
public class ReggitTakeOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggitTakeOutApplication.class, args);
    }

}
