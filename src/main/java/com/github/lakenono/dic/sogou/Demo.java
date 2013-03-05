package com.github.lakenono.dic.sogou;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.List;
import java.util.Map;

/**
 * Class description
 *
 */
public class Demo {

    public static void parseWords(File baseDir, String outputDir) {

        File[] childs = baseDir.listFiles();

        for (File child : childs) {

            if (child.isDirectory()) {

                parseWords(child, outputDir);

            } else {

                if (child.getName().endsWith(".scel")) {

                    System.out.println("start parse " + child.getName());

                    try {

                        File dic = new File(outputDir + "/" + child.getName() + "-" + System.currentTimeMillis()
                                            + ".dic");

                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dic),
                                                    "utf-8"));

                        SougouScelMdel model = new SougouScelReader().read(child); 

                        Map<String, List<String>> words = model.getWordMap();    // 词<拼音,词>\

                        int total = 0;

                        for (Map.Entry<String, List<String>> entry : words.entrySet()) {

                            for (String s : entry.getValue()) {

//                                System.out.println(s);

                                writer.write(s +" ");
                                writer.newLine();

                                total++;

                            }

                        }

                        writer.close();

                        System.out.println("parse " + total + " words");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

        }

    }

    public static void main(String[] args) throws IOException {

        File baseDir = new File("/Volumes/lake/tmp/sogou/");

        String outputDir = "/Volumes/lake/tmp/sogou/";

        parseWords(baseDir, outputDir);

    }
}
