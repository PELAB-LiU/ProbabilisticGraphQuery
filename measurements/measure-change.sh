#!/bin/bash

# This file it to measure relative time savings for the Smarthome domain.

###################
## Configuration ##
###################

RUNS_UPPER=10
WARM_UPPER=3
GC_TIME_S=5
VQL=smarthome.vql

PGQ_JAR=pgq.jar
LOGDIR=logs/

cd "$(dirname "$0")"

runconfig(){
	java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
		--case SH \
		--vql $VQL \
		--size 150 \
		--prefix SH \
		--warmups 0..$WARM_UPPER \
		--seed 0..$RUNS_UPPER \
		--iterations 1 \
		--gctime $GC_TIME_S \
		--batching $1
		--warmuplogto $2 \
		--logto $3 \
		 |& tee $LOGDIR$()std-SH-change-$1.txt
}

#################
### SMARTHOME ###
#################

#         batching  warmup_log                                         measure_log
runconfig 0         "System.out $LOGDIR$()log-SH-change-0-warmup.txt"  "System.out  $LOGDIR$()log-SH-change-0.txt"
runconfig 10        "System.out $LOGDIR$()log-SH-change-10-warmup.txt" "System.out  $LOGDIR$()log-SH-change-10.txt"
runconfig 20        "System.out $LOGDIR$()log-SH-change-20-warmup.txt" "System.out  $LOGDIR$()log-SH-change-20.txt"
runconfig 30        "System.out $LOGDIR$()log-SH-change-30-warmup.txt" "System.out  $LOGDIR$()log-SH-change-30.txt"
runconfig 40        "System.out $LOGDIR$()log-SH-change-40-warmup.txt" "System.out  $LOGDIR$()log-SH-change-40.txt"
runconfig 50        "System.out $LOGDIR$()log-SH-change-50-warmup.txt" "System.out  $LOGDIR$()log-SH-change-50.txt"
