#!/bin/bash

#cd /opt
#rm -rf DTool*
tar zxf /opt/DTools-5.3.1-alpha.01.tar.gz -C /opt
cd /opt/DTools-5.3.1-alpha.01/bin
chmod a+x /opt/DTools-5.3.1-alpha.01/bin/*.sh
sh /opt/DTools-5.3.1-alpha.01/bin/start.sh