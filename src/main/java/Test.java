import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import jdepend.framework.PackageFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class Test {
    public static void main(String[] args) throws IOException {
        JDepend jDepend = new JDepend();
        jDepend.addDirectory("D:\\TestJDepend");

        Collection<JavaPackage> cols = jDepend.analyze();
        cols.forEach(pkg -> {
            System.out.printf("Package %s, Ca = %d, Ce = %d; \n", pkg.getName(), pkg.getAfferents().size(), pkg.getEfferents().size());
        });

        try (PrintWriter writer = new PrintWriter("results.xml")) {
            jdepend.xmlui.JDepend xml = new jdepend.xmlui.JDepend(writer);
            xml.addDirectory("D:\\TestJDepend");

            PackageFilter filter = PackageFilter.all();
            //filter.including("edu.iuh");
            filter.accept("edu.iuh");
            filter.excluding("com", "org", "java", "javax");

            xml.setFilter(filter);
            xml.analyze();
        }

    }

}
