@echo off
@title test
set CLASSPATH=.;out\artifacts\Island\*;lib\*
java -Dnet.sf.odinms.wzpath=wz constants.test
pause