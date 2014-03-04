#!/usr/bin/python
import random
import sys

def main():
        print "Starting"
	dataEntry = ""
	dataFile = open("dataFiles/websites/websites10.txt","w")
	for i in range(1, 100):
	        readWebsite = open("dataFiles/websites/websites1.txt","r")
		for i in range(1,6000000):
	        	website = readWebsite.next().strip()
	        	dataEntry = dataEntry + website
			dataFile.write(dataEntry+ '\n')
		readWebsite.close()
if __name__ == "__main__":
        main()

