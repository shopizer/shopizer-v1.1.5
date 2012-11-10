Readme File
Date: 20110814-1854$

This package contains Shopizer v. 1.1.5

Shopizer is an online sales management software providing an online catalogue, a shopping cart,
order fullfillment and online invoicing functionalities.
Follow these instructions to build from the source
Building Shopizer will result in 2 web applications packaged in war files. Another
seperate web application 'media' is included in that package for hosting media files. 

Requirements

1) Apache Ant (tested with v 1.6 and more)

available at: http://ant.apache.org/

2) Sun JDK >= 1.5 (or any other JDK equivalent)

This link is for JDK 5.0 (1.5)

http://www.oracle.com/technetwork/java/javase/downloads/index-jdk5-jsp-142662.html


1) Run HSQLDB Database
------------------------------------------------------------------------------------------------------

For testing your installation, you can start HSQL DB in memory database already pre-configured
in <SHOPIZER ROOT>/schema/other/hsqldb-memory. start the database using startdb.bat

You can also install the schema on your HSQLDB, MySQL or Oracle database by following the instructions
(readme.txt) in <SHOPIZER ROOT>/schema


2) Building media web application
------------------------------------------------------------------------------------------------------

cd <SHOPIZER ROOT>/media
Run ANT command
a war file (media.war) will be created in <SHOPIZER ROOT>/media/dist


3) Change <SHOPIZER ROOT>/sm-core/conf/properties/sm-core-config.properties
------------------------------------------------------------------------------------------------------

Check the 5 lines

core.domain.server=<YOUR HOST>
#example localhost:8080
#This is the media bin absolute path for storing branding images, product images and files (js, css, flash...)
#This is the path where you will be dropping your war files
#example c:/dev/apache-tomcat-6.0.20/webapps
core.bin.mediapath=<ABSOLUTE DIR TO WAR DIRECTORY>
#This is the absolute path where downloadable files will reside
core.download.path=<ABSOLUTE DIR TO WAR DIRECTORY>/media/download


4) Change <SHOPIZER ROOT>/sm-core/conf/properties/systems.properties
------------------------------------------------------------------------------------------------------

Check those properties

database.driver
database.jdbcUrl
database.user
database.password

should be adjusted to your database, those properties are configured for using HSQLDB pre-configured (See step 1)

an smtp server should also be available. If there is no possibility for an smtp server, it is possible
to use gmail server if you have a gmail account, if so uncomment #Gmail section and comment #Emails section.


5) Building sm-central
------------------------------------------------------------------------------------------------------

cd <SHOPIZER ROOT>/sm-central
Run ANT command
a war file (sm-central.war) will be created in <SHOPIZER ROOT>/sm-central/dist


6) Building sm-shop
------------------------------------------------------------------------------------------------------

cd <SHOPIZER ROOT>/sm-shop
Run ANT command
a war file (sm-shop.war) will be created in <SHOPIZER ROOT>/sm-shop/dist


7) Voila ! Drop the war files generated in dist (media, sm-central and sm-core) in your favorite application
server deployment directory
------------------------------------------------------------------------------------------------------


administration url -> http://<yourhost>:<yourport>/central
catalogue url -> http://<yourhost>:<yourport>/shop





See shopizer.pdf for all details on creating a new store


For best performance, add those JVM settings -> -Xms256m -Xmx256m -XX:MaxPermSize=128m


Thanks for using Shopizer !!!!

*** Let us know your appreciation at support@shopizer.com ***
