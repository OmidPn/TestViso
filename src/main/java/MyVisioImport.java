import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.microsoft.schemas.office.visio.x2012.main.SectionType;
import com.sun.org.apache.xml.internal.utils.QName;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xdgf.geom.Dimension2dDouble;
import org.apache.poi.xdgf.usermodel.*;
import org.apache.poi.xdgf.usermodel.shape.ShapeRenderer;
import org.apache.poi.xdgf.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MyVisioImport {
  static BufferedWriter output = null;
   static Writer w=null;
    public static void renderToPng(XDGFPage page, String outFilename, double scale, ShapeRenderer renderer) throws IOException {
        renderToPng(page, new File(outFilename), scale, renderer);
    }

    public static void renderToPngDir(XDGFPage page, File outDir, double scale, ShapeRenderer renderer) throws IOException {

        File pageFile = new File(outDir, "page" + page.getPageNumber() + "-" + Util.sanitizeFilename(page.getName()) + ".png");
        System.out.println("** Writing image to " + pageFile);

        renderToPng(page, pageFile, scale, renderer);

    }

    public static void renderToPng(XDGFPage page, File outFile, double scale, ShapeRenderer renderer) throws IOException {

        Dimension2dDouble sz = page.getPageSize();

        int width = (int)(scale * sz.getWidth());
        int height = (int)(scale * sz.getHeight());

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics = img.createGraphics();

        // default rendering options
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        graphics.setColor(Color.black);
        graphics.setBackground(Color.white);
        graphics.clearRect(0, 0, width, height);

        // Visio's coordinate system is flipped, so flip the image vertically
        graphics.translate(0,  img.getHeight());
        graphics.scale(scale, -scale);

        // toplevel shapes only
        renderer.setGraphics(graphics);
        page.getContent().visitShapes(renderer);

        graphics.dispose();

        FileOutputStream out = new FileOutputStream(outFile);
        ImageIO.write(img, "png", out);
        out.close();
    }

    public static void renderToPng(XmlVisioDocument document, String outDirname, double scale, ShapeRenderer renderer) throws IOException {

        File outDir = new File(outDirname);

        for (XDGFPage page: document.getPages()) {
            renderToPngDir(page, outDir, scale, renderer);
        }
    }

    public static void main(String [] args) throws Exception {
        try {
            File startWrting = new File("C:/Users/opanahi/Desktop/BPMN2.0 project/visioOut.txt");
            FileOutputStream is = new FileOutputStream(startWrting);
            OutputStreamWriter osw = new OutputStreamWriter(is);
             w = new BufferedWriter(osw);

            String fileName ="C:/Users/opanahi/Desktop/BPMN2.0 project/simpleflow.vsdx";

            String fileName1="C:/Users/opanahi/Desktop/BPMN2.0 project/exampleofBPMN2.vsdx";
            String fileName2= "C:/Users/opanahi/Desktop/BPMN2.0 project/start_Task.vsdx";


            XmlVisioDocument xmlViso= new XmlVisioDocument(OPCPackage.open(new FileInputStream((fileName))));
            getAllObject(xmlViso);
            w.close();
        }
        catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }//end of main
    public static void getAllObject(XmlVisioDocument xmlVisio) throws IOException {

        for (XDGFPage page: xmlVisio.getPages()){
            int i=1;

            XDGFConnection xdgfConnection  =page.getContent().getConnections().get(0);
            System.out.println(xdgfConnection.getToCellName());
            System.out.println(xdgfConnection.getToShape().getXmlObject().getName().toString());
            System.out.println(xdgfConnection.getFromPart().toString());

            for (Map.Entry<Long, XDGFShape> visioElement:page.getContent().getShapesMap().entrySet()) {
                if (visioElement.getValue().getXmlObject().getName() != null) {
                    System.out.println("\n\n----------------------------Object No:"+ i+"  ----------------------------------------------");
                    w.append("\n\n----------------------------Object No:"+ i+"  ----------------------------------------------\n");
                    System.out.println("Object Name:" + getObjectName(visioElement.getValue().getSymbolName()));
                     w.append("\nObject Name:"+getObjectName(visioElement.getValue().getSymbolName()));
                    System.out.println("Id :" + visioElement.getValue().getXmlObject().getID());
                    w.append("\nId :" + visioElement.getValue().getXmlObject().getID() + "\n");
                    SectionType  mysection [] = visioElement.getValue().getXmlObject().getSectionArray();
                    if(mysection.length>0) {

                        for(int j=0;j<mysection.length;j++)
                        {
                            if(isAction(mysection[j])){
                                for (int m=0;m<mysection[j].getDomNode().getChildNodes().getLength();m++){
                                    if(mysection[j].getDomNode().getChildNodes().item(m).getFirstChild().getAttributes().item(2).getNodeValue().equals("1")){
                                        System.out.println("task type: "+mysection[j].getDomNode().getChildNodes().item(m).getAttributes().item(0).getNodeValue());
                                        w.append("\ntask type: "+mysection[j].getDomNode().getChildNodes().item(m).getAttributes().item(0).getNodeValue()+"\n");
                                    }
                                }
                            }//end of if it is Action


                            if(isProperty(mysection[j])){
                                for (int m=0;m<mysection[j].getDomNode().getChildNodes().getLength();m++){
                                    if(mysection[j].getDomNode().getChildNodes().item(m).getFirstChild().getAttributes().item(2).getNodeValue().equals("1")){
                                        System.out.println("task type: "+mysection[j].getDomNode().getChildNodes().item(m).getAttributes().item(0).getNodeValue());
                                        w.append("\ntask type: "+mysection[j].getDomNode().getChildNodes().item(m).getAttributes().item(0).getNodeValue()+"\n");
                                    }

                                }
                            }//end of if it is Action
//

                        }
                    }

                    //show xml file
                   // System.out.println(visioElement.getValue().getXmlObject());

                    System.out.println("-------------------------------------------------------------------------------");
                    i++;
                }

            }

        }


    }
    public static void doCProp(XDGFDocument parts){
        System.out.println(parts.getXmlObject().toString());

    }
    // flowObject
    public static String getObjectName(String flowobjectName){
        if (flowobjectName.equals("Task"))return "Task";
        else if (flowobjectName.equals("End Event"))return "End Event";
        else if (flowobjectName.equals("Intermediate Event"))return "Intermediate Event";
        else if (flowobjectName.equals("Gateway"))return "Gateway";
        else if (flowobjectName.equals("Rectangle"))return "Rectangle";
        else if (flowobjectName.equals("Triangle"))return "Triangle";
        else if (flowobjectName.equals("Dynamic Connector"))return "Dynamic Connector";
        else if (flowobjectName.equals("Collapsed Sub-Process.14"))return "Collapsed Sub-Process.14";
        else if (flowobjectName.equals("Start Event"))return "Start Event";
        else if (flowobjectName.equals("Sequence Flow"))return "Sequence Flow";

        else return "NotDefined";
    }



    //select sections
    public static boolean isProperty(SectionType  sectionType){
        return(sectionType.getN().matches("Property"));
    }
    public static boolean isAction(SectionType  sectionType){
        return(sectionType.getN().matches("Actions"));
    }
    public static boolean isShape(SectionType  sectionType){
        return(sectionType.getN().matches("Geometry"));
    }

//    public VisioAcion findAction(SectionType  sectionType){
//		VisioAcion visioAcion=new VisioAcion();
//		if(isAction(sectionType))
//		 for (int i=0;i<sectionType.getDomNode().getChildNodes().getLength();i++){
//          if(sectionType.getDomNode().getChildNodes().item(i).getFirstChild().getChildNodes().item(2).getNodeValue().equals("1")){
//           visioAcion.setValue(sectionType.getDomNode().getChildNodes().item(i).getFirstChild().getNodeValue());
//		  }
//		}
//      return visioAcion;
//	}
    /// to get node and childs
    //mysection[j].getDomNode().getChildNodes().item(1).getFirstChild().getAttributes().item(3).getNodeValue()
    //mysection[j].getDomNode().getChildNodes().item(0).getAttributes().item(0).getNodeValue()

    //public static void

}