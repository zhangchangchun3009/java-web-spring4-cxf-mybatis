# a light wrapped java web project.

# desc:
* jdk8 + spring context/web 5.2 + cxf3 + javax.inject + mysql8 + mybatis + shardingsphere.   

* integrate some common functions, including
user,role,privilege,login,modbus/tcp master/ slave,server/client socket,websocket,alipay,wechatpay,excel import/export,timertask 

---
# develop:
run class `scm.web/src/main/java/pers/zcc/scm/web/lauch/AppBootstrap.java`

---
# deploy:    
there are three packages that could be deployed. recommended one is the `scm.web.jar` which is packaged by `spring-boot-maven-plugin`.

another choice is the `scm.web.zip` file,unzip it and run the `startup(/shutdown).sh(/.bat)` in the `bin` folder.

if you need a war file, uncommon war plugin in `/scm.web/pom.xml`.

---
# docker:
## build image: 
`docker build --tag scm .`

---
## local run:   
   **create network:** 
   `docker network create scm-app`  
   **run scm-mysql:** 
   `docker run -d --network scm-app --network-alias mysql --name scm-mysql -v scm-mysql-data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=admin -e MYSQL_DATABASE=scm -e MYSQL_USER=develop -e MYSQL_PASSWORD=admin mysql:8.0.29`   
   **login on scm-mysql && create user && run sql scripts:**
   `docker exec -it scm-mysql mysql -u root -p`   
   **run scm:** 
   `docker run -d -p 80:80 -p 443:443 --network scm-app --network-alias scm --name scm scm:latest`    
   
--- 
## docker swarm run:
   1. `docker network create -d overlay scm-app`
   2. `docker service create -d --name mysql --network scm-app --publish published=3306,target=3306 --publish published=33060,target=33060 -e MYSQL_ROOT_PASSWORD=admin -e MYSQL_DATABASE=scm -e MYSQL_USER=develop -e MYSQL_PASSWORD=admin mysql:8.0.29`
   3. `docker service create -d --name scm --network scm-app --replicas=2 scm:latest`
   
---