FROM scratch

EXPOSE 8474
ENTRYPOINT ["/toxiproxy"]
CMD ["-host=0.0.0.0"]

COPY toxiproxy-server-linux-amd64 /toxiproxy
COPY toxiproxy-cli-linux-amd64 /toxiproxy-cli

# This is basically the Dockerfile you get at https://github.com/Shopify/toxiproxy/blob/master/Dockerfile, 
# with a small modification