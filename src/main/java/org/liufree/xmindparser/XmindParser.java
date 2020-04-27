package org.liufree.xmindparser;

import com.alibaba.fastjson.JSON;
import org.apache.commons.compress.archivers.ArchiveException;
import org.dom4j.DocumentException;
import org.liufree.xmindparser.json.JsonRootBean;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author liufree liufreeo@gmail.com
 * @Classname XmindParser
 * @Description 解析主体
 * @Date 2020/4/27 14:05
 */
public class XmindParser {
    public static final String xmindZenJson = "content.json";
    public static final String xmindLegacyContent = "content.xml";
    public static final String xmindLegacyComments = "comments.xml";

    public String parser(String xmindFile) throws IOException, ArchiveException, DocumentException {
        String res = ZipUtils.extract(xmindFile);
        if (isXmindZen(res, xmindFile)) {
            // todo zen版本中Notes没有content
            return getXmindZenContent(xmindFile);
        } else {
            return getXmindLegacyContent(xmindFile);
        }
    }

    /**
     * @return
     */
    public String getXmindZenContent(String xmindFile) throws IOException, ArchiveException {
        List<String> keys = new ArrayList<>();
        keys.add(xmindZenJson);
        Map<String, String> map = ZipUtils.getContents(keys, xmindFile);
        String content = map.get(xmindZenJson);
        content = content.substring(1, content.lastIndexOf("]"));
        return content;
    }

    /**
     * @return
     */
    public String getXmindLegacyContent(String xmindFile) throws IOException, ArchiveException, DocumentException {
        List<String> keys = new ArrayList<>();
        keys.add(xmindLegacyContent);
        keys.add(xmindLegacyComments);
        Map<String, String> map = ZipUtils.getContents(keys, xmindFile);

        String contentXml = map.get(xmindLegacyContent);
        String commentsXml = map.get(xmindLegacyComments);
        String xmlContent = XmlUtils.getContent(contentXml);

        return xmlContent;
    }


    private boolean isXmindZen(String res, String xmindFile) throws IOException, ArchiveException {
        //解压
        File parent = new File(res);
        if (parent.isDirectory()) {
            String[] files = parent.list(new ZipUtils.FileFilter());
            for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
                if (files[i].equals(xmindZenJson)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException, ArchiveException, DocumentException {
        String fileName = "doc/XmindZen解析.xmind";
        XmindParser xmindParser = new XmindParser();
        String res = xmindParser.parser(fileName);
        JsonRootBean jsonRootBean = JSON.parseObject(res, JsonRootBean.class);
        System.out.println(jsonRootBean);
    }

}
