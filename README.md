# PulseMkt
[![build-status-image]][home] 
[![lang-java]][home] 
[![version]][home]
## Overview
Example of webservice rest that implements a shopping cart

## Usage

### Logon
##### POST:/pulsemkt/user
###### Request
* Content-Type: application/json
* Body
```
{
    "name":"String name",
    "email":"String email",
    "password":"String SHA-1 Encoded password",
    "number":"String CPF",
    "address":{
        "address":"String Address information",
        "number":"String s/n",
        "complement":"String Complement",
        "city":"String city",
        "state":"String state"
    }
}
```

###### Response
* HTTP Status 201 - CREATED - When success
* HTTP Status 400 - BAD REQUEST - When data for a mandatory field is missing

###### CURL
```shell
curl --request POST \
  --url http://localhost:8080/pulsemkt/user/ \
  --header 'content-type: application/json' \
  --data '{
    "name":"User Name",
    "email":"your@email.com",
    "password":"3ea9edc38460cf3616c480e6ae6d8c3c901b5c93",
    "number":"55555555555",
    "address":{
        "address":"Pennsylvania Avenue NW",
        "number":"1600",
        "complement":"",
        "city":"Washington",
        "state":"DC"
    }
}'
```
###### Java
```java
OkHttpClient client = new OkHttpClient();

MediaType mediaType = MediaType.parse("application/json");
RequestBody body = RequestBody.create(mediaType, "{\n    \"name\":\"User Name\",\n    \"email\":\"your@email.com\",\n    \"password\":\"3ea9edc38460cf3616c480e6ae6d8c3c901b5c93\",\n    \"number\":\"55555555555\",\n    \"address\":{\n        \"address\":\"Pennsylvania Avenue NW\",\n        \"number\":\"1600\",\n        \"complement\":\"\",\n        \"city\":\"Washington\",\n        \"state\":\"DC\"\n    }\n}");
Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/user/")
  .post(body)
  .addHeader("content-type", "application/json")
  .build();

Response response = client.newCall(request).execute();
```
###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$body = new http\Message\Body;
$body->append('{
    "name":"User Name",
    "email":"your@email.com",
    "password":"3ea9edc38460cf3616c480e6ae6d8c3c901b5c93",
    "number":"55555555555",
    "address":{
        "address":"Pennsylvania Avenue NW",
        "number":"1600",
        "complement":"",
        "city":"Washington",
        "state":"DC"
    }
}');

$request->setRequestUrl('http://localhost:8080/pulsemkt/user/');
$request->setRequestMethod('POST');
$request->setBody($body);

$request->setHeaders(array(
  'content-type' => 'application/json'
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```

## Instalation Guide

[![github]][github-url] 
### Git
Fork the project to your own repository

* [Fork it `https://github.com/crqcastro/PulseMkt/fork`] [git-fork-help]

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
database-script/pulsemkt.sql

```
#### Step 2
Create user in MySQL database and specify in the configuration file DBConfig.properties located in the resources directory
```
db.user=pulsemkt
db.password=pulsemkt
```
### Step3
insert database connection string information, including host and port
```
db.url=jdbc:mysql://localhost:3306/pulsemkt?autoReconnect=true&useSSL=false&useTimezone=true&serverTimezone=UTC
```



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