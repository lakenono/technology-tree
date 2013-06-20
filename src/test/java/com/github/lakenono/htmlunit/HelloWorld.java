package com.github.lakenono.htmlunit;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class HelloWorld
{
    @Test
    public void staticHtml() throws FailingHttpStatusCodeException, MalformedURLException, IOException
    {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        final HtmlPage page = webClient.getPage("http://www.eucita.com");
        System.out.println(page.getTitleText());

        final String pageAsXml = page.asXml();
        System.out.println(pageAsXml);

        System.out.println("------------");

        final String pageAsText = page.asText();
        System.out.println(pageAsText);

        webClient.closeAllWindows();
    }

    @Test
    public void ajaxHtml() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException
    {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        final HtmlPage page = webClient.getPage("http://item.jd.com/407000.html");

        Thread.sleep(2000);

        System.out.println(page.asXml());
    }

    @Test
    public void login() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException
    {
        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
        final HtmlPage loginPage = webClient.getPage("http://weibo.com/?from=bp");

        Thread.sleep(2000);

        System.out.println(loginPage.asXml());
        System.out.println("----");

        System.out.println(loginPage.getByXPath("/html/body/div/div[2]/div[2]/div[2]/div/div[6]/a"));

        HtmlTextInput username = loginPage.getElementByName("username");
        HtmlPasswordInput password = loginPage.getElementByName("password");
        HtmlAnchor submit = (HtmlAnchor) loginPage.getByXPath("/html/body/div/div[2]/div[2]/div[2]/div/div[6]/a").get(0);

        //        username.setText("1950828049@qq.com");
        //        password.setText("eucita");

        username.setText("2842521380@qq.com");
        password.setText("eucita");

        System.out.println(username);
        System.out.println(password);
        System.out.println(submit);

        submit.mouseUp();
        HtmlPage index = submit.click();

        Thread.sleep(5000);

        System.out.println("----");

        HtmlPage page = webClient.getPage("http://weibo.com/nimui");
        Thread.sleep(5000);

        System.out.println(page.asXml());
        System.out.println(page.asText());
    }
}
