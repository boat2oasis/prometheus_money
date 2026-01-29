package com.prometheus.money.generator;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;
import java.util.List;

public class V2phLoginScraper {

    public static void main(String[] args) throws Exception {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);

        // ① 先打开登录页
        driver.get("https://www.v2ph.com/login");

        System.out.println("请在浏览器中手动登录，登录完成后按 Enter 继续...");
        System.in.read(); // 阻塞，等你登录完成

        int page = 1;

        while (true) {
            String url = "https://www.v2ph.com/album/TouTiao-S026?hl=fr&page=" + page;
            System.out.println("抓取第 " + page + " 页");

            driver.get(url);
            Thread.sleep(3000); // 等 JS 加载

            List<WebElement> images = driver.findElements(By.tagName("img"));
            if (images.isEmpty()) {
                System.out.println("没有图片，结束。");
                break;
            }

            for (WebElement img : images) {
                String src = img.getAttribute("src");
                if (src != null && src.startsWith("http")) {
                    downloadImage(src);
                }
            }

            page++;
            Thread.sleep(1500);
        }

        driver.quit();
    }

    private static void downloadImage(String imgUrl) {
        try {
            Files.createDirectories(Paths.get("images"));
            String name = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);

            try (InputStream in = new URL(imgUrl).openStream()) {
                Files.copy(in, Paths.get("images", name),
                        StandardCopyOption.REPLACE_EXISTING);
                System.out.println("下载: " + name);
            }
        } catch (Exception e) {
            System.err.println("失败: " + imgUrl);
        }
    }
}
