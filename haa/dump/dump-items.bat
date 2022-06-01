@echo off
@title Dump
set CLASSPATH=.;dist\*;dist\lib\*;lib\*
java -server -Dnet.sf.odinms.wzpath=wz tools.wztosql.DumpItems
pause