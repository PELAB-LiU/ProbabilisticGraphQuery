#!/bin/bash

PGQ_JAR=pgq.jar
LOGDIR=logs-scale/

cd "$(dirname "$0")"

#################
### SATELLITE ###
#################

CASE=SAT
VQL=satellite.vql

SIZE=20
STDOUT=std-sat-$SIZE.txt
java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..20 \
	--gctime 20 \
	--iterations 0 \
	--logto System.out $LOGDIR$()log-$CASE-$SIZE-run.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-$SIZE-warmup.txt | tee $LOGDIR$()$STDOUT

SIZE=40
STDOUT=std-sat-$SIZE.txt
java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..20 \
	--gctime 20 \
	--iterations 0 \
	--logto System.out $LOGDIR$()log-$CASE-$SIZE-run.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-$SIZE-warmup.txt | tee $LOGDIR$()$STDOUT

SIZE=60
STDOUT=std-sat-$SIZE.txt
java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..20 \
	--gctime 20 \
	--iterations 0 \
	--logto System.out $LOGDIR$()log-$CASE-$SIZE-run.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-$SIZE-warmup.txt | tee $LOGDIR$()$STDOUT

SIZE=80
STDOUT=std-sat-$SIZE.txt
java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -Xss128m -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..5 \
	--gctime 20 \
	--iterations 0 \
	--logto System.out $LOGDIR$()log-$CASE-$SIZE-run.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-$SIZE-warmup.txt |& tee $LOGDIR$()$STDOUT

####################
### SURVEILLANCE ###
####################

CASE=SRV
VQL=surveillance.vql

SIZE=10
STDOUT=std-srv-$SIZE.txt
java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..20 \
	--gctime 20 \
	--iterations 0 \
	--logto System.out $LOGDIR$()log-$CASE-$SIZE-run.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-$SIZE-warmup.txt | tee $LOGDIR$()$STDOUT
	
SIZE=100
STDOUT=std-srv-$SIZE.txt
java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..20 \
	--gctime 20 \
	--iterations 0 \
	--logto System.out $LOGDIR$()log-$CASE-$SIZE-run.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-$SIZE-warmup.txt | tee $LOGDIR$()$STDOUT

SIZE=1000
STDOUT=std-srv-$SIZE.txt
java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..20 \
	--gctime 20 \
	--iterations 0 \
	--logto System.out $LOGDIR$()log-$CASE-$SIZE-run.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-$SIZE-warmup.txt | tee $LOGDIR$()$STDOUT

SIZE=10000 
STDOUT=std-srv-$SIZE-b.txt
java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -Xss128m -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..5 \
	--gctime 20 \
	--iterations 0 \
	--logto System.out $LOGDIR$()log-$CASE-$SIZE-run-b.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-$SIZE-warmup-b.txt | tee $LOGDIR$()$STDOUT

#################
### SMARTHOME ###
#################

CASE=SH
VQL=smarthome.vql

SIZE=100
STDOUT=std-sh-$SIZE.txt
java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..20 \
	--gctime 20 \
	--iterations 0 \
	--logto System.out $LOGDIR$()log-$CASE-$SIZE-run.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-$SIZE-warmup.txt | tee $LOGDIR$()$STDOUT

SIZE=200
STDOUT=std-sh-$SIZE.txt
java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..20 \
	--gctime 20 \
	--iterations 0 \
	--logto System.out $LOGDIR$()log-$CASE-$SIZE-run.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-$SIZE-warmup.txt | tee $LOGDIR$()$STDOUT

SIZE=300
STDOUT=std-sh-$SIZE.txt
java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..20 \
	--gctime 20 \
	--iterations 0 \
	--logto System.out $LOGDIR$()log-$CASE-$SIZE-run.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-$SIZE-warmup.txt | tee $LOGDIR$()$STDOUT
	
SIZE=400
STDOUT=std-sh-$SIZE.txt
java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
	--case $CASE \
	--vql $VQL\
	--size $SIZE \
	--seed 0..50 \
	--prefix $CASE \
	--warmups 0..20 \
	--gctime 20 \
	--iterations 0 \
	--logto System.out $LOGDIR$()log-$CASE-$SIZE-run.txt \
	--warmuplogto System.out $LOGDIR$()log-$CASE-$SIZE-warmup.txt | tee $LOGDIR$()$STDOUT
