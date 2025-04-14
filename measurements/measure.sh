#!/bin/bash

RUNS_UPPER=10
WARM_UPPER=5
GC_TIME_S=5
INCREMENTS=4


PGQ_JAR=pgq.jar
LOGDIR=logs/

cd "$(dirname "$0")"

#################
### SATELLITE ###
#################

CASE=SAT
SIZE=45
VQL=satellite.vql
STDOUT=std-sat-inc.txt

java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..$RUNS_UPPER \
	--prefix $CASE \
	--warmups 0..$WARM_UPPER \
	--gctime $GC_TIME_S \
	--iterations $INCREMENTS \
	--logto System.out $LOGDIR$()log-$CASE-run-inc.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-warmup-inc.txt |& tee $LOGDIR$()$STDOUT

####################
### SURVEILLANCE ###
####################

CASE=SRV
SIZE=200
VQL=surveillance.vql
STDOUT=std-srv-inc.txt

java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..$RUNS_UPPER \
	--prefix $CASE \
	--warmups 0..$WARM_UPPER \
	--gctime $GC_TIME_S \
	--iterations $INCREMENTS \
	--logto System.out $LOGDIR$()log-$CASE-run-inc.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-warmup-inc.txt |& tee $LOGDIR$()$STDOUT
	
#################
### SMARTHOME ###
#################

CASE=SH
SIZE=150
VQL=smarthome.vql
STDOUT=std-sh-inc.txt

java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..$RUNS_UPPER \
	--prefix $CASE \
	--warmups 0..$WARM_UPPER \
	--gctime $GC_TIME_S \
	--iterations $INCREMENTS \
	--logto System.out $LOGDIR$()log-$CASE-run-inc.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-warmup-inc.txt |& tee $LOGDIR$()$STDOUT
