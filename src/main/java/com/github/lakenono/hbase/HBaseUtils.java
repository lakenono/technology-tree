package com.github.lakenono.hbase;

import org.apache.commons.lang.ArrayUtils;

public class HBaseUtils
{
    public static byte[] keyBuild(byte[]... bs)
    {
        byte[] result = null;

        for (byte[] b : bs)
        {
            result = ArrayUtils.addAll(result, b);
        }

        return result;
    }
}
