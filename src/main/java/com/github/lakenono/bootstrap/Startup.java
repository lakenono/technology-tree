package com.github.lakenono.bootstrap;

public class Startup
{
    public static void main(String[] args)
    {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                System.out.println("clean!");
            }

        });

        new Thread()
        {
            public void run()
            {

                while (true)
                {
                    System.out.println("work!");
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            };
        }.start();

    }
}
