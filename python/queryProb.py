#!/usr/bin/python

import sys

oldFrame=sys.argv[1]
dist=sys.argv[2]

def main():
    inputFileName = "/home/ec2-user/eecs584/workloadGen/querySet/querySet"+dist +"/frame_"+oldFrame+"/workload_frequencies.txt"
    outputFileName = "/home/ec2-user/eecs584/GLPK_Files/input/probQueries.txt"    
    outputFile = open(outputFileName, "w")
    freqs = []
    with open(inputFileName, "r") as f:
        content = f.readlines()
        for item in content:
            item = item.strip()
            freqs.append(int(item))
    
    total = sum(freqs)
    print total
    for i in freqs:
        outputFile.write(str(float(i)/float(total)) + "\n")

    outputFile.close()

if __name__ == "__main__":
    main()
