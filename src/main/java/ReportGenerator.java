import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.table.TableColumnModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ReportGenerator extends JFrame {

    private JTable tblReport;
    private DefaultTableModel modelTblReport;

    public ReportGenerator() {
        setTitle("Report Table");
        setSize(1400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        JLabel lblTitle = new JLabel("REPORT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 25));
        lblTitle.setBounds(630,10,300,50);
        add(lblTitle);

        JLabel lblFile = new JLabel("File: results.xml");
        lblFile.setFont(new Font("Arial", Font.PLAIN, 15));
        lblFile.setBounds(50,60,200,50);
        add(lblFile);

        JLabel lblRecipe = new JLabel("I = Ce/(Ce+Ca)");
        lblRecipe.setFont(new Font("Arial", Font.PLAIN, 15));
        lblRecipe.setBounds(600,60,200,50);
        add(lblRecipe);

        JLabel lblStatus = new JLabel("0-0.3: Stable || 0.7-1: Bad ");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 15));
        lblStatus.setBounds(850,60,200,50);
        add(lblStatus);

        String[] columns = {"Package", "Ca", "Ce", "I", "A", "Status", "Action"};
        modelTblReport = new DefaultTableModel(columns, 0);

        tblReport = new JTable(modelTblReport);
        tblReport.setFont(new Font("Arial", Font.PLAIN, 14));
        tblReport.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));
        tblReport.getTableHeader().setForeground(Color.BLUE);
        tblReport.setRowHeight(30);
        tblReport.setShowGrid(true);

        JScrollPane scrollPane = new JScrollPane(tblReport);
        scrollPane.setBounds(30,110,1330,400);

        TableColumnModel tcm = tblReport.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(180);
        tcm.getColumn(1).setPreferredWidth(40);
        tcm.getColumn(2).setPreferredWidth(40);
        tcm.getColumn(3).setPreferredWidth(40);
        tcm.getColumn(4).setPreferredWidth(40);
        tcm.getColumn(5).setPreferredWidth(40);
        tcm.getColumn(6).setPreferredWidth(250);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tcm.getColumn(1).setCellRenderer(centerRenderer);
        tcm.getColumn(2).setCellRenderer(centerRenderer);
        tcm.getColumn(3).setCellRenderer(centerRenderer);
        tcm.getColumn(4).setCellRenderer(centerRenderer);
        tcm.getColumn(5).setCellRenderer(centerRenderer);

        add(scrollPane);
        populateTable("results.xml");
    }

    private void populateTable(String xmlFilePath) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(xmlFilePath);

            NodeList packageNodes = document.getElementsByTagName("Package");
            for (int i = 0; i < packageNodes.getLength(); i++) {
                Element packageElement = (Element) packageNodes.item(i);
                String packageName = packageElement.getAttribute("name");

                NodeList statsNodes = packageElement.getElementsByTagName("Stats");

                if (!packageName.isEmpty() && packageName.contains("edu.iuh") && statsNodes.getLength() > 0) {
                    Element statsElement = (Element) statsNodes.item(0);
                    NodeList statElements = statsElement.getChildNodes();

                    double iValue = -1;
                    double caValue = -1;
                    double ceValue = -1;
                    double aValue = -1;

                    for (int j = 0; j < statElements.getLength(); j++) {
                        if (statElements.item(j) instanceof Element) {
                            Element stat = (Element) statElements.item(j);
                            String statName = stat.getNodeName();
                            String statValue = stat.getTextContent();

                            switch (statName) {
                                case "I":
                                    iValue = Double.parseDouble(statValue);
                                    break;
                                case "Ca":
                                    caValue = Double.parseDouble(statValue);
                                    break;
                                case "Ce":
                                    ceValue = Double.parseDouble(statValue);
                                    break;
                                case "A":
                                    aValue = Double.parseDouble(statValue);
                                    break;
                            }
                        }
                    }

                    String status;
                    String action;
                    if (iValue >= 0 && iValue < 0.4) {
                        status = "Stable";
                        action = "";
                    } else if (iValue > 0.6 && iValue <= 1) {
                        status = "Bad";
                        action = "Reduce Ce or increase Ca (or both)";
                    } else {
                        status = "Simple";
                        action = "Reduce Ce or increase Ca (or both)";
                    }


                    Object[] rowData = {packageName, caValue, ceValue, iValue, aValue, status, action};
                    modelTblReport.addRow(rowData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReportGenerator reportGenerator = new ReportGenerator();
            reportGenerator.setVisible(true);
        });
    }
}
