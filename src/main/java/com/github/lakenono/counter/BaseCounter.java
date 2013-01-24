package com.github.lakenono.counter;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.time.DateFormatUtils;

public abstract class BaseCounter
{
    protected ConcurrentHashMap<String, AtomicLong> counters = new ConcurrentHashMap<String, AtomicLong>();

    protected ScheduledExecutorService scheduledExecutorService;

    protected boolean hasNew = false;

    public BaseCounter()
    {
        // 定时保存
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
        this.scheduledExecutorService.scheduleWithFixedDelay(new Runnable()
        {
            public void run()
            {
                try
                {
                    persistence();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    protected AtomicLong getCounter(String counter)
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

    public abstract void read() throws Exception;

    public abstract void persistence() throws Exception;

    public void destroy() throws Exception
    {
        this.persistence();
    }

}
