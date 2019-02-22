package com.authority.utils;

import javax.annotation.Resource;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import groovy.util.logging.Slf4j;

@Slf4j
@Component
public class SpiderUtil {
	@Resource
    private DynamicIpUtil dynamicIpUtil;

    /**
     * 根据url爬取页面信息
     *
     * @param url url
     * @return 页面信息
     */
    public Document spiderDocument(String url) {
        Document pageDoc = null;
        try {
            Connection con= Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)")
                    .timeout(5000);
            /*.ignoreHttpErrors(true)
            .followRedirects(true)*/
            Connection.Response resp = con.execute();
            if (resp.statusCode() == 200){
                pageDoc = con.get();
            } else {
                System.out.println("http status error");
                dynamicIpUtil.changeMyIp();
                spiderDocument(url);
            }
            if(pageDoc == null || pageDoc.toString().trim().equals("")) {// 表示ip被拦截或者其他情况
                System.out.println("ip被拦截 无内容");
                dynamicIpUtil.changeMyIp();
                spiderDocument(url);
            }

        } catch (Exception e) {
            System.out.println("ip被拦截 异常: {}"+e);
            dynamicIpUtil.getMyIpInfo();
            dynamicIpUtil.changeMyIp();
            spiderDocument(url);
        }
        if (ipDefensed(url, pageDoc)) {
            // 如果被ip限制了，更换动态ip
            dynamicIpUtil.changeMyIp();
            spiderDocument(url);
        }
        return pageDoc;
    }

    /**
     * 判断ip是否被封
     *
     * @param pageDoc 页面信息
     * @return ip
     */
    private boolean ipDefensed(String url, Document pageDoc) {
        boolean ipDefensed = false;
        if (url.contains("anjuke.com")) {
            ipDefensed = AJKIpDefense(pageDoc);
        }
        return ipDefensed;
    }


    /**
     * 安居客判断ip是否被封
     *
     * @param pageDoc 页面信息
     */
    private boolean AJKIpDefense(Document pageDoc) {
        System.out.println("ip 被拦截 安居客");
        boolean ajkppDefensed = false;
        String title = pageDoc.title();
        if (title.equals("访问验证-安居客")) {
            ajkppDefensed = true;
        }
        return ajkppDefensed;
    }
}
