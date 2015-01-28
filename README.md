# CountQueryReport
Extension report for the Tomcat JDBC Connection Pool
### What does it do ?
CountQueryReport counts the number of statements executed between two defined points. You could count the number of executed statements for a method, transaction or any other unit of work. 
### What do i need ?  
  - Java 1.5+
  - The Tomcat JDBC Connection Pool 
  
### How do i build it ?
Clone the repository and run 
```sh
./gradlew assemble
```
This creates the binary, source and javadoc jars in
```sh
build/lib/
```
### How do I use it ?
Add CountQueryReport.jar to your project.

Configure the connection-pool to use CountQueryReport
In a Spring environment:
```xml
<bean id="myDataSource" class="org.apache.tomcat.jdbc.pool.DataSource">
	...
	<!-- Log queries -->
	<property name="jdbcInterceptors" value="net.solidsyntax.jdbc.pool.interceptor.CountQueryReport" />
</bean>
```
Add code where you want to count statements:  
```java
CountQueryReport.resetCount();  //Reset the count for the current thread
int nrOfqueries = CountQueryReport.numberOfStatements();  //Get the number of statements executed on the current thread since the last reset
```
### More information
SolidSyntax blog: http://www.solidsyntax.be/thoughts/count-number-queries-transaction/
Official JDBC-pool site: http://tomcat.apache.org/tomcat-7.0-doc/jdbc-pool.html
