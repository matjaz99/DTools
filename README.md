# DTools
Developer tools (SSH, FTP, SNMP simulator, automatization of common tasks, network status, docker status...)

Current stable release: 5.3.0


## Features

- create a network of servers
- monitor network
- monitor services
- autodiscovery
- execute ssh commands
- save common ssh commands
- upload/download files using ftp/sftp
- snmp manager
- snmp agent
- snmp simulator
- docker status
- quick notes
- todo list
- find duplicate files
- external links, favorites



Commons-logging is required by commons-vfs


## Docker image

Build:

```
docker build -t dtools .
docker tag dtools matjaz99/dtools:5.4.1
docker push matjaz99/dtools:5.4.1
```

docker run -d --name dtools -p 8088:8080 matjaz99/dtools:5.4.1




## Version history

moved to functional specification


Whois implementation in Java:

https://github.com/ethauvin/Whois/blob/master/Whois.java

https://stackoverflow.com/questions/2195730/check-domain-availability-java





