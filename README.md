# Software Package Metrics Analyzer

This project utilizes JDepend and JDOM to analyze and generate reports on the project's package dependencies and structure. The process involves running JDepend to analyze the project, parsing the generated XML files using JDOM, and finally generating a report that includes statistics and actions for packages.

## Dependencies

- [JDepend](#): A tool for measuring and analyzing the package dependencies in Java projects.
  - ```js
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
