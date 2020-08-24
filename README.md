# PulseMkt
## Overview
Example of webservice rest that implements a shopping cart

[![build-status-image]][home] 
[![lang-java]][home] 
[![version]][home]


## Instalation

[![github]][github-url] 
### Git
Fork the project to your own repository

[!* Fork it `https://github.com/crqcastro/PulseMkt/fork`][git-fork-help]

[![maven]][maven-url] 
### Maven
Use maven to manage dependencies and build the project
* Maven Package Goal

```
$ mvn clean verify
```
```
[INFO] Scanning for projects...
[INFO]
[INFO] --------------------< br.com.cesarcastro:pulsemkt >---------------------
[INFO] Building pulsemkt 0.0.1-SNAPSHOT
[INFO] --------------------------------[ war ]---------------------------------
[INFO] --- maven-war-plugin:3.2.3:war (default-war) @ pulsemkt ---
[INFO] Packaging webapp
[INFO] Assembling webapp [pulsemkt] in [\projects\pulsemkt\target\pulsemkt]
[INFO] Processing war project
[INFO] Copying webapp resources [\projects\pulsemkt\WebContent]
[INFO] Webapp assembled in [412 msecs]
[INFO] Building war: \projects\pulsemkt\target\pulsemkt.war
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  4.364 s
[INFO] Finished at: 2020-08-24T19:11:56-03:00
[INFO] ------------------------------------------------------------------------
```
Inside the target folder will be pulsemkt.war.
Just deploy the file to the application server.

[![mysql]][mysql-url]
### Database Configuration
Follow the steps to make the necessary settings

#### Step 1
Run the database application script 
```

```
#### Step 2
Create user in MySQL database and specify in the configuration file DBConfig.properties located in the resources directory
```
db.user=pulsemkt
db.password=pulsemkt
```
## Resources





[build-status-image]:https://raw.githubusercontent.com/crqcastro/svg/master/buildpassing.svg
[lang-java]:https://raw.githubusercontent.com/crqcastro/svg/master/langjava.svg
[version]:https://raw.githubusercontent.com/crqcastro/svg/master/version.svg
[home]:https://cesarcastro.com.br
[git-fork-help]:https://docs.github.com/en/github/getting-started-with-github/fork-a-repo
[mysql]:https://raw.githubusercontent.com/crqcastro/svg/master/mysql.svg
[mysql-url]:https://www.mysql.com/
[github]:https://raw.githubusercontent.com/crqcastro/svg/master/github.svg
[github-url]:https://github.com
[maven]:https://raw.githubusercontent.com/crqcastro/svg/master/apachemaven.svg
[maven-url]:https://maven.apache.org/