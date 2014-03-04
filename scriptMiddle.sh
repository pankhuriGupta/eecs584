#1/bin/bash

echo "Generating samples for time frame 0"

export frame=$1
export oldFrame=$2
export dist=$3
export base=$4


# Copy the QCS files for candidates and queries
echo "Copying freq and qcs"
cp ${base}/workloadGen/querySet/querySet${dist}/frame_${oldFrame}/workload_qcs.txt ${base}/GLPK_Files/input/queries.txt
cp ${base}/workloadGen/candidateSet/frame_0/qcs ${base}/GLPK_Files/input/candidates.txt                     # The inputs from which candidates are to be selected will always be the initial set

cd ${base}"/python"
python queryProb.py ${oldFrame} ${dist}

# Run the python script to run queries in blink for collecting statistics of candidates from previous time frame
echo "collect stats for candidates from prev time frame"
cd ${base}/../blinkdb
python candidateQueriesRunner.py 0 "stats"
python workloadQueriesRunner.py ${oldFrame} ${dist}

# Parse the outputs so produced to convert into GLPK input
cd ${base}"/python"
python parser.py ${oldFrame} ${dist}

# Generate the model and dat files of the GLPK solver
cd ${base}"/modelGen"
java ModelGenerator

# Run the GLPK solver
cd ${base}
glpsol --model modelGen/blinkdb.mod --data modelGen/blinkdb.dat --output GLPK_Files/glpk_output

# Parse the output of the glpk solver
cd ${base}"/python"
python glpkParser.py

# Copy the output file from the GLPK output folder to the next candidate frame to be generated
cp ${base}"/GLPK_Files/selectedCandidates.out" ${base}"/workloadGen/candidateSet/frame_"${frame}"/qcs"

# Run the java code to generate the queries for this set of candidates
cd ${base}/code
java -cp bin workload.SimpleWorkloadGenerator -samples ${frame}

# Set up the database - create the samples in the database for the queries being issued in the current frame
cd ${base}/../blinkdb
python candidateQueriesRunner.py ${frame} "create" "yes"

# NOW RUN THE QUERIES and NOTE TIME
time ./bin/blinkdb -i ${base}/workloadGen/querySet/querySet${dist}/frame_${frame}/workload.queries >  ${base}/workloadGen/querySetOutput/querySet${dist}/frame_${frame}/workload.result

