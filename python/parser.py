#!/usr/bin/python

import sys


frame=sys.argv[1]           # This referes to the previous frame number
dist=sys.argv[2]
base="/home/ec2-user/eecs584"


def main():
    
    # Working on CANDIDATES

    # Storage
    print("Storage for candidates")
    inputFileName = base + "/workloadGen/candidateSetOutput/frame_"+ frame + "/candidate_stats_storage.result"
    outputFileName = base + "/GLPK_Files/input/stratifiedSampleSize.txt"
    outputFile = open(outputFileName, "w")
    with open(inputFileName, "r") as f:
        content = f.readlines()
        for item in content:
            item = item.strip()
            if "GC" in item or "sec" in item or "seconds" in item or "OK" in item:
                continue
            elif(item.isdigit() == False):
                continue;
            else:
                outputFile.write(item + "\n")
    outputFile.close()

    # Dqcs
    print("Dqcs for candidates")
    inputFileName = base + "/workloadGen/candidateSetOutput/frame_"+ frame + "/candidate_stats_dqcs.result"
    outputFileName = base + "/GLPK_Files/input/dqcs_candidates.txt"
    outputFile = open(outputFileName, "w")
    with open(inputFileName, "r") as f:
        content = f.readlines()
        for item in content:
            item = item.strip()
            if "GC" in item or "sec" in item or "seconds" in item or "OK" in item:
                continue
            elif(item.isdigit() == False):
                continue;
            else:
                outputFile.write(item + "\n")
    outputFile.close()

    ############################################################################

    # Working on QUERIES

    # Dqcs 
    print("Dqcs for queries");
    inputFileName = base + "/workloadGen/querySetOutput/querySet"+dist+"/frame_"+frame+"/workload_stats_dqcs.result"
    outputFileName = base + "/GLPK_Files/input/dqcs_queries.txt"
    outputFile = open(outputFileName, "w")
    with open(inputFileName, "r") as f:
        content = f.readlines()
        for item in content:
            item = item.strip()
            if "GC" in item or "sec" in item or "seconds" in item or "OK" in item:
                continue
            elif(item.isdigit() == False):
                continue;
            else:
                outputFile.write(item + "\n")
    outputFile.close()

    # Sparsity
    print("Sparsity Delta  for queries");
    inputFileName = base + "/workloadGen/querySetOutput/querySet"+dist+"/frame_"+frame+"/workload_stats_sparsity.result"
    outputFileName = base + "/GLPK_Files/input/sparsityDelta.txt"
    outputFile = open(outputFileName, "w")
    with open(inputFileName, "r") as f:
        content = f.readlines()
        for item in content:
            item = item.strip()
            if "GC" in item or "sec" in item or "seconds" in item or "OK" in item:
                continue
            elif(item.isdigit() == False):
                continue;
            else:
                outputFile.write(item + "\n")
    outputFile.close()

if __name__ == "__main__":
    main()
