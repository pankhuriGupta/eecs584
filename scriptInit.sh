#!/bin/bash

echo "Generating samples for time frame 0"
export base=$1
export totalFrames=$2
export totalDistances=$3


cp ${base}/eecs584/python/candidateQueriesRunner.py ${base}/blinkdb
cp ${base}/eecs584/python/workloadQueriesRunner.py ${base}/blinkdb
cp ${base}/eecs584/python/loaddata.py ${base}/blinkdb


# Initializing data structure
cd ${base}
mkdir workloadGen
mkdir workloadGen/querySet workloadGen/querySetOutput

# Generating directory structure for candidates
for dist in `seq 1 ${totalDistances}`; do
    mkdir ${base}/workloadGen/querySet/querySet${dist} ${base}/workloadGen/querySetOutput/querySetOutput${dist};
    for frame in `seq 1 ${totalFrames}` ; do
        mkdir ${base}/workloadGen/querySet/querySet${dist}/frame_${frame}  ${base}/workloadGen/querySetOutput/querySetOutput${dist}/frame_${frame} ;
    done;
done;

# Generating directory structure for candidates
mkdir workloadGen/candidateSet workloadGen/candidateSetOutput
for frame in `seq 1 ${totalFrames}` ; do
    mkdir ${base}/workloadGen/candidateSet/frame_${frame}  ${base}/workloadGen/candidateSetOutput/frame_${frame};
done;

cd ${base}/code
java -cp bin workload.SimpleWorkloadGenerator -samples 0

echo "Generating all the queries for all the time frames at different distances"
java -cp bin workload.SimpleWorkloadGenerator -queries

# CREATE candidtaes 
cd /home/ec2-user/blinkdb
python candidateQueriesRunner.py 0 "create" "no"

# Run the queries for different time frames to get the base readings (using all candidates to answer queries):
for frame in `seq 1 4`; do
    for dist in `seq 0 3` ; do
        ./bin/blinkdb -i ${base}/workloadGen/querySet/querySet${dist}/frame_${frame}/workload.queries > ${base}/workloadGen/querySetOutput/querySet${dist}/frame_${frame}/workload_all.result ;
    done;
done

