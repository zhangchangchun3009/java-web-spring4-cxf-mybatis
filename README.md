# a light wrapped java web project.

# desc:
    jdk8 + spring context/web 5.2 + cxf3 + javax.inject + mysql8 + mybatis + shardingsphere.
    integrate some common functions, including
    user,role,privilege,login,modbus/tcp master/ slave,server/client socket,websocket,alipay,wechatpay,excel import/export,timertask
# develop:
    run class 'scm.web/src/main/java/pers/zcc/scm/web/lauch/AppBootstrap.java'
# deploy:    
    there are three package that could be deployed. recommended one is the 'scm.web.jar' which is packaged by spring-boot-maven-plugin.another choice is the 'scm.web.zip' file,unzip it and run the 'startup(/shutdown).sh(/.bat)' in the 'bin' folder.
    if you need a war file, uncommon war plugin in '/scm.web/pom.xml'.
# docker:
    ## build image: docker build --tag scm .
    ## run scm-mysql: docker run -d --network scm-app --network-alias mysql --name scm-mysql -v scm-mysql-data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=admin -e MYSQL_DATABASE=scm mysql:8.0.29
    ## run scm: docker run -d -p 80:80 --network scm-app --network-alias scm --name scm scm:latest    
