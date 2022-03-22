package com.su.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class ShortUrlServiceImplTest {

    @Autowired
    private IShortUrlService shortUrlService;

    @Test
    public void getShortUrl() {
        shortUrlService.getShortUrl("http://www.baidu.com");
        shortUrlService.getShortUrl("http://www.baidu.com");
    }

    @Test
    public void getLongUrl() {

        Assertions.assertThrows(RuntimeException.class,
                () -> shortUrlService.getLongUrl("aaaa"));

        String longUrl = "http://www.baidu.com";
        String url = shortUrlService.getShortUrl(longUrl);
        Assert.isTrue(longUrl.equals(shortUrlService.getLongUrl(url)));
    }
}
