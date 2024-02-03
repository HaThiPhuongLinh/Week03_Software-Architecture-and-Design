# Software Package Metrics Analyzer

This project utilizes JDepend and JDOM to analyze and generate reports on the project's package dependencies and structure. The process involves running JDepend to analyze the project, parsing the generated XML files using JDOM, and finally generating a report that includes statistics and actions for packages.

## Dependencies

- [JDepend](#): A tool for measuring and analyzing the package dependencies in Java projects.
  
   ```js
     implementation ("guru.nidi:jdepend:2.9.5")
     ```
- [DOM](#): XML parsing library.


1. **JDepend:**
   - Using:

     ```js
     JDepend jDepend = new JDepend();
     jDepend.addDirectory(fileName);
     jDepend.analyze();
     ```

2. **Parse XML Documents:**
   - Use DOM to parse the generated XML files.

     ```js
     DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
     DocumentBuilder builder = builderFactory.newDocumentBuilder();
     Document document = builder.parse(xmlFilePath);
     ```
## Documentation
#### The meaning and calculation of the software package metrics that it uses. 
- Ce: Efferent couplings, outgoing dependency
- Ca: Afferent couplings, incoming dependency
- I: Instability, the ratio of efferent coupling to the sum of both efferent and afferent coupling. I = Ce/(Ce+Ca). The range for I is 0 to 1.
   - [0-0.3]: indicating a completely stable package
   - [0.7-1] indicating a completely unstable package
- A: Abstractness, the ratio of abstract artifacts (abstract, classes, interfaces, and so on) to concrete artifacts (implementaion). The range for A is 0 to 1
  - Close to 0: indicating a completely concrete package
  - Close to 1: indicating a completely abstract package
## Result
![image](https://github.com/HaThiPhuongLinh/Week03_Software-Architecture-and-Design/assets/109422010/c3d2ae99-a85a-45dd-b622-50a1c5e328c6)

__________
# Task 1 (LCOM)
## ckjm
Uses CKJM to analyze the metrics of a specific Java class file (MetricsFilterTest.class) and prints the results to the console. The output includes the Weighted Methods per Class (WMC) and Lack of Cohesion in Methods (LCOM) metrics for the analyzed class.
```js
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<ClassMetrics> ref = new AtomicReference<>();
        CkjmOutputHandler outputHandler = new CkjmOutputHandler() {
            @Override
            public void handleClass(String name, ClassMetrics c) {
                System.out.println("name: " + name + ", WMC: " + c.getWmc());
                System.out.println("name: " + name + ", LCOM: " + c.getLcom());
                ref.set(c);
                latch.countDown();
            }
        };
        File f = new
                File("build/classes/java/main/ckjm/MetricsFilterTest.class");
        assertTrue("File " + f.getAbsolutePath() + " not present", f.exists());
        MetricsFilter.runMetrics(new String[] { f.getAbsolutePath() }, outputHandler, false);
        latch.await(1, TimeUnit.SECONDS);

        //result:
        //name: ckjm.MetricsFilterTest, WMC: 3
        //name: ckjm.MetricsFilterTest, LCOM: 3
```
## BCEL
1. **Group**
- This class represents a group of fields and methods within a Java class that are interconnected
  
  ```js
    private final Set<String> fields = new HashSet<>();
    private final Set<String> methods = new HashSet<>();
  ```
  - `addFields(String... fields)`: Adds fields to the group.
  - `addMethods(String... methods)`: Adds methods to the group.
  - `fields()`: Returns the number of fields in the group.
  - `methods()`: Returns the number of methods in the group.
  - `intersects(Group other)`: Checks if the group intersects with another group.
    
    ```js
    public boolean intersects(Group other) {
        for (String field: other.fields) {
            if (fields.contains(field)) {
                return true;
            }
        }
        for (String method: other.methods) {
            if (methods.contains(method)) {
                return true;
            }
        }
        return false;
    }
    ```
  - `merge(Group other)`: Merges the current group with another group.
    
    ```js
    public void merge(Group other) {
        fields.addAll(other.fields);
        methods.addAll(other.methods);
    }
    ```
  - `toString()`: Provides a string representation of the group.
2. **LCOM4Calculation**
- This class is responsible for calculating the LCOM4 metric for a given Java class file using the Apache BCEL library.
  - `loadGroups(File file)`: Loads groups of fields and methods from the provided Java class file and calculates LCOM4.
    
    ```js
    List<Group> loadGroups(File file) throws IOException {
        try (InputStream in = new FileInputStream(file)) {
            ClassParser parser = new ClassParser(in, file.getName());
            JavaClass clazz = parser.parse();
            String className = clazz.getClassName();
            ConstantPoolGen cp = new ConstantPoolGen(clazz.getConstantPool());
            List<Group> groups = new ArrayList<Group>();
            for (Field field: clazz.getFields()) {
                groups.add(new Group().addFields(field.getName()));
            }
            for (Method method: clazz.getMethods()) {
                Group group = new Group().addMethods(method.getName());
                Code code = method.getCode();
                InstructionList instrs = new InstructionList(code.getCode());
                for (InstructionHandle ih: instrs) {
                    Instruction instr = ih.getInstruction();
                    if (instr instanceof FieldInstruction) {
                        FieldInstruction fld = (FieldInstruction)instr;
                        if (fld.getClassName(cp).equals(className)) {
                            group.addFields(fld.getFieldName(cp));
                        }
                    } else if (instr instanceof InvokeInstruction) {
                        InvokeInstruction inv = (InvokeInstruction)instr;
                        if (inv.getClassName(cp).equals(className)) {
                            group.addMethods(inv.getMethodName(cp));
                        }
                    }
                }
                if (group.fields() > 0 || group.methods() > 1) {
                    int i = groups.size();
                    while (i > 0) {
                        --i;
                        Group g = groups.get(i);
                        if (g.intersects(group)) {
                            group.merge(g);
                            groups.remove(i);
                        }
                    }
                    groups.add(group);
                }
            }
            return groups;
        }
    }
    ```
3. **Driver**
- This class serves as the main entry point for running LCOM4 metric calculations on a specific Java class file.
  
  ```js
  public static void main(String[] args) throws Exception {
        LCOM4Calculation calculation = new LCOM4Calculation();
        File file = new File("build/classes/java/main/ReportGenerator.class");
                List< Group > lst = calculation.loadGroups(file);
        lst.forEach(System.out::println);
        int lcom4 = calculation.loadGroups(file).size();
        System.out.printf("LCOM4 of class %s is %d\n", file.getName(), lcom4);
    }
  // result:
  // Group{fields=[tblReport, modelTblReport], methods=[setVisible, add, setSize, populateTable, setResizable, lambda$main$0, setTitle, setDefaultCloseOperation, setLayout, <init>, setLocationRelativeTo]}
  // LCOM4 of class ReportGenerator.class is 1
  ```

# Tool
## MetricTree
![image](https://github.com/HaThiPhuongLinh/Week03_Software-Architecture-and-Design/assets/109422010/7ab1c58d-2620-4ce3-b3e3-eb92fd5b7bc9)
![image](https://github.com/HaThiPhuongLinh/Week03_Software-Architecture-and-Design/assets/109422010/0a08a651-18e0-45d3-ad09-b99fd9ff5109)
![image](https://github.com/HaThiPhuongLinh/Week03_Software-Architecture-and-Design/assets/109422010/bbda22a7-ec9a-4c68-a8fd-f6e10f1ba275)


