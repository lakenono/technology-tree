package com.github.lakenono.util.htmlunit;

import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

/**
*
* @author lake
* createTime：2013-6-20 下午4:33:03
*/
public class HtmlUnitLogClean
{
    static
    {
        System.out.println("LogClean.. init");

        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
    }
}
