package com.github.lakenono.redis.list;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class RedisList
{
    private static Jedis redis;

    @Test
    public void add()
    {
        for (int i = 0; i < 100; i++)
        {
            redis.lpush("demo-0", i + "");
        }
    }

    @Test
    public void get()
    {
        for (int i = 0; i < 200; i++)
        {
            String rpop = redis.rpop("demo-0");
            System.out.println(rpop);
        }
    }

    @Test
    public void size()
    {
        Long length = redis.llen("demo-0");
        System.out.println(length);
    }

    @BeforeClass
    public static void before()
    {
        redis = new Jedis("192.168.2.57", 6379);
        redis.connect();
    }

    @AfterClass
    public static void after()
    {
        redis.disconnect();
    }

}
