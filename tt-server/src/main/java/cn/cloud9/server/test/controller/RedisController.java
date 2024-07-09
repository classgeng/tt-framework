package cn.cloud9.server.test.controller;

import cn.cloud9.server.test.model.RedisModel;
import cn.cloud9.server.test.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月12日 下午 09:10
 */
@Slf4j
@RestController
@RequestMapping("/test/redis")
public class RedisController {

    @Resource
    private RedisTemplate<String, RedisModel> restTemplate;

    @Autowired
    private RedisTemplate<String, List<RedisModel>> listTemplate;

    @GetMapping("/one")
    public RedisModel getOneRedisMode() {
        RedisModel redisModel = new RedisModel();
        redisModel.setAmount(100);
        redisModel.setCode("RM-1001");
        redisModel.setStatus(true);
        redisModel.setCreateTime(LocalDateTime.now());
        redisModel.setTimeline2(new Date());
        redisModel.setTimeLine(LocalTime.now());
        redisModel.setUpdateDate(LocalDate.now());

        final ValueOperations<String, RedisModel> ops = restTemplate.opsForValue();
        ops.set("redisModel", redisModel);
        log.info("redisModel, before -> {}", redisModel);
        redisModel = ops.get("redisModel");
        log.info("redisModel, after -> {}", redisModel);
        return redisModel;
    }

    @GetMapping("/list")
    public void getListRedisMode() {
        List<RedisModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RedisModel redisModel = new RedisModel();
            redisModel.setAmount(100);
            redisModel.setCode("RM-1001");
            redisModel.setStatus(true);
            redisModel.setCreateTime(LocalDateTime.now());
            redisModel.setTimeline2(new Date());
            redisModel.setTimeLine(LocalTime.now());
            redisModel.setUpdateDate(LocalDate.now());

            list.add(redisModel);
        }

        final ValueOperations<String, List<RedisModel>> ops = listTemplate.opsForValue();
        ops.set("redisModelList", list);
        log.info("redisModelList, before -> {}", list);
        list = ops.get("redisModelList");
        log.info("redisModelList, after -> {}", list);
    }
}
