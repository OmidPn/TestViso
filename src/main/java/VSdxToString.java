import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xdgf.geom.Dimension2dDouble;
import org.apache.poi.xdgf.usermodel.*;
import org.apache.poi.xdgf.usermodel.shape.ShapeDebuggerRenderer;
import org.apache.poi.xdgf.usermodel.shape.ShapeRenderer;
import org.apache.poi.xdgf.util.Util;
import org.apache.poi.xdgf.xml.XDGFXMLDocumentPart;

public class VSdxToString {

	
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

			String fileName ="C:/Users/opanahi/Desktop/BPMN2.0 project/justRec.vsdx";

		    String fileName1="C:/Users/opanahi/Desktop/BPMN2.0 project/exampleofBPMN2.vsdx";
		    String fileName2= "C:/Users/opanahi/Desktop/BPMN2.0 project/start_Task.vsdx";


			XmlVisioDocument xmlViso= new XmlVisioDocument(OPCPackage.open(new FileInputStream((fileName))));
			getShapeName(xmlViso);

		}
		catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}//end of main
	public static void getShapeName(XmlVisioDocument xmlVisio){
		for (XDGFPage page: xmlVisio.getPages())
			for (Map.Entry<Long, XDGFShape> visioElement:page.getContent().getShapesMap().entrySet()) {
				System.out.println(visioElement.getValue().getSymbolName());
			}

	}
	public static void doCProp(XDGFDocument parts){
		System.out.println(parts.getXmlObject().toString());


	}
}