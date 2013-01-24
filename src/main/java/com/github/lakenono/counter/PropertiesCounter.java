package com.github.lakenono.counter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.time.DateFormatUtils;

public class PropertiesCounter
{
    private PropertiesConfiguration properties;

    private ConcurrentHashMap<String, AtomicLong> counters = new ConcurrentHashMap<String, AtomicLong>();

    private ScheduledExecutorService scheduledExecutorService;

    private boolean hasNew = false;

    public PropertiesCounter(String path) throws ConfigurationException, IOException
    {
        System.out.println(path);
        File file = new File(path);
        if (!file.exists())
        {
            file.createNewFile();
        }

        this.properties = new PropertiesConfiguration(file);

        // 还原持久化数据
        this.read();

        // 定时保存
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
        this.scheduledExecutorService.scheduleWithFixedDelay(new Runnable()
        {
            public void run()
            {
                persistence();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    private AtomicLong getCounter(String counter)
    {
        AtomicLong c = this.counters.get(counter);

        if (c == null)
        {
            this.counters.putIfAbsent(counter, new AtomicLong());
            c = this.counters.get(counter);
        }

        return c;
    }

    public void add(String counter)
    {
        this.hasNew = true;

        AtomicLong atomicLong = this.getCounter(counter);
        atomicLong.addAndGet(1);
    }

    public void addByDate(String counter)
    {
        this.add(DateFormatUtils.format(new Date(), "yyyyMMdd") + "." + counter);
    }

    public void persistence()
    {
        if (!this.hasNew) // 判断是否改变
        {
            return;
        }

        String[] array = this.counters.keySet().toArray(new String[0]);
        Arrays.sort(array);

        this.properties.clear();
        for (String countername : array)
        {
            this.properties.setProperty(countername, this.counters.get(countername).longValue());
        }

        try
        {
            this.properties.save();
            this.hasNew = false; // 标志目前没有未持久化内容
        }
        catch (ConfigurationException e)
        {
            throw new RuntimeException("计数器持久化失败");
        }
    }

    public void read()
    {
        @SuppressWarnings("unchecked")
        Iterator<String> keys = this.properties.getKeys();

        while (keys.hasNext())
        {
            String countername = keys.next();
            this.counters.put(countername, new AtomicLong(this.properties.getLong(countername)));
        }
    }

    public void destroy()
    {
        this.persistence();
    }

    public String toString()
    {
        return this.counters.toString();
    }
}
