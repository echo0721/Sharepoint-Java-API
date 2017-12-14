![banner](http://www.quantr.hk/wp-content/uploads/2017/11/banner1.png)

# Sharepoint Java API
This library is calling SharePoint restful API https://msdn.microsoft.com/en-us/library/office/dn499819.aspx

## Why make this library
Calling SharePoint restful api using java is not that simple, first need to get the request token, secondly you need to get the rtfa and FedAuth keys. Now if you calling http-get methods, you need to stuck those keys into cookies. If you are calling http-post methods, you need to get the X-RequestDigest key from other request first, so it would be very very trouble and this library handled all these for you

## Who are we
We are quantr development team, we are a sharepoint dev company http://www.quantr.hk

## Example
https://github.com/quantr-research/Sharepoint-Java-API/blob/master/src/test/java/hk/quantr/sharepoint/TestSPOnline.java

## Compile

This library rely on peter-swing library https://github.com/mcheung63/peter-swing

1. git clone https://github.com/mcheung63/peter-swing.git
2. cd peter-swing
3. mvn clean install
4. cd ..
5. cd Sharepoint-Java-API
6. mvn clean package
7. The compiled jar file is in target folder, you can use it in your project now

## Becareful
		
Please encode the parameter yourself:

If there is space in the parameter, so dont just pass it as parameter like this

```
String jsonString = SPOnline.get(token, serverInfo.domain, serverInfo.path + "/_api/web/lists?$select=ID,Title&$filter=basetype eq 1&$orderby=title");
```

You have to encode it yourself, like this
		
```
String jsonString = SPOnline.get(token, serverInfo.domain, serverInfo.path + "/_api/web/lists?$select=ID,Title&$filter=" + URLEncoder.encode("basetype eq 1", "utf-8") + "&$orderby=title");
```

## Azure functions

This library support deploy to Azure function, run this command to deploy to azure. Then browse to https://quantr-sharepointonline.azurewebsites.net/api/getToken?username=peter@quantr.hk&password=xxx&domain=quantr

```
az login
mvn -P azure package azure-functions:package
mvn -P azure azure-functions:deploy
```
		
## Spring boot

This library support spring boot, you can pack the jar by the following command and run the jar by "ava -jar target/Sharepoint-Java-API-XX.jar" so the embedded server will listen to port 7654. Finally, you can browse to localhost:7654/getToken?domain=quantr&username=peter@quantr.hk&password=xxx

```
mvn -P spring clean package
```
		
!!! If you have this exception "java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty" during executing the jar, run this command "sudo update-ca-certificates -f"

## Author
My name is Peter, http://peter.quantr.hk , if you have troubles, please email me peter@quantr.hk

## Tutorial
All tutorials are in the wiki https://gitlab.com/quantr/sharepoint/Sharepoint-Java-API/wikis/home
