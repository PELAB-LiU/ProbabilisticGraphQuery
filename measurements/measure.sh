#!/bin/bash

PGQ_JAR=pgq-sip.jar
LOGDIR=../logs-sip/

cd "$(dirname "$0")"

#################
### SATELLITE ###
#################

CASE=SAT
SIZE=45
VQL=satellite.vql
STDOUT=std-sat.txt

java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..20 \
	--gctime 4 \
	--iterations 4 \
	--logto System.out $LOGDIR$()log-$CASE-run.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-warmup.txt | tee $LOGDIR$()$STDOUT

####################
### SURVEILLANCE ###
####################

CASE=SRV
SIZE=200
VQL=surveillance.vql
STDOUT=std-srv.txt

java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..20 \
	--gctime 4 \
	--iterations 4 \
	--logto System.out $LOGDIR$()log-$CASE-run.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-warmup.txt | tee $LOGDIR$()$STDOUT
	
#################
### SMARTHOME ###
#################

CASE=SH
SIZE=150
VQL=smarthome.vql
STDOUT=std-sh.txt

java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..20 \
	--gctime 4 \
	--iterations 4 \
	--logto System.out $LOGDIR$()log-$CASE-run.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-warmup.txt | tee $LOGDIR$()$STDOUT
