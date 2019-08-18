@ECHO OFF
SET SERVER_CODE="S1"
SET APP_HOME="D:\seomse\server"

CD %APP_HOME%

SET JAVA_HOME="C:\Program Files\Java\jdk1.8.0_74\bin"
SET CLASSPATH="%APP_HOME%\classes;"
SET CONFIG_PATH="..\config\seomse_config.xml"

for %%f in (%APP_HOME%\lib\*.jar) do call set CLASSPATH="%%CLASSPATH%%%%f;"


%JAVA_HOME%\java.exe -Xms32m -Xmx1024m -cp %CLASSPATH% com.seomse.system.server.Server %SERVER_CODE% %CONFIG_PATH%