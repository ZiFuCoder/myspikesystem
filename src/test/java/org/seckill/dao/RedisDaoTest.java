package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.cache.RedisDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    @Autowired
    private SeckillDao seckillDao;
    private final long id = 1001;
    @Autowired
    private RedisDao redisDao;

    @Test
    public void allRedisTest(){
        Seckill seckill = redisDao.getSeckill(id);

        if(seckill == null){
            System.out.println("redis中没有该商品id");
            seckill = seckillDao.queryById(id);
            if(seckill != null){
                redisDao.putSeckill(seckill);
            }else{
                System.out.println("该商品id不存在");
            }
        }else
            System.out.println("redis中有该商品id");
        System.out.println(seckill);
    }

}
