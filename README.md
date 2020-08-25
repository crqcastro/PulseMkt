# PulseMkt
[![build-status-image]][home] 
[![lang-java]][home] 
[![version]][home]
## Overview
Example of webservice rest that implements a shopping cart

## Resources Table
Verb | Resource | ADMIN
-----|----------|-------
POST | /user | N
POST | /login | N
POST | /cart | N
GET | /cart/{Integer:cartid} | N
GET | /products | N
PUT | /cart/{Integer:cartid}/product/{Integer:productid}/{Double:quantity} | N
DELETE | /cart/{Integer:cartid}/product/{Integer:productid}/{Double:quantity} | N
GET | /paymentmethod | N
PUT | /cart/{Integer:cartid}/payment/{Integer:ppaymentid}/{Double:amount} | N
DELETE | /cart/{Integer:cartid}/payment/{Integer:paymentid}/{Double:amount} | N
POST | /delivery | S
GET | /delivery/types | N
PUT | /cart/{Integer:cartid}/delivery/{Integer:deliveryid} | N
GET | /stores | N
POST | /store | S
POST | /cart/checkout | N
GET | /orders/{Integer:orderid} | N
GET | /oredrs | S





## Usage

### Logon
To create a new user
##### POST:/user
###### Request
* Content-Type: application/json
* Body: Json 
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

[![curl]][curl-url]

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
[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

MediaType mediaType = MediaType.parse("application/json");
RequestBody body = RequestBody.create(mediaType, "{\"name\":\"User Name\", \"email\":\"your@email.com\", \"password\":\"3ea9edc38460cf3616c480e6ae6d8c3c901b5c93\",\"number\":\"55555555555\", \"address\":{ \"address\":\"Pennsylvania Avenue NW\",\"number\":\"1600\",\n        \"complement\":\"S/N\",\"city\":\"Washington\",\"state\":\"DC\"}}");
Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/user/")
  .post(body)
  .addHeader("content-type", "application/json")
  .build();

Response response = client.newCall(request).execute();
```
[![php]][php-url]

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
### Login
To authenticate a user
##### POST:/login
###### Request
* Authorization: Basic Base64(your@email.com:password)
	* EX-> Basic amFuaWZibEBnbWFpbC5jb206M2VhOWVkYzM4NDYwY2YzNjE2YzQ4MGU2YWU2ZDhjM2M5MDFiNWM5Mw==

###### Response
* Authorization: Generated token
	* Every requeste will update this token
	* Valid: 10 minutes
* HTTP Status 200 - OK - When success

[![curl]][curl-url]

###### CURL
```curl
curl --request POST \
  --url http://localhost:8080/pulsemkt/login \
  --header 'authorization: Basic eW91ckBlbWFpbC5jb206MTIzNDU2'
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/login")
  .post(null)
  .addHeader("authorization", "Basic eW91ckBlbWFpbC5jb206MTIzNDU2")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/login');
$request->setRequestMethod('POST');
$request->setHeaders(array(
  'authorization' => 'Basic eW91ckBlbWFpbC5jb206MTIzNDU2'
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```

### Create Cart
To start a new cart
##### POST:/cart
###### Request
* Authorization: Last received valid token

###### Response
* AUthorization: Generated token
* Location: cart resource location
* HTTP Status 201 - CREATED - When success
* HTTP Status 401 - UNAUTHORIZED - When token expires

[![curl]][curl-url]

###### CURL
```curl
curl --request POST \
  --url http://localhost:8080/pulsemkt/cart/ \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNH0sInRpbWUiOnsiaG91ciI6MjIsIm1pbnV0ZSI6OSwic2Vjb25kIjo0LCJuYW5vIjo5NjAwMDAwMDB9fX0='
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/cart/")
  .post(null)
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNH0sInRpbWUiOnsiaG91ciI6MjIsIm1pbnV0ZSI6OSwic2Vjb25kIjo0LCJuYW5vIjo5NjAwMDAwMDB9fX0=")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/cart/');
$request->setRequestMethod('POST');
$request->setHeaders(array(
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNH0sInRpbWUiOnsiaG91ciI6MjIsIm1pbnV0ZSI6OSwic2Vjb25kIjo0LCJuYW5vIjo5NjAwMDAwMDB9fX0='
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```

### Product List
For the list of available products
##### GET:/products
* Query Parameters
	* Offset: Used in pagination to limit the start of the query scope
	* Limit: Used in pagination to limit the amount of results. Maximum 30 products per search
	* Barcode: Used to search for a product by barcode
	* Description: Used to search for a product that contains the value informed in the description

###### Request
* Authorization: Last received valid token


###### Response
* AUthorization: Generated token
* Location: cart resource location
* HTTP Status 200 - OK - When success
* HTTP Status 401 - UNAUTHORIZED - When token expires
* Body: Json
```
[
    {
        "id": Integer product id,
        "codBar": String Barcode,
        "description": String description,
        "value": Double product value,
        "image": URL to image resource
    }
]
```

[![curl]][curl-url]

###### CURL
```curl
curl --request GET \
  --url 'http://localhost:8080/pulsemkt/products/?offset=0&limit=20&barcode=7893321654&description=coke' \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ=='
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/products/?offset=0&limit=20&barcode=7893321654&description=coke")
  .get()
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ==")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/products/');
$request->setRequestMethod('GET');
$request->setQuery(new http\QueryString(array(
  'offset' => '0',
  'limit' => '20',
  'barcode' => '7893321654',
  'description' => 'coke'
)));

$request->setHeaders(array(
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ=='
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```

### Add product to cart
To add a product to the cart
##### PUT:/cart/{cartid}/product/{productid}/{quantity}
* Cartid: Id returned at cart creation
* Productid: Valid product id. The product list can be obtained at *List Products*
* Quantity: Quantity of a product to be added

###### Request
* Authorization: Last received valid token

###### Response
* AUthorization: Generated token
* HTTP Status 201 - ACCEPTED - When success
* HTTP Status 409 - CONFLICT - When the requisition user is not the same user who created the cart
* HTTP Status 401 - UNAUTHORIZED - When token expires

[![curl]][curl-url]

###### CURL
```curl
curl --request PUT \
  --url http://localhost:8080/pulsemkt/cart/1/product/1/5 \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/cart/1/product/1/5")
  .put(null)
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/cart/1/product/1/5');
$request->setRequestMethod('PUT');
$request->setHeaders(array(
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```

### To remove product to cart
To add a product to the cart
##### DELETE:/cart/{cartid}/product/{productid}/{quantity}
* Cartid: Id returned at cart creation
* Productid: Valid product id. The product list can be obtained at *List Products*
* Quantity: Quantity of a product to be removed

###### Request
* Authorization: Last received valid token

###### Response
* AUthorization: Generated token
* HTTP Status 201 - ACCEPTED - When success
* HTTP Status 409 - CONFLICT - When the requisition user is not the same user who created the cart
* HTTP Status 401 - UNAUTHORIZED - When token expires

[![curl]][curl-url]

###### CURL
```curl
curl --request DELETE \
  --url http://localhost:8080/pulsemkt/cart/1/product/1/3 \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/cart/1/product/1/3")
  .delete(null)
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/cart/1/product/1/3');
$request->setRequestMethod('DELETE');
$request->setHeaders(array(
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```

### List payment methods
For a list of available payment methods
##### GET:/paymentmethod
###### Request
* Authorization: Last received valid token

###### Response
* AUthorization: Generated token
* HTTP Status 201 - ACCEPTED - When success
* HTTP Status 409 - CONFLICT - When the requisition user is not the same user who created the cart
* HTTP Status 401 - UNAUTHORIZED - When token expires

[![curl]][curl-url]

###### CURL
```curl
curl --request DELETE \
  --url http://localhost:8080/pulsemkt/cart/1/product/1 \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/cart/1/product/1")
  .delete(null)
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/cart/1/product/1');
$request->setRequestMethod('DELETE');
$request->setHeaders(array(
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```

### Add payment
Add a payment method to the cart
##### POST:/cart/{cartid}/payment/{paymentid}/{value}
* Cartid: Id returned at cart creation
* paymentid: Id of a valid payment method. The  payment methods list can be obtained at *List payment methods*
* value: A double value to be paid with this payment method

###### Request
* Authorization: Last received valid token

###### Response
* AUthorization: Generated token
* HTTP Status 201 - ACCEPTED - When success
* HTTP Status 409 - CONFLICT - When the requisition user is not the same user who created the cart
* HTTP Status 401 - UNAUTHORIZED - When token expires

[![curl]][curl-url]

###### CURL
```curl
curl --request POST \
  --url http://localhost:8080/pulsemkt/cart/1/payment/1/99.99 \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/cart/1/payment/1/99.99")
  .post(null)
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/cart/1/payment/1/99.99');
$request->setRequestMethod('POST');
$request->setHeaders(array(
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```

### Delete payment
Delete a payment method to the cart
##### DELETE:/cart/{cartid}/payment/{paymentid}/{value}
* Cartid: Id returned at cart creation
* paymentid: Payment method id to be removed
* value: A double value to be removed with this payment method

###### Request
* Authorization: Last received valid token

###### Response
* AUthorization: Generated token
* HTTP Status 201 - ACCEPTED - When success
* HTTP Status 409 - CONFLICT - When the requisition user is not the same user who created the cart
* HTTP Status 401 - UNAUTHORIZED - When token expires

[![curl]][curl-url]

###### CURL
```curl
curl --request DELETE
  --url http://localhost:8080/pulsemkt/cart/1/payment/1/99.99 \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/cart/1/payment/1/99.99")
  .delete()
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/cart/1/payment/1/99.99');
$request->setRequestMethod('DELETE');
$request->setHeaders(array(
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```


### Create store
To create a new store
##### POST:/store
###### Request
* Authorization: Last received valid token
* body:
```
{
    "description": String:Stroe name,
    "address": {
        "address": String:Address,
        "number": Sring:number,
        "complement": String:Complement,
        "city": String: city,
        "state": Char[2]: State
    }
}
```

###### Response
* AUthorization: Generated token
* HTTP Status 201 - CREATED - When success
* HTTP Status 401 - UNAUTHORIZED - When token expires

[![curl]][curl-url]

###### curl
```curl
curl --request POST \
  --url http://localhost:8080/pulsemkt/store \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6MTAsIm1pbnV0ZSI6NDYsInNlY29uZCI6MzIsIm5hbm8iOjk4ODAwMDAwMH19fQ==' \
  --header 'content-type: application/json' \
  --data '{
    "description": "Materus Cohama",
    "address": {
        "address": "Av Daniel de la Touche",
        "number": "123",
        "complement": "Ao lado da BlackSwain",
        "city": "São Luis",
        "state": "MA"
    }
}'
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

MediaType mediaType = MediaType.parse("application/json");
RequestBody body = RequestBody.create(mediaType, "{\n    \"description\": \"Materus Cohama\",\n    \"address\": {\n        \"address\": \"Av Daniel de la Touche\",\n        \"number\": \"123\",\n        \"complement\": \"Ao lado da BlackSwain\",\n        \"city\": \"São Luis\",\n        \"state\": \"MA\"\n    }\n}");
Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/store")
  .post(body)
  .addHeader("content-type", "application/json")
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6MTAsIm1pbnV0ZSI6NDYsInNlY29uZCI6MzIsIm5hbm8iOjk4ODAwMDAwMH19fQ==")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$body = new http\Message\Body;
$body->append('{
    "description": "Materus Cohama",
    "address": {
        "address": "Av Daniel de la Touche",
        "number": "123",
        "complement": "Ao lado da BlackSwain",
        "city": "São Luis",
        "state": "MA"
    }
}');

$request->setRequestUrl('http://localhost:8080/pulsemkt/store');
$request->setRequestMethod('POST');
$request->setBody($body);

$request->setHeaders(array(
  'content-type' => 'application/json',
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6MTAsIm1pbnV0ZSI6NDYsInNlY29uZCI6MzIsIm5hbm8iOjk4ODAwMDAwMH19fQ=='
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```

### Delivery methods types
For types of delivery methods
##### GET:/delivery/types
###### Request
* Authorization: Last received valid token

###### Response
* AUthorization: Generated token
* HTTP Status 20o - OK - When success
* HTTP Status 401 - UNAUTHORIZED - When token expires

[![curl]][curl-url]

###### CURL
```curl
curl --request GET
  --url http://localhost:8080/pulsemkt/delivery/types \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/delivery/types")
  .delete()
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/delivery/types');
$request->setRequestMethod('GET');
$request->setHeaders(array(
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```

### Stores List
Get the list of available stores
##### GET:/stores
###### Request
* Authorization: Last received valid token

###### Response
* AUthorization: Generated token
* HTTP Status 200 - OK - When success
* HTTP Status 401 - UNAUTHORIZED - When token expires

[![curl]][curl-url]

###### CURL
```curl
curl --request GET
  --url http://localhost:8080/pulsemkt/stores \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/stores")
  .get()
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/stores');
$request->setRequestMethod('GET');
$request->setHeaders(array(
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```


### Add  or change delivery method
Add or change delivery method in cart
##### PUT:/cart/{cartid}/delivery/{deliveryid}
###### Request
* Authorization: Last received valid token

###### Response
* AUthorization: Generated token
* HTTP Status 201 - ACCEPTED - When success
* HTTP Status 409 - CONFLICT - When the requisition user is not the same user who created the cart
* HTTP Status 401 - UNAUTHORIZED - When token expires

[![curl]][curl-url]

###### CURL
```curl
curl --request PUT
  --url http://localhost:8080/pulsemkt/cart/1/delivery/1 \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/cart/1/delivery/1")
  .put()
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/cart/1/delivery/1');
$request->setRequestMethod('PUT');
$request->setHeaders(array(
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```

### Checkout
Checkout
##### :POST/{cartid}/checkout
###### Request
* Authorization: Last received valid token

###### Response
* AUthorization: Generated token
* Location: Order resource location
* HTTP Status 201 - ACCEPTED - When success
* HTTP Status 409 - CONFLICT - When the requisition user is not the same user who created the cart
* HTTP Status 401 - UNAUTHORIZED - When token expires

[![curl]][curl-url]

###### CURL
```curl
curl --request POST
  --url http://localhost:8080/pulsemkt/cart/1/checkout \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/cart/1/checkout")
  .post()
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/cart/1/checkout');
$request->setRequestMethod('POST');
$request->setHeaders(array(
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```

### Checkout
Checkout
##### :POST/{cartid}/checkout
###### Request
* Authorization: Last received valid token

###### Response
* AUthorization: Generated token
* Location: Order resource location
* HTTP Status 201 - ACCEPTED - When success
* HTTP Status 409 - CONFLICT - When the requisition user is not the same user who created the cart
* HTTP Status 401 - UNAUTHORIZED - When token expires

[![curl]][curl-url]

###### CURL
```curl
curl --request POST
  --url http://localhost:8080/pulsemkt/cart/1/checkout \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/cart/1/checkout")
  .post()
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/cart/1/checkout');
$request->setRequestMethod('POST');
$request->setHeaders(array(
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```

### Order List
Get the list of orders
* This feature is only available to administrators

##### GET:/orders
* Query Parameters
  * Offset: Used in pagination to limit the start of the query scope
  * Limit: Used in pagination to limit the amount of results. Maximum 30 products per search
  * Init: Min date
  * End: Max date
  * OrderID: To obtain a specific order

###### Request
* Authorization: Last received valid token

###### Response
* AUthorization: Generated token
* HTTP Status 200 - OK - When success
* HTTP Status 401 - UNAUTHORIZED - When token expires

[![curl]][curl-url]

###### CURL
```curl
curl --request GET
  --url http://localhost:8080/pulsemkt/orders?init=8-25-2020&end=8-25-2020&status=D&orderid=1&offset=0&limit=30 \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/orders?init=8-25-2020&end=8-25-2020&status=D&orderid=1&offset=0&limit=30")
  .get()
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/orders?init=8-25-2020&end=8-25-2020&status=D&orderid=1&offset=0&limit=30');
$request->setRequestMethod('GET');
$request->setHeaders(array(
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```

### Detail a order
Get a especific orders

##### GET:/orders/{id}
* Id: Order identification

###### Request
* Authorization: Last received valid token

###### Response
* AUthorization: Generated token
* HTTP Status 200 - OK - When success
* HTTP Status 401 - UNAUTHORIZED - When token expires

[![curl]][curl-url]

###### CURL
```curl
curl --request GET
  --url http://localhost:8080/pulsemkt/orders/1 \
  --header 'authorization: eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
```

[![java]][java-url]

###### JAVA
```java
OkHttpClient client = new OkHttpClient();

Request request = new Request.Builder()
  .url("http://localhost:8080/pulsemkt/orders/1")
  .get()
  .addHeader("authorization", "eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ")
  .build();

Response response = client.newCall(request).execute();
```

[![php]][php-url]

###### PHP
```php
<?php

$client = new http\Client;
$request = new http\Client\Request;

$request->setRequestUrl('http://localhost:8080/pulsemkt/orders/1');
$request->setRequestMethod('GET');
$request->setHeaders(array(
  'authorization' => 'eyJ1c2VyIjp7ImlkIjoxLCJuYW1lIjoiQWRtaW4iLCJlbWFpbCI6ImFkbWluQHB1bHNlbWt0LmNvbSIsInBhc3N3b3JkIjoiNGU3YWZlYmNmYmFlMDAwYjIyYzdjODVlNTU2MGY4OWEyYTAyODBiNCIsInJvbGUiOiJBRE1JTklTVFJBVE9SIn0sImV4cGlyYXRpb24iOnsiZGF0ZSI6eyJ5ZWFyIjoyMDIwLCJtb250aCI6OCwiZGF5IjoyNX0sInRpbWUiOnsiaG91ciI6NywibWludXRlIjoxNCwic2Vjb25kIjo4LCJuYW5vIjo5NTAwMDAwMH19fQ'
));

$client->enqueue($request)->send();
$response = $client->getResponse();

echo $response->getBody();
```


## Instalation Guide

[![github]][github-url] 
### Git
Fork the project to your own repository

* Fork it `https://github.com/crqcastro/PulseMkt/fork`

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
##### DER
[![der]][der-link]

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

## Contributing :

* Fork it `https://github.com/crqcastro/pulsemkt/fork`
* Create your feature branch `git checkout -b my-new-feature`
* Commit your changes `git commit -am 'Add some feature'`
* Push to the branch `git push origin my-new-feature`
* Create a new Pull Request

<hr/>

### Have fun! :grin:




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
[java]:https://raw.githubusercontent.com/crqcastro/svg/master/java.svg
[java-url]:https://www.java.com/
[php]:https://raw.githubusercontent.com/crqcastro/svg/master/php.svg
[php-url]:https://www.php.net/
[curl]:https://raw.githubusercontent.com/crqcastro/svg/master/curl.svg
[curl-url]:https://curl.haxx.se/
[der]:https://raw.githubusercontent.com/crqcastro/PulseMkt/master/database-script/DER.png
[der-link]:https://raw.githubusercontent.com/crqcastro/PulseMkt/master/database-script/DER.png