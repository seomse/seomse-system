@ECHO OFF
SET APP_HOME="D:\seomse\engine"

CD %APP_HOME%

SET JAVA_HOME="C:\Program Files\Java\jdk1.8.0_74\bin"
SET CLASSPATH="%APP_HOME%\classes;"

for %%f in (%APP_HOME%\dependencies\*.jar) do call set CLASSPATH="%%CLASSPATH%%%%f;"

%JAVA_HOME%\java.exe -Xms%3m -Xmx%4m -cp %CLASSPATH% com.seomse.system.engine.Engine %1 %2 %3