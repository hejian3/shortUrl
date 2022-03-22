package com.su.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.su.service.IShortUrlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author hejian
 * 短域名服务-实现
 */
@Service
public class ShortUrlServiceImpl implements IShortUrlService {

    @Value("${su.prefix}")
    private String prefix;

    private Cache<String, String> shortUrlCache =
            CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(1000).build();

    private Cache<String, String> longUrlCache =
            CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(1000).build();

    @Override
    public String getShortUrl(String url) {
        if(longUrlCache.asMap().containsKey(url)){
            return longUrlCache.asMap().get(url);
        }
        String surl = url;
        while (true) {
            HashCode hashCode = Hashing.murmur3_32().hashString(surl, StandardCharsets.UTF_8);
            String shortUrl = prefix + hashCode.toString();
            if (!shortUrlCache.asMap().values().contains(shortUrl)) {
                shortUrlCache.put(shortUrl, url);
                longUrlCache.put(url,shortUrl);
                return shortUrl;
            }
            surl += System.currentTimeMillis();
        }
    }

    @Override
    public String getLongUrl(String shortUrl) {
        if (shortUrlCache.asMap().containsKey(shortUrl)) {
            return shortUrlCache.asMap().get(shortUrl);
        }
        throw new RuntimeException(shortUrl + " 非法");
    }
}
