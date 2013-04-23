package com.github.lakenono.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.regionserver.StoreFile;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 不常调用的代码.. 写的很粗糙
 * 
 * @author lake
 */
public class HBaseAdminUtil
{
    public static Configuration conf;
    public static HBaseAdmin admin;

    static
    {
        conf = new Configuration();
        conf.addResource("hbase-site.xml");
        conf = HBaseConfiguration.create(conf);

        try
        {
            admin = new HBaseAdmin(conf);
        }
        catch (MasterNotRunningException e)
        {
            e.printStackTrace();
        }
        catch (ZooKeeperConnectionException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean tableExists(String tablename)
    {
        try
        {
            return admin.tableExists(tablename);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static void createTable(String tablename)
    {
        try
        {
            if (!admin.tableExists(tablename))
            {
                HTableDescriptor descriptor = new HTableDescriptor(tablename);
                descriptor.addFamily(new HColumnDescriptor("r"));
                admin.createTable(descriptor);
            }
        }
        catch (MasterNotRunningException e)
        {
            e.printStackTrace();
        }
        catch (ZooKeeperConnectionException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void reBuildTable(String tablename)
    {
        if (tableExists(tablename))
        {
            deleteTable(tablename);
        }

        createTableByGzip(tablename);
    }

    public static void createTableByGzip(String tablename)
    {
        try
        {
            if (!admin.tableExists(tablename))
            {
                HTableDescriptor descriptor = new HTableDescriptor(tablename);
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(Bytes.toBytes("r"), 1, "GZ", false, true, HConstants.FOREVER, StoreFile.BloomType.NONE.toString());
                descriptor.addFamily(hColumnDescriptor);
                descriptor.setValue("COMPRESSION", "GZ");
                admin.createTable(descriptor);
            }
        }
        catch (MasterNotRunningException e)
        {
            e.printStackTrace();
        }
        catch (ZooKeeperConnectionException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void reBuildTTLTable(String tablename, int ttl)
    {
        if (tableExists(tablename))
        {
            deleteTable(tablename);
        }

        createTTLTableByGzip(tablename, ttl);
    }

    public static void createTTLTableByGzip(String tablename, int ttl)
    {
        try
        {
            if (!admin.tableExists(tablename))
            {
                HTableDescriptor descriptor = new HTableDescriptor(tablename);
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(Bytes.toBytes("r"), 1, "GZ", false, true, ttl, StoreFile.BloomType.NONE.toString());
                descriptor.addFamily(hColumnDescriptor);
                descriptor.setValue("COMPRESSION", "GZ");
                admin.createTable(descriptor);
            }
        }
        catch (MasterNotRunningException e)
        {
            e.printStackTrace();
        }
        catch (ZooKeeperConnectionException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteTable(String tablename)
    {
        try
        {
            if (admin.tableExists(tablename))
            {
                admin.disableTable(tablename);
                admin.deleteTable(tablename);
            }
        }
        catch (MasterNotRunningException e)
        {
            e.printStackTrace();
        }
        catch (ZooKeeperConnectionException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException
    {

        HBaseAdminUtil.deleteTable("weibo_users");
        HBaseAdminUtil.deleteTable("weibo_vip_users");

        HTableDescriptor weibousers = new HTableDescriptor("weibo_users");
        weibousers.addFamily(new HColumnDescriptor("u"));
        weibousers.setValue("COMPRESSION", "LZO");
        admin.createTable(weibousers);

        HTableDescriptor weibovipusers = new HTableDescriptor("weibo_vip_users");
        weibovipusers.addFamily(new HColumnDescriptor("u"));
        weibovipusers.setValue("COMPRESSION", "LZO");
        admin.createTable(weibovipusers);
    }
}
