#!/bin/sh
tpversion=2.2.0
wget https://github.com/Shopify/toxiproxy/releases/download/v$tpversion/toxiproxy-server-linux-amd64
wget https://github.com/Shopify/toxiproxy/releases/download/v$tpversion/toxiproxy-cli-linux-amd64
chmod u+x,g+x,o+x toxiproxy*linux*
docker build -t shopify/toxiproxy:$tpversion . 
rm toxiproxy-server-linux-amd64*
rm toxiproxy-cli-linux-amd64*
echo .
echo Built image with binaries of version $tpversion

