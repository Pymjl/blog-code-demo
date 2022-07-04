package cuit.pymjl;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/4 13:01
 **/
public class SAXHandler extends DefaultHandler {

    @Override
    public void startDocument() throws SAXException {
        System.out.println("====================Starting parse the document=======================");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("=================Ending parse the document=======================");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("bean")) {
            System.out.println("Bean{id=" + attributes.getValue(0) +
                    ",class=" + attributes.getValue(1) + "}");
        } else if (qName.equals("property")) {
            System.out.println("Property{name=" + attributes.getValue(0) +
                    ",value=" + attributes.getValue(1) + "}");
        }
    }

    public static void parser() {
        try {
            File file = new File("C:\\Users\\Admin\\JavaProjects\\blog-code-demo\\xml-parse\\src\\main\\resources\\spring.xml");
            // 创建一个SAX解析器
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            javax.xml.parsers.SAXParser parser = saxParserFactory.newSAXParser();
            // 解析对应的文件
            parser.parse(file, new SAXHandler());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        parser();
    }
}
