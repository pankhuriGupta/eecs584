#!/usr/bin/python

import sys
import os
import shutil
import errno

frameNo=sys.argv[1]
action=sys.argv[2]

base="/home/ec2-user/eecs584/workloadGen"

def main():
    if(action == "create"):
        shouldDrop=sys.argv[3]
        if(shouldDrop == "yes"):
            print "dropping candidates for previous frame window"
            oldFrame = int(frameNo) - 1
            workingPath = base+"/candidateSet/frame_"+ str(oldFrame)
            os.system("./bin/blinkdb -i " + workingPath + "/candidate_drop.queries")
            print "PREVIOUS FRAMES CANDIDATES DROPEED"
        workingPath = base+"/candidateSet/frame_"+frameNo
        print("creating candidates in blinkdb");
        os.system("./bin/blinkdb -i " + workingPath + "/candidate_create.queries")
        
    elif(action == "stats") :
        # Here, the frameNo being passed is the previous frame itself.
        ipWorkingPath = (base + "/candidateSet/frame_" + frameNo)
        opWorkingPath = (base + "/candidateSetOutput/frame_" + frameNo)

        print("storage queries in blinkdb")
        os.system("./bin/blinkdb -i " + ipWorkingPath + "/candidate_stats_storage.queries" + " > " + opWorkingPath + "/candidate_stats_storage.result")    
        print("dqcs queries in blinkdb")
        os.system("./bin/blinkdb -i " +ipWorkingPath + "/candidate_stats_dqcs.queries" + " > " + opWorkingPath + "/candidate_stats_dqcs.result")
        
    # Quitting from blink db
    print("Qutting blinkdb")
    os.system('./bin/blinkdb -e "quit;"')


if __name__ == "__main__":
    main()


