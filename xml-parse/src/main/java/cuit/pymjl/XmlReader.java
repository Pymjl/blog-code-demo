package cuit.pymjl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/3 21:59
 **/
public class XmlReader {
    public static void main(String[] args) {
        parse();

    }

    public static void parse() {
        // students的内容为上面所示XML代码内容
        File file = new File("C:\\Users\\Admin\\JavaProjects\\blog-code-demo\\xml-parse\\src\\main\\resources\\spring.xml");
        try {
            // 创建文档解析的对象
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 解析文档，形成文档树，也就是生成Document对象
            Document document = builder.parse(file);

            // 获得根节点
            Element rootElement = document.getDocumentElement();
            System.out.printf("Root Element: %s\n", rootElement.getNodeName());

            // 获得根节点下的所有子节点
            NodeList students = rootElement.getChildNodes();
            for (int i = 0; i < students.getLength(); i++) {
                // 获得第i个子节点
                Node childNode = students.item(i);
                // 由于节点多种类型，而一般我们需要处理的是元素节点
                // 元素节点就是非空的子节点，也就是还有孩子的子节点
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element childElement = (Element) childNode;
                    System.out.printf(" Element: %s\n", childElement.getNodeName());
                    System.out.printf("  Attribute: id = %s\n", childElement.getAttribute("id"));
                    System.out.printf("  Value: class= %s\n", childElement.getAttribute("class"));
                    // 获得第二级子元素
                    NodeList childNodes = childElement.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node child = childNodes.item(j);
                        if (child.getNodeType() == Node.ELEMENT_NODE) {
                            Element eChild = (Element) child;
                            System.out.printf("  sub Element %s:  name= %s value= %s\n", eChild.getNodeName(), eChild.getAttribute("name"),
                                    eChild.getAttribute("value"));
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
