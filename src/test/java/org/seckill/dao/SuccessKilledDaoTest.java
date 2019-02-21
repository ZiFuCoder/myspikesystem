package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest{
    @Resource
    SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() throws Exception{
        long id = 1001L;
        long iphone = 18569420507L;
        int insertCount = successKilledDao.insertSuccessKilled(id, iphone);
        System.out.println("insertCount=" + insertCount);
    }

    @Test
    /**
     * Zero date value prohibited异常，当用mybatis查询中出现多个零日期时会抛出该异常，解决方法：
     * 1.driver-url=jdbc:mysql://127.0.0.1/test?zeroDateTimeBehavior=convertToNull
     * ZERO日期会转化为null
     * 2.driver-url=jdbc:mysql://127.0.0.1/test?zeroDateTimeBehavior=round
     * ZERO 日期会转化为0001-01-0100:00:00.0，相当于加了一年
     */
    public void queryByIdWithSeckill() throws Exception{
        long id = 1000;
        long phone = 384728345;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}