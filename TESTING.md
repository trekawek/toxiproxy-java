
# Testing

Testing of this library is done by using JUnit in combination with Docker. 
During the tests a docker container will be spun up and the unit tests connects to it in order to use the toxiproxy REST API.

For this the unit test makes use of the official toxiproxy docker hub image at https://hub.docker.com/r/shopify/toxiproxy/.
Unfortunately Shopify didn't update their image in the last years. This means that currently (October 2021) only version 2.1.4 can be used, 
but that specific version doesn't support the latest additions (like the reset_peer toxic for example). 

Fastest approach is to give this unit test what it needs: simply run the docker.sh script to give it an image containing the current version of toxiproxy.

Please note that this image will be named "shopify/toxiproxy:vx.y.z" to make this work but although a Dockerfile nearly identical to that provided by shopify is used this IS NOT the official image.

When toxiproxy gets updated to a version > 2.0.0 simply adjust the version number in the provided docker.sh.

	 
	 