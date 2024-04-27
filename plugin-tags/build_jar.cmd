cd %~dp0


set JAVA_HOME=C:\Program Files\Java\jdk-8
set CLASSPATH=C:\TagNet\lib\junit-4.13.2.jar;C:\TagNet\lib\hamcrest-core-1.3.jar
set ANT_HOME=C:\TagNet\lib\apache-ant-1.10.14
set PATH=%ANT_HOME%\bin;%JAVA_HOME%\bin;%PATH%

call ant -f build-local.xml build

