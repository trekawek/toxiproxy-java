# Testing

Testing of this library is done by using JUnit in combination with Docker. 
During the tests a docker container will be spun up and the unit tests connects to it in order to use the toxiproxy REST API.

For this the unit test makes use of the official toxiproxy docker hub image at ghcr.io/shopify/toxiproxy
