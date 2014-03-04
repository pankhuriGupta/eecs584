#!/usr/bin/python

import sys
import os
import shutil
import errno

oldFrameNo=sys.argv[1]
dist=sys.argv[2]

base="/home/ec2-user/eecs584/workloadGen"

def main():
    workingPath = (base + "/querySet/querySet"+dist+"/frame_" + oldFrameNo)
    print("dqcs queries in blinkdb")
    print "path :  ./bin/blinkdb -i " + workingPath + "/workload_stats_dqcs.queries" + " > " + base + "/querySetOutput/querySet" +dist + "/frame_" + str(oldFrameNo) + "/workload_stats_dqcs.result"
    os.system("./bin/blinkdb -i " + workingPath + "/workload_stats_dqcs.queries" + " > " + base + "/querySetOutput/querySet" +dist + "/frame_" + str(oldFrameNo) + "/workload_stats_dqcs.result")

    print("Sparsity queries in blinkdb")
    os.system("./bin/blinkdb -i " + workingPath + "/workload_stats_sparsity.queries" + " > " +base + "/querySetOutput/querySet" +dist + "/frame_" + str(oldFrameNo) + "/workload_stats_sparsity.result")

    # Quitting from blink db
    print("Qutting blinkdb")
    os.system('./bin/blinkdb -e "quit;"')


if __name__ == "__main__":
    main()



