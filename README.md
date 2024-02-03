# Overview

This project utilizes JDepend and JDOM to analyze and generate reports on the project's package dependencies and structure. The process involves running JDepend to analyze the project, parsing the generated XML files using JDOM, and finally generating a report that includes statistics and actions for packages.

## Dependencies

- [JDepend](#): A tool for measuring and analyzing the package dependencies in Java projects.
- [DOM](#): XML parsing library.


1. **JDepend:**
   - Gradle:

     ```js
     implementation ("guru.nidi:jdepend:2.9.5")
     ```

2. **Parse XML Documents:**
   - Use DOM to parse the generated XML files.

   ```js
   DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
   DocumentBuilder builder = builderFactory.newDocumentBuilder();
   Document document = builder.parse(xmlFilePath);
   ```
