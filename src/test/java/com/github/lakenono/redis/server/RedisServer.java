package com.github.lakenono.redis.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class RedisServer
{
    private Jedis redis;

    @Test
    public void info()
    {
        String info = redis.info();
        System.out.println(info);
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
