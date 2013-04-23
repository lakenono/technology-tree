package com.github.lakenono.hbase.demo;

import org.junit.Test;

import com.github.lakenono.hbase.HBaseAdminUtil;

public class CreateTable
{
    @Test
    public void create()
    {
        HBaseAdminUtil.createTableByGzip(Global.tableName);
    }

    @Test
    public void delete()
    {
        HBaseAdminUtil.deleteTable(Global.tableName);
    }
}
