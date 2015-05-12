# Step #


## 1 software needed ##

  * Running With JRE 5.0 Or Later
    1. Download JRE,release version 5.0 or later, from http://java.sun.com/j2se
    1. Set an environment variable named JRE\_HOME to the pathname of the directory into which you installed the JRE, e.g. c:\jre5.0 or /usr/local/java/jre5.0.
  * Running With Tomcat 5.5 Or Later
    1. Download a binary distribution of Tomcat from: http://tomcat.apache.org
    1. Unpack the binary distribution into a convenient location so that the distribution resides in its own directory (conventionally named "apache-tomcat-[version](version.md)").  For the purposes of the remainder of this document, the symbolic name "$CATALINA\_HOME" is used to refer to the full pathname of the release directory.
  * Running with Mysql 5.1 Or Later
    1. Download Mysql release version ,from http://dev.mysql.com/downloads/mysql/
    1. Run mysql and Import SQL Script  [distributedmanagement.sql](http://netrender.googlecode.com/files/distributedmanagement.sql)

---

## 2 Deploy ##
  * Get package, deploy to tomcat
    1. Download war [WebRender.war](http://netrender.googlecode.com/files/WebRender.war)
    1. use [maven](http://maven.apache.org/) to compile  sources , get the newest package.
    1. copy WebRender.war to TOMCAT\_HOME/webapps/
    1. open http://localhost:8080/WebRender