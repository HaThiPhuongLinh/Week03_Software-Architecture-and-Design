import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLParser {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse("results.xml");

            // Parse packages
            NodeList packageNodes = document.getElementsByTagName("Package");
            for (int i = 0; i < packageNodes.getLength(); i++) {
                //change to element object for using special method of element
                Element packageElement = (Element) packageNodes.item(i);
                String packageName = packageElement.getAttribute("name");
                if (!packageName.isEmpty() && packageName.contains("edu.iuh")) {
                    System.out.println("Package: " + packageName);

                    // Parse package statistics
                    NodeList statsNodes = packageElement.getElementsByTagName("Stats");

                    if (statsNodes.getLength() > 0) {
                        Element statsElement = (Element) statsNodes.item(0);
                        NodeList statElements = statsElement.getChildNodes();

                        for (int j = 0; j < statElements.getLength(); j++) {
                            // Check if childNode at index[0] is an XML element
                            if (statElements.item(j) instanceof Element) {
                                // Change to element object
                                Element stat = (Element) statElements.item(j);
                                String statName = stat.getNodeName();
                                String statValue = stat.getTextContent();
                                System.out.println("    " + statName + ": " + statValue);
                            }
                        }
                    }
                }

                //Parse concrete classes
                NodeList concreteClassesNodes = packageElement.getElementsByTagName("ConcreteClasses");
                if (concreteClassesNodes.getLength() > 0) {
                    Element concreteClassesElement = (Element) concreteClassesNodes.item(0);
                    NodeList classNodes = concreteClassesElement.getElementsByTagName("Class");

                    for (int k = 0; k < classNodes.getLength(); k++) {
                        Element classElement = (Element) classNodes.item(k);
                        String className = classElement.getTextContent().trim();
                        System.out.println("   Class: " + className);
                    }
                }

                // Parse dependencies
                NodeList dependsUponNodes = packageElement.getElementsByTagName("DependsUpon");
                if (dependsUponNodes.getLength() > 0) {
                    Element dependsUponElement = (Element) dependsUponNodes.item(0);
                    NodeList dependencyNodes = dependsUponElement.getElementsByTagName("Package");

                    for (int l = 0; l < dependencyNodes.getLength(); l++) {
                        Element dependencyElement = (Element) dependencyNodes.item(l);
                        String dependencyName = dependencyElement.getTextContent().trim();
                        System.out.println("   Depends Upon: " + dependencyName);
                    }
                }

                System.out.println();
            }

            // Parse cycles
            NodeList cyclesNodes = document.getElementsByTagName("Cycles");
            if (cyclesNodes.getLength() > 0) {
                Element cyclesElement = (Element) cyclesNodes.item(0);
                NodeList cycleNodes = cyclesElement.getElementsByTagName("Package");

                for (int m = 0; m < cycleNodes.getLength(); m++) {
                    Element cycleElement = (Element) cycleNodes.item(m);
                    String cycleName = cycleElement.getAttribute("Name");
                    System.out.println("Cycle: " + cycleName);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
