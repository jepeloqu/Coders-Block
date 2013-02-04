package com.github.jepeloqu.codersblock;



import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Level {
   private int width, height;
   int[][] level;
   private String levelData;
   
   public Level() {
      extractLevel();
   }
   
   private void extractLevel()  {
      
      DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
      domFactory.setNamespaceAware(true);
      try {
         DocumentBuilder builder = domFactory.newDocumentBuilder();
         Document doc = builder.parse(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "levels" + File.separator + "Level2.tmx");
      
         XPathFactory factory = XPathFactory.newInstance();
         XPath xpath = factory.newXPath();
         XPathExpression levelWidthExpr = xpath.compile("//map/@width");
         XPathExpression levelHeightExpr = xpath.compile("//map/@height");
         XPathExpression levelDataExpr = xpath.compile("//data/text()");
      
         //extract width
         Object result = levelWidthExpr.evaluate(doc, XPathConstants.NODESET);
         NodeList nodes = (NodeList) result;
         width = Integer.parseInt(nodes.item(0).getNodeValue());
         
         //extract height
         result = levelHeightExpr.evaluate(doc, XPathConstants.NODESET);
         nodes = (NodeList) result;
         height = Integer.parseInt(nodes.item(0).getNodeValue());
         
         //extract level data
         result = levelDataExpr.evaluate(doc, XPathConstants.NODESET);
         nodes = (NodeList) result;
         levelData = nodes.item(0).getNodeValue();
         
         
         String text = nodes.item(0).getNodeValue();
         String delims = "[\n]";
         String[] tokens1 = text.replaceFirst("\n", "").split(delims);
         
         delims = "[,]";
         
         level = new int[height][width];
         String[] tokens2;
  
         for (int i = 0; i < height; i++) {
            tokens2 = tokens1[i].split(delims);
            
            for (int j = 0; j < width; j++) {
               level[i][j] = Integer.parseInt(tokens2[j]);
            }
         }
      } catch (SAXException | IOException | ParserConfigurationException | XPathExpressionException e) {
         System.out.println(e.toString());
      }
      
   }  
}
