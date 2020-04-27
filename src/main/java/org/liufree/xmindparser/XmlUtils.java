package org.liufree.xmindparser;

import com.alibaba.fastjson.JSON;
import org.dom4j.*;
import org.json.JSONObject;
import org.json.XML;
import org.liufree.xmindparser.json.JsonRootBean;

import java.io.*;
import java.util.List;

/**
 * @author liufree liufreeo@gmail.com
 * @Classname XmlUtils
 * @Description TODO
 * @Date 2020/4/27 23:56
 */
public class XmlUtils {

    public static String getContent(String xmlContent) throws IOException, DocumentException {
        //删除里面不能识别的字符串
        xmlContent = xmlContent.replace("xmlns=\"urn:xmind:xmap:xmlns:content:2.0\"", "");
        xmlContent = xmlContent.replace("xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"", "");
        xmlContent = xmlContent.replace("<topics type=\"attached\">", "");
        xmlContent = xmlContent.replace("</topics>","");
        Document document = DocumentHelper.parseText(xmlContent);// 读取XML文件,获得document对象
        Element root = document.getRootElement();
        Node rootTopic = root.selectSingleNode("/xmap-content/sheet/topic");
        rootTopic.setName("rootTopic");
        List<Node> topicList = rootTopic.selectNodes("//topic");
        for (Node node : topicList) {
            node.setName("attached");
        }
        Element sheet = root.element("sheet");
        String res = sheet.asXML();
        //将xml转为json
        JSONObject xmlJSONObj = XML.toJSONObject(res);
        JSONObject jsonObject = xmlJSONObj.getJSONObject("sheet");
        //设置缩进
        String jsonPrettyPrintString = jsonObject.toString(4);
        return jsonPrettyPrintString;
    }
}
