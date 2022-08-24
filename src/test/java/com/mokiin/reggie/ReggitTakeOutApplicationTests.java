package com.mokiin.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
class ReggitTakeOutApplicationTests {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void testRedis() {
        redisTemplate.opsForValue().set("name1","tim");
        String name = (String) redisTemplate.opsForValue().get("name1");
        System.out.println(name);
    }

}
