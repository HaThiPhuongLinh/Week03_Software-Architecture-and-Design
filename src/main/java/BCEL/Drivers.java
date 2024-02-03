package BCEL;
import java.io.File;
import java.util.List;

public class Drivers {
    public static void main(String[] args) throws Exception {
        LCOM4Calculation calculation = new LCOM4Calculation();
        File file = new File("build/classes/java/main/ReportGenerator.class");
                List< Group > lst = calculation.loadGroups(file);
        lst.forEach(System.out::println);
        int lcom4 = calculation.loadGroups(file).size();
        System.out.printf("LCOM4 of class %s is %d\n", file.getName(), lcom4);
    }
}
