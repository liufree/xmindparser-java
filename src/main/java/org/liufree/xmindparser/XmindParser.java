package org.liufree.xmindparser;

import com.alibaba.fastjson.JSON;
import org.apache.commons.compress.archivers.ArchiveException;
import org.dom4j.DocumentException;
import org.json.JSONObject;
import org.liufree.xmindparser.pojo.JsonRootBean;

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

    /**
     * 解析脑图文件，返回content整合后的内容
     *
     * @param xmindFile
     * @return
     * @throws IOException
     * @throws ArchiveException
     * @throws DocumentException
     */
    public static String parseJson(String xmindFile) throws IOException, ArchiveException, DocumentException {
        String res = ZipUtils.extract(xmindFile);

        String content = null;
        if (isXmindZen(res, xmindFile)) {
            content = getXmindZenContent(xmindFile);
        } else {
            content = getXmindLegacyContent(xmindFile);
        }

        //删除生成的文件夹
        File dir = new File(res);
        boolean flag = deleteDir(dir);
        if (flag) {
            // do something
        }
        JsonRootBean jsonRootBean = JSON.parseObject(content, JsonRootBean.class);
       return(JSON.toJSONString(jsonRootBean,false));
    }

    public static Object parseObject(String xmindFile) throws DocumentException, ArchiveException, IOException {
        String content = parseJson(xmindFile);
        JsonRootBean jsonRootBean = JSON.parseObject(content, JsonRootBean.class);
        return jsonRootBean;
    }


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


    /**
     * @return
     */
    public static String getXmindZenContent(String xmindFile) throws IOException, ArchiveException {
        List<String> keys = new ArrayList<>();
        keys.add(xmindZenJson);
        Map<String, String> map = ZipUtils.getContents(keys, xmindFile);
        String content = map.get(xmindZenJson);
        content = content.substring(1, content.lastIndexOf("]"));
        content = XmindZen.getContent(content);
        return content;
    }

    /**
     * @return
     */
    public static String getXmindLegacyContent(String xmindFile) throws IOException, ArchiveException, DocumentException {
        List<String> keys = new ArrayList<>();
        keys.add(xmindLegacyContent);
        keys.add(xmindLegacyComments);
        Map<String, String> map = ZipUtils.getContents(keys, xmindFile);

        String contentXml = map.get(xmindLegacyContent);
        String commentsXml = map.get(xmindLegacyComments);
        String xmlContent = XmindLegacy.getContent(contentXml, commentsXml);

        return xmlContent;
    }


    private static boolean isXmindZen(String res, String xmindFile) throws IOException, ArchiveException {
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

   /* public static void main(String[] args) throws IOException, ArchiveException, DocumentException {
        String fileName = "doc/XmindZen解析.xmind";
        XmindParser xmindParser = new XmindParser();
        String res = xmindParser.parser(fileName);
        JsonRootBean jsonRootBean = JSON.parseObject(res, JsonRootBean.class);
        System.out.println(jsonRootBean);
    }*/

}
