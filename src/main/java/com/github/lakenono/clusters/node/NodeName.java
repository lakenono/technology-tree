package com.github.lakenono.clusters.node;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * 用主机名 加 当前pid组成节点名称.
 * 可直接根据NodeName获得集群网络拓扑. (包括机器个数 每个机器的实例)
 * 
 * @author lake
 */
public class NodeName
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("name: " + getRuntimeName());
        System.out.println("pid: " + getPid());

        System.in.read(); // block the program so that we can do some probing on it  
    }

    private static String getRuntimeName()
    {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName(); // format: "pid@hostname"  

        return name;
    }

    private static int getPid()
    {
        String name = getRuntimeName();

        try
        {
            return Integer.parseInt(name.substring(0, name.indexOf('@')));
        }
        catch (Exception e)
        {
            return -1;
        }
    }
}
