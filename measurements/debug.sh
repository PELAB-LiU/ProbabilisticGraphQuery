#!/bin/bash

RUNS_UPPER=50
WARM_UPPER=10
GC_TIME_S=5
INCREMENTS=4


PGQ_JAR=pgq.jar
LOGDIR=logs-test/

cd "$(dirname "$0")"

runconfig(){
	java -XX:InitialRAMPercentage=70.0 -XX:MaxRAMPercentage=95.0 -jar $PGQ_JAR \
		--case $1 \
		--vql $2\
		--size $3 \
		--prefix $1 \
		--warmups $4 \
		--seed $5 \
		--iterations $6 \
		--gctime $7 \
		--warmuplogto $8 \
		--logto $9 \
		 |& tee ${10}
}

#         Case VQL          Size warmups runs iterations gc_time warmup_log log        tee
#runconfig SH  smarthome.vql 100  none    0..1 4          1       System.out System.out /dev/null

#         Case VQL            Size warmups runs iterations gc_time warmup_log log        tee
#runconfig SAT  satellite.vql  45   none    0..1 4          1       System.out System.out /dev/null

#         Case VQL               Size  warmups runs iterations gc_time warmup_log log        tee
runconfig SRV  surveillance.vql  100   none    0..0 1          1       System.out System.out /dev/null

