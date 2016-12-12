import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xdgf.usermodel.XDGFCell;
import org.apache.poi.xdgf.usermodel.XDGFPage;
import org.apache.poi.xdgf.usermodel.XDGFShape;
import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by opanahi on 12/9/2016.
 */
public class MyVisioImport {

    public static void  main (String[] args){
        try {
           /* FileOutputStream fileOut = new FileOutputStream("C:\\Users\\opanahi\\Desktop");
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("POI Worksheet");*/

            //POIDataSamples diagrams =  POIDataSamples.getDiagramInstance();

            String fileName ="C:/Users/opanahi/Desktop/BPMN2.0project/justRec.vsdx";
            File myfile = new File("C:/Users/opanahi/Desktop/BPMN2.0project/exampleofBPMN2.vsdx");
            OPCPackage opcPackage=OPCPackage.open(new FileInputStream((fileName)));

            XmlVisioDocument xmlViso= new XmlVisioDocument(opcPackage);

            int n=0;
            for (XDGFPage page: xmlViso.getPages()){
                System.out.println(page.getPageSheet().getDocument().getXmlObject().getStyleSheets().getStyleSheetList().toString());
              //  System.out.println(page.getPageSheet().getCell("GradientStopPosition").getValue());

               // System.out.println("end of pege end of page end of page end of page:"+n);
                n++;
            }




            if(xmlViso!=null)
            {
                System.out.println(xmlViso.getPackagePart().getPartName().getName());
            }
         /*Iterator<XDGFPage> it=xmlViso.getPages().iterator();

            while (it.hasNext()){
                System.out.println(it.next().getName());
            }

            System.out.println(xmlViso.toString());*/
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

    }
}
