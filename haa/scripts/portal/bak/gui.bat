@echo off
@title 서버로그
@color B
set CLASSPATH=.;Dist\*;Dist\Lib\*
java -Xms300m -Djavax.net.ssl.keyStore=filename.keystore -Djavax.net.ssl.keyStorePassword=passwd -Djavax.net.ssl.trustStore=filename.keystore -Djavax.net.ssl.trustStorePassword=passwd constants.programs.Login