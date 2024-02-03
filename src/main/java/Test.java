import jdepend.xmlui.JDepend;

import java.io.*;

public class Test {
    public static void main(String[] args) throws IOException {
        String proDir = "D:\\JDepend";
        String XMLFilePath = "jdepend.xml";

        File xmlFile = new File(XMLFilePath);
        if(!xmlFile.exists()){
            xmlFile.createNewFile();
        }

        try (PrintStream out = new PrintStream(new FileOutputStream(xmlFile))) {
            System.setOut(out);

            JDepend depend = new JDepend();
            depend.addDirectory(proDir);
            depend.analyze();
        }
    }

}
