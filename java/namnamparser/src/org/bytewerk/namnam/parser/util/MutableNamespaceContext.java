package org.bytewerk.namnam.parser.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;

/**
 * this is from http://blog.oroup.com/2006/11/05/the-joys-of-screenscraping/
 * thanks oliver!
 *
 * There is a bug in the JDK which omits the setNamespace declaration
 * from implementations of NamespaceContext. We have to create our
 * own implementation to work around it. Documented here:
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5101859
 * @author Oliver Roup <oroup@oroup.com>
 */
public class MutableNamespaceContext implements NamespaceContext {

  private Map<String,String> map;

  public MutableNamespaceContext() {
    map = new HashMap<String,String>();
  }

  public void setNamespace(String prefix, String namespaceURI) {
    map.put(prefix, namespaceURI);
  }

  public String getNamespaceURI(String prefix) {
    return map.get(prefix);
  }

  public String getPrefix(String namespaceURI) {
    for (String prefix : map.keySet()) {
      if (map.get(prefix).equals(namespaceURI)) {
        return prefix;
      }
    }
    return null;
  }

  public Iterator getPrefixes(String namespaceURI) {
    List<String> prefixes = new ArrayList<String>();
    for (String prefix : map.keySet()) {
      if (map.get(prefix).equals(namespaceURI)) {
        prefixes.add(prefix);
      }
    }
    return prefixes.iterator();
  }
}