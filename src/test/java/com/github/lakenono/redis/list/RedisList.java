package com.github.lakenono.redis.list;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class RedisList
{
    private Jedis redis;
    
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
    public void getAlways()
    {
        while (true)
        {
            String rpop = redis.rpop("demo-0");
            System.out.println(rpop);
        }
    }

    @Test
    public void size()
    {
        Long length = this.redis.llen("Sylvanas-message");
        System.out.println(length);
    }

    @Before
    public void before()
    {
        redis = new Jedis("42.96.168.163", 6379);
        redis.connect();
    }

    @After
    public void after()
    {
        redis.disconnect();
    }

}
