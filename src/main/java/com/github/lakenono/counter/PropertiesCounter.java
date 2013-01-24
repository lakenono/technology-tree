package com.github.lakenono.counter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropertiesCounter extends BaseCounter
{
    protected PropertiesConfiguration properties;

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
    }

    @Override
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

    @Override
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

    @Override
    public String toString()
    {
        return this.counters.toString();
    }

    public static void main(String[] args) throws ConfigurationException, IOException, InterruptedException
    {
        PropertiesCounter counter = new PropertiesCounter("/Volumes/lake/tmp/propertiesCounter.properties");

        System.out.println(counter);

        for (int i = 0; i < 10000; i++)
        {
            counter.add(i % 10 + "");
        }

        // 测试自动保存
        Thread.sleep(20000);

        System.out.println("save");

        System.out.println(counter);

        System.exit(0);
    }
}
