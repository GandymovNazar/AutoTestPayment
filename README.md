To run different suites via cmd use these commands:
* **mvn install -Denv=stage -DsuiteXmlFile=smoke.xml**

Available parameters:
* env : stage, production, cd2
* suiteXmlFile: smoke.xml, reels.xml, limits.xml (default)

If you want to run just limits test, you can use command
**mvn install -Denv=stage**
In other cases use **suiteXmlFile** parameter.


Test report you'll find there **target/surefire-reports/index.html**