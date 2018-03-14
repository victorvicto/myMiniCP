#! /bin/bash

mvn -gs settings.xml clean install
zip -r minicp.zip src data pom.xml settings.xml

