package com.github.lakenono.counter;

import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;

public class DBCounter extends BaseCounter
{
    private QueryRunner run;

    public DBCounter(BasicDataSource dataSource)
    {
        this.run = new QueryRunner(dataSource);
    }

    @Override
    public void read()
    {

    }

    @Override
    public void persistence() throws SQLException
    {
        synchronized (this.counters)
        {
            for (Entry<String, AtomicLong> entry : this.counters.entrySet())
            {
                String key = entry.getKey();
                long value = entry.getValue().get();

                Object[] exist = this.run.query("select * from counter where counter.`key` = '" + key + "';", new ArrayHandler());

                if (exist != null)
                {
                    this.run.update("update counter set counter.`value` = counter.`value`+ ? where counter.`key` = ?", value, key);
                }
                else
                {
                    this.run.update("insert into counter values(?,?);", key, value);
                }

                entry.getValue().set(0l);
            }
        }

    }

    public static void main(String[] args) throws SQLException
    {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.1.188:3306/bdp?characterEncoding=UTF8");
        dataSource.setUsername("root");
        dataSource.setPassword("newpwd");

        DBCounter counter = new DBCounter(dataSource);
        counter.add("a");
        counter.add("c");
        counter.add("d");
        counter.add("e");

        counter.persistence();
    }
}
