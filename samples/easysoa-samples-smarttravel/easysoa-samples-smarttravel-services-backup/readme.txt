
To build this mock : mvn assembly:assembly

To launch the mock :
- unzip target/easysoa-samples-smarttravel-services-backup-VERSION-bin.zip -d target
- cd target/easysoa-samples-smarttravel-services-backup-VERSION/bin
- java -jar easysoa-samples-smarttravel-services-backup-VERSION.jar http://localhost:9020/

-------

This mock is a modified copy of the meteo-sca-backup to avoid problems due to differences with WSDL signatures (One reason is that FraSCAti ignore JAXWS annotations).

In addition, two other mock are included :
- The currency mock : http://www.currencyserver.de/webservice/currencyserverwebservice.asmx?WSDL
- The translate mock : http://api.microsofttranslator.com/V1/Soap.svc?WSDL

Given the original WSDL from "http://www.webservicex.net/globalweather.asmx?WSDL", java classes were generated by CXF.
Same method was used for the currency mock.

For the translate mock, the work to do was more important. The original WSDL contains circular references to other WSDL. These references cause CXF fails to generate server skeleton classes.
So we have to modify the original WSDL to include directly in it the dependencies.

A Server class with a main method creates an instance of the web service implementation class and publish it to the given endpoint.
The main method need an argument to work : The endpoint to publish the mocked services.
If the endpoint is not specified, the default value is used : http://localhost:9020/

Services are published to these specifics endpoints (if base address is http://localhost:9020/):

- Meteo backup : http://localhost:9020/WeatherService
- Currency backup : http://localhost:9020/CurrencyServerWebService
- Translate backup : http://localhost:9020/SoapService

To get the WSDL's, just add '?wsdl' to the above addresses.

-----

Remark : In this case, it is not possible to run the mock with FraSCAti because FraSCAti doesn't works with the JAXWS annotations.
So when you have to work with CXF generated classes from a WSDL, you have at least one problem :
- The port name generated by FraSCAti don't match with the one from the wsdl because FraSCAti add the suffix 'Port'.
