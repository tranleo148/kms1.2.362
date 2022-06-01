package provider;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MapleData implements MapleDataEntity, Iterable<MapleData> {
  private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
  
  private Node node;
  
  private File imageDataDir;
  
  private MapleData(Node node) {
    this.node = node;
  }
  
  public MapleData(FileInputStream fis, File imageDataDir) {
    try {
      this.node = documentBuilderFactory.newDocumentBuilder().parse(fis).getFirstChild();
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } 
    this.imageDataDir = imageDataDir;
  }
  
  public MapleData getChildByPath(String path) {
    String[] segments = path.split("/");
    if (segments[0].equals(".."))
      return ((MapleData)getParent()).getChildByPath(path.substring(path.indexOf("/") + 1)); 
    Node myNode = this.node;
    for (int x = 0; x < segments.length; x++) {
      NodeList childNodes = myNode.getChildNodes();
      boolean foundChild = false;
      for (int i = 0; i < childNodes.getLength(); i++) {
        Node childNode = childNodes.item(i);
        if (childNode != null && childNode.getAttributes() != null && childNode.getAttributes().getNamedItem("name") != null && childNode.getNodeType() == 1 && childNode.getAttributes().getNamedItem("name").getNodeValue().equals(segments[x])) {
          myNode = childNode;
          foundChild = true;
          break;
        } 
      } 
      if (!foundChild)
        return null; 
    } 
    MapleData ret = new MapleData(myNode);
    ret.imageDataDir = (new File(this.imageDataDir, getName() + "/" + path)).getParentFile();
    return ret;
  }
  
  public List<MapleData> getChildren() {
    List<MapleData> ret = new ArrayList<>();
    NodeList childNodes = this.node.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node childNode = childNodes.item(i);
      if (childNode != null && childNode.getNodeType() == 1) {
        MapleData child = new MapleData(childNode);
        child.imageDataDir = new File(this.imageDataDir, getName());
        ret.add(child);
      } 
    } 
    return ret;
  }
  
  public Object getData() {
    NamedNodeMap attributes = this.node.getAttributes();
    MapleDataType type = getType();
    switch (type) {
      case DOUBLE:
        return Double.valueOf(Double.parseDouble(attributes.getNamedItem("value").getNodeValue()));
      case FLOAT:
        return Float.valueOf(Float.parseFloat(attributes.getNamedItem("value").getNodeValue()));
      case INT:
        return Integer.valueOf(Integer.parseInt(attributes.getNamedItem("value").getNodeValue()));
      case SHORT:
        return Short.valueOf(Short.parseShort(attributes.getNamedItem("value").getNodeValue()));
      case LONG:
        return Long.valueOf(Long.parseLong(attributes.getNamedItem("value").getNodeValue()));
      case STRING:
      case UOL:
        return attributes.getNamedItem("value").getNodeValue();
      case VECTOR:
        return new Point(Integer.parseInt(attributes.getNamedItem("x").getNodeValue()), Integer.parseInt(attributes.getNamedItem("y").getNodeValue()));
      case CANVAS:
        return new MapleCanvas(Integer.parseInt(attributes.getNamedItem("width").getNodeValue()), Integer.parseInt(attributes.getNamedItem("height").getNodeValue()), new File(this.imageDataDir, getName() + ".png"));
    } 
    return null;
  }
  
  public final MapleDataType getType() {
    String nodeName = this.node.getNodeName();
    if (nodeName.equals("imgdir"))
      return MapleDataType.PROPERTY; 
    if (nodeName.equals("canvas"))
      return MapleDataType.CANVAS; 
    if (nodeName.equals("convex"))
      return MapleDataType.CONVEX; 
    if (nodeName.equals("sound"))
      return MapleDataType.SOUND; 
    if (nodeName.equals("uol"))
      return MapleDataType.UOL; 
    if (nodeName.equals("double"))
      return MapleDataType.DOUBLE; 
    if (nodeName.equals("float"))
      return MapleDataType.FLOAT; 
    if (nodeName.equals("int"))
      return MapleDataType.INT; 
    if (nodeName.equals("short"))
      return MapleDataType.SHORT; 
    if (nodeName.equals("long"))
      return MapleDataType.LONG; 
    if (nodeName.equals("string"))
      return MapleDataType.STRING; 
    if (nodeName.equals("vector"))
      return MapleDataType.VECTOR; 
    if (nodeName.equals("null"))
      return MapleDataType.IMG_0x00; 
    return null;
  }
  
  public MapleDataEntity getParent() {
    Node parentNode = this.node.getParentNode();
    if (parentNode.getNodeType() == 9)
      return null; 
    MapleData parentData = new MapleData(parentNode);
    parentData.imageDataDir = this.imageDataDir.getParentFile();
    return parentData;
  }
  
  public String getName() {
    return this.node.getAttributes().getNamedItem("name").getNodeValue();
  }
  
  public Iterator<MapleData> iterator() {
    return getChildren().iterator();
  }
}
