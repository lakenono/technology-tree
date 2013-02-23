package com.github.lakenono.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTableInterfaceFactory;
import org.apache.hadoop.hbase.client.HTablePool;

public class EucitaHTablePool
{
    public HTablePool get(int size)
    {
        // init conf
        Configuration conf = HBaseConfiguration.create();

        return new HTablePool(conf, size, new HTableInterfaceFactory()
        {
            public void releaseHTableInterface(HTableInterface table)
            {
                try
                {
                    table.close();
                }
                catch (IOException ioe)
                {
                    throw new RuntimeException(ioe);
                }
            }

            public HTableInterface createHTableInterface(Configuration config, byte[] tableName)
            {
                try
                {
                    HTable table = new HTable(config, tableName);
                    // 真是不敢相信 忘记关掉autoflush会带来那么多奇怪的bug.. false以后相安无事了.. 
                    table.setAutoFlush(false);
                    table.setWriteBufferSize(1024 * 1024 * 10);

                    return table;
                }
                catch (IOException ioe)
                {
                    throw new RuntimeException(ioe);
                }
            }
        });
    }
}
