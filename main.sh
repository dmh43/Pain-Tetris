#!/bin/bash
apt-get update
apt-get -y install wget default-jre default-jdk
wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u112-b15/jdk-8u112-linux-x64.tar.gz
tar zxvf jdk-8u112-linux-x64.tar.gz
PATH=$(pwd)/jdk1.8.0_112/bin/:$PATH
export PATH
wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
chmod a+x ./lein
./lein
./lein cljsbuild once
LEIN_NO_DEV=true ./lein ring server-headless 80
