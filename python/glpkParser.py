#!/usr/bin/python

import sys
import re


def getDecisions():
    inputFile = "/home/ec2-user/eecs584/GLPK_Files/glpk_output"
    decisions = {}
    qcsNumber = 0;

    with open(inputFile, "r") as f:
        content = f.readlines()
        for item in content:
            item = item.strip()
            if "z[" in item:
                item = re.sub(" ","",item)
                decision = item.split('*')[1][0]
                decisions[qcsNumber] = decision
                qcsNumber = qcsNumber + 1;
    return decisions

def main():

    # Read the qcs from GLPK input.
    readQcs = "/home/ec2-user/eecs584/GLPK_Files/input/candidates.txt"
    allQcs = []

    with open(readQcs, "r") as f:
        content = f.readlines()
        for item in content:
            allQcs.append(item.strip())
    
    decisions = getDecisions()
    outputFileName = "/home/ec2-user/eecs584/GLPK_Files/selectedCandidates.out"
    outputFile = open(outputFileName, "w")
    
    for i in range(0,len(decisions)):
        if(decisions[i] == "1"):
            outputFile.write(allQcs[i] + "\n")

    outputFile.flush()
    outputFile.close()

if __name__ == "__main__":
    main()
