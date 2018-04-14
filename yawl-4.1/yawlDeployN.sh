

cp /home/noha/yawl-4.1-srcN2/yawl-4.1/output/{resourceService.war,yawl.war,workletService.war,yawlWSInvoker.war,monitorService.war,mailSender.war,documentStore.war} /home/noha/apache-tomcat-8.5.5/webapps

cp /home/noha/yawl-4.1-srcN2/yawl-4.1/classes/org/yawlfoundation/yawl/elements/data/external/MyExternalDBGatewayImpl.class /home/noha/apache-tomcat-8.5.5/webapps/yawl/WEB-INF/lib



cd /home/noha/apache-tomcat-8.5.5/webapps/

unzip resourceService.war -d /home/noha/apache-tomcat-8.5.5/webapps/resourceService
unzip monitorService.war -d /home/noha/apache-tomcat-8.5.5/webapps/monitorService
unzip workletService.war -d /home/noha/apache-tomcat-8.5.5/webapps/workletService
unzip documentStore.war -d /home/noha/apache-tomcat-8.5.5/webapps/documentStore
unzip yawlWSInvoker.war -d /home/noha/apache-tomcat-8.5.5/webapps/yawlWSInvoker
unzip mailSender.war -d /home/noha/apache-tomcat-8.5.5/webapps/mailSender
unzip yawl.war -d /home/noha/apache-tomcat-8.5.5/webapps/yawl


rm  /home/noha/apache-tomcat-8.5.5/webapps/*.war



