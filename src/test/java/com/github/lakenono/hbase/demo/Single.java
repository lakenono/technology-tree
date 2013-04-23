package com.github.lakenono.hbase.demo;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import com.github.lakenono.hbase.HTablePoolUtils;

public class Single
{
    HTablePool hTablePool = new HTablePoolUtils().get(1);

    @Test
    public void add() throws IOException
    {
        HTableInterface table = this.hTablePool.getTable(Global.tableName);

        Put put = new Put(Bytes.toBytes("a001"));
        put.add(Bytes.toBytes("r"), Bytes.toBytes("age"), Bytes.toBytes(15));

        table.put(put);
        table.flushCommits();
        this.hTablePool.putTable(table);
    }

    @Test
    public void get() throws IOException
    {
        HTableInterface table = this.hTablePool.getTable(Global.tableName);

        Get get = new Get(Bytes.toBytes("a001"));

        Result result = table.get(get);

        System.out.println(Bytes.toString(result.getRow()));
        System.out.println(Bytes.toInt(result.getValue(Bytes.toBytes("r"), Bytes.toBytes("age"))));

        this.hTablePool.putTable(table);
    }
}
