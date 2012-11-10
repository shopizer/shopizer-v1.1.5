The following document describes the step for using MySQL, Oracle or create the schema on your own HSQLDB

Apache Ant (tested with v 1.6 and more)
JDK >= 1.5

Using MySQL
--------------

Create the database (assuming you want to keep SALESMANAGER as the database name)

1- Log in MySQL as root

mysql>CREATE DATABASE SALESMANAGER;
mysql>GRANT USAGE, SELECT ON *.* TO <YOUR USERNAME>@localhost IDENTIFIED BY '<YOUR PASSWORD>' with grant option;
mysql>grant all privileges on SALESMANAGER.* to <YOUR USERNAME>@localhost;
mysql>grant all privileges on SALESMANAGER.* to <YOUR USERNAME>@'%';
mysql>GRANT FILE ON *.* TO <YOUR USERNAME>@localhost;
mysql>GRANT FILE ON *.* TO <YOUR USERNAME>@'%';
mysql>FLUSH PRIVILEGES; 

2- Uncomment and change MySQL properties in build.properties

#MySQL
db_port=3306
db_username=<YOUR USERNAME>
db_password=<YOUR PASSWORD>
salesmanager_db_name=SALESMANAGER

3- Run shopizer-build-mysql.bat in the current directory

4- Change <SHOPIZER ROOT>/sm-core/conf/properties/systems.properties to use MySQL

5- Rebuild sm-central and sm-shop web applications


Using Oracle (Assuming Oracle XE - Lite or APEX)
--------------

Create the database (assuming you want to keep SALESMANAGER as the database name)

1- 



2- Uncomment and change MySQL properties in build.properties

#Oracle
db_port=1521
db_username=<YOUR USERNAME>
db_password=<YOUR PASSWORD>
salesmanager_db_name=XE

3- Run shopizer-build-oracle.bat in the current directory

4- Change <SHOPIZER ROOT>/sm-core/conf/properties/systems.properties to use Oracle

5- Rebuild sm-central and sm-shop web applications