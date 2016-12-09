import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xdgf.usermodel.XDGFPage;
import org.apache.poi.xdgf.usermodel.XmlVisioDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by opanahi on 12/9/2016.
 */
public class MyVisioImport {

    public static void  main (String[] args){
        try {
//            POIDataSamples diagrams =  POIDataSamples.getDiagramInstance();
            XmlVisioDocument xmlVisioDocument=null;
            String fileName = "C:/Users/opanahi/Desktop/BPMN2.0project/justRec.vsdx";
            File myfile = new File("C:/Users/opanahi/Desktop/BPMN2.0project/justRec.vsdx");
            OPCPackage opcPackage=OPCPackage.open(new FileInputStream((fileName)));

            xmlVisioDocument = new XmlVisioDocument(opcPackage);
            if(xmlVisioDocument!=null)
            {
                System.out.printf("test");
            }
         Iterator<XDGFPage> it=xmlVisioDocument.getPages().iterator();

            while (it.hasNext()){
                System.out.printf(it.next().getName());
            }

            System.out.println(xmlVisioDocument.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

    }
}
