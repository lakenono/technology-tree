package com.github.lakenono.sina.weibo.login;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class LoginRunner
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        //Hi， 我是留几手！赶快注册微博粉我吧，随时分享我的最新动态！
        String file = LoginRunner.class.getResource("/UserList.txt").getFile();

        List<String> lines = FileUtils.readLines(new File(file));
        for (String line : lines)
        {
            if (line.startsWith("#"))
            {
                continue;
            }

            WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
            HtmlPage loginPage = webClient.getPage("http://weibo.com/?from=bp");

            Thread.sleep(2000);

            HtmlTextInput username = loginPage.getElementByName("username");
            HtmlPasswordInput password = loginPage.getElementByName("password");
            HtmlAnchor submit = (HtmlAnchor) loginPage.getByXPath("/html/body/div/div[2]/div[2]/div[2]/div/div[6]/a").get(0);

            username.setText(line);
            password.setText("eucita");

            submit.mouseUp();
            HtmlPage index = submit.click();

            Thread.sleep(5000);

            System.out.println("----");

            HtmlPage page = webClient.getPage("http://weibo.com/nimui");
            Thread.sleep(5000);

            System.out.println(page.asText());

            webClient.closeAllWindows();

            Thread.sleep(500000);

        }
    }
}
