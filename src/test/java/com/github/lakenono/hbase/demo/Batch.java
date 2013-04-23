package com.github.lakenono.hbase.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import com.github.lakenono.hbase.HBaseAdminUtil;
import com.github.lakenono.hbase.HBaseUtils;
import com.github.lakenono.hbase.HTablePoolUtils;

public class Batch
{
    HTablePool hTablePool = new HTablePoolUtils().get(1);

    @Test
    public void add() throws IOException
    {
        HTableInterface table = this.hTablePool.getTable(Global.tableName);

        List<Put> puts = new ArrayList<Put>();

        for (int i = 1000; i <= 3000; i++)
        {
            Put put = new Put(Bytes.toBytes("b" + i)); // id为字典序
            put.add(Bytes.toBytes("r"), Bytes.toBytes("age"), Bytes.toBytes(RandomUtils.nextInt(50)));
            puts.add(put);
        }

        table.put(puts);
        table.flushCommits();
        this.hTablePool.putTable(table);
    }

    @Test
    public void scan() throws IOException
    {
        Scan scan = new Scan();

        HTableInterface table = this.hTablePool.getTable(Global.tableName);
        ResultScanner scanner = table.getScanner(scan);

        for (Result result : scanner)
        {
            byte[] value = result.getValue(Bytes.toBytes("r"), Bytes.toBytes("age"));
            int age = Bytes.toInt(value);

            if (age == 10)
            {
                System.out.println(Bytes.toString(result.getRow()));
            }

        }
    }

    @Test
    public void index() throws IOException
    {
        HBaseAdminUtil.deleteTable(Global.tableName);

        HBaseAdminUtil.createTableByGzip(Global.tableName);
        HBaseAdminUtil.createTableByGzip(Global.ageIndex);

        HTableInterface table = this.hTablePool.getTable(Global.tableName);
        HTableInterface index_age = this.hTablePool.getTable(Global.ageIndex);

        List<Put> puts = new ArrayList<Put>();
        List<Put> agePuts = new ArrayList<Put>();

        for (int i = 1000; i <= 3000; i++)
        {
            String key = "b" + i;
            int age = RandomUtils.nextInt(50);

            Put put = new Put(Bytes.toBytes(key)); // id为字典序
            put.add(Bytes.toBytes("r"), Bytes.toBytes("age"), Bytes.toBytes(age));
            puts.add(put);

            Put agePut = new Put(HBaseUtils.keyBuild(Bytes.toBytes(age), Bytes.toBytes(key)));
            agePut.add(Bytes.toBytes("r"), Bytes.toBytes("key"), Bytes.toBytes(key));
            agePuts.add(agePut);
        }

        table.put(puts);
        table.flushCommits();

        index_age.put(agePuts);
        index_age.flushCommits();

        this.hTablePool.putTable(table);
        this.hTablePool.putTable(index_age);
    }

    @Test
    public void useFilter() throws IOException
    {
        HTableInterface table = this.hTablePool.getTable(Global.tableName);

        Scan scan = new Scan();
        scan.setFilter(new SingleColumnValueFilter(Bytes.toBytes("r"), Bytes.toBytes("age"), CompareOp.EQUAL, Bytes.toBytes(10)));

        int size = 0;

        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner)
        {
            System.out.println(Bytes.toString(result.getRow()));
            size++;
        }

        System.out.println(size);
    }

    @Test
    public void useIndex() throws IOException
    {
        HTableInterface table = this.hTablePool.getTable(Global.tableName);
        HTableInterface index_age = this.hTablePool.getTable(Global.ageIndex);

        Scan scan = new Scan();
        scan.setStartRow(HBaseUtils.keyBuild(Bytes.toBytes(10), Bytes.toBytes("00000")));
        scan.setStopRow(HBaseUtils.keyBuild(Bytes.toBytes(11), Bytes.toBytes("ZZZZZZ")));

        int size = 0;

        ResultScanner scanner = index_age.getScanner(scan);
        for (Result result : scanner)
        {
            System.out.println(result.getValue(Bytes.toBytes("r"), Bytes.toBytes("key")));
            size++;
        }
        System.out.println(size);
    }
}
