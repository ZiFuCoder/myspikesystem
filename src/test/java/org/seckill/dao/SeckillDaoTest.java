package org.seckill.dao;

import org.seckill.entity.Seckill;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

import static org.junit.Assert.*;
/**
 *配置spring和junit整合，这样junit在启动时就会加载spring容器
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    //注入DAO实现类依赖
    @Resource
    private SeckillDao seckillDao;
    @Test
    public void reduceNumber() throws Exception{
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L, killTime);
        System.out.println("updateCount" + updateCount);
    }

    @Test
    public void queryById() throws Exception{
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getNumber());
        System.out.println(seckill);
    }

    @Test
    /**
     *  Parameter 'offset' not found. Available parameters are [0, 1, param1, param2]
     *  因为如果多参数的话mybatis无法找到哪个参数对应哪个参数，单参数可以找到，因为唯一。所以需要加注解@param
     */
    public void queryAll() throws Exception{
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for(Seckill seckill : seckills)
            System.out.println(seckill);
    }
}