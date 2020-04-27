/**
  * Copyright 2020 bejson.com 
  */
package org.liufree.xmindparser.json;
import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2020-03-24 11:24:27
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Attached {

    private String id;
    private String title;
    private Notes notes;
    private List<Comments> comments;
    private Children children;


}