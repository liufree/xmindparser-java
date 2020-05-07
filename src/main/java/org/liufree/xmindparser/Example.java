package org.liufree.xmindparser;

import com.alibaba.fastjson.JSON;
import org.apache.commons.compress.archivers.ArchiveException;
import org.dom4j.DocumentException;
import org.liufree.xmindparser.pojo.JsonRootBean;

import java.io.IOException;

/**
 * @author liufree liufreeo@gmail.com
 * @Classname Example
 * @Description 测试例子
 * @Date 2020/4/28 13:12
 */
public class Example {

    public static void main(String[] args) throws DocumentException, ArchiveException, IOException {
       // String fileName = "doc/XmindZen解析.xmind";
        String fileName = "doc/CheckDiagram0507.xmind";
        String res = XmindParser.parseJson(fileName);
        System.out.println(res);

        Object root = XmindParser.parseObject(fileName);
        System.out.println(root);



    }


}
