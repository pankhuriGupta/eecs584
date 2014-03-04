#!/usr/bin/python
import random
import sys

# Data structures
firstNames = []
lastNames = []
cities = []
states = []
countries = []
favoriteFoods = []
favoriteSports = []
favoriteNovels = []
majors = []

numberOfRandomNames = 40
numberOfRandomOthers = 10

fileNumber = sys.argv[1]
fileNumber = str(fileNumber)

def generateGPA():
	return random.uniform(1.0,4.0)

def generatePhoneNumber():
	number = ""
	for i in range(1,4):
		n = random.randint(1,9)
		number = number+ (str(n))
	number = number + '-'
	for i in range(1,4):
		n = random.randint(0,9)
		number = number + str(n)
	number = number + '-'
	for i in range(1,4):
		n = random.randint(0,9)
		number = number + str(n)

	return number

def generateZipcode():
	zipcode = ""
	for i in range(0,5):
		zipcode = zipcode + str(random.randint(0,9))
	return zipcode

def loaddata():
	rfile = open("dataFiles/firstNames.dat","r")
	for line in rfile:
		firstNames.append(line.strip())
	rfile.close()

	rfile = open("dataFiles/lastNames.dat","r")
	for line in rfile:
		lastNames.append(line.strip())
	rfile.close()

	rfile = open("dataFiles/cities.dat","r")
	for line in rfile:
		cities.append(line.strip())
	rfile.close()

	rfile = open("dataFiles/states.dat","r")
	for line in rfile:
		states.append(line.strip())
	rfile.close()

	rfile = open("dataFiles/countries.dat","r")
	for line in rfile:
		countries.append(line.strip())
	rfile.close()

	rfile = open("dataFiles/majors.dat","r")
	for line in rfile:
		majors.append(line.strip())
	rfile.close()

def getGender():
 	indexGender = random.randint(0,1)
	if(indexGender == 0):
		gender = "male"
	else:
		gender = "female" 

	return gender

def main():

	dataFile = open("loaddata/students" + fileNumber ,"w")	
	print "Start generating data"
	#readWebsite = open("dataFiles/websites/websites"+fileNumber+".txt","r")
	loaddata()

	for i in range(1,140000000):

		dataEntry = ""
		indexNames = random.randint(0,numberOfRandomNames-1)
		indexOthers = random.randint(0,numberOfRandomOthers-1)
		randNum = random.randint(0,24)

		studentId = i
		dataEntry = dataEntry + str(studentId) + "\t"

		fname = firstNames[indexNames]
		dataEntry = dataEntry + fname + "\t"

		lname = lastNames[indexNames]
		dataEntry = dataEntry + lname + "\t"

		gpa = generateGPA()
		dataEntry = dataEntry + str(gpa) + "\t"

		major = majors[indexOthers]
		dataEntry = dataEntry + major  + "\t"

		contactNo = generatePhoneNumber()
		dataEntry = dataEntry + contactNo + "\t"

		gender = getGender()
		dataEntry = dataEntry + gender + "\t"

		city = cities[randNum]
		dataEntry = dataEntry + city + "\t"

		state = states[randNum]
		dataEntry = dataEntry + state + "\t"

		country = countries[indexOthers]
		dataEntry = dataEntry + country + "\t"

		zipcode = generateZipcode()
		dataEntry = dataEntry + zipcode + "\t"

		height = random.uniform(4.0,6.5)
		dataEntry = dataEntry + str(height) + "\t"

		weight = random.uniform(50,200)
		dataEntry = dataEntry +str(weight)  + "\t"

		number_siblings = random.randint(0,5)
		dataEntry = dataEntry + str(number_siblings) + "\t"

		favorite_number = random.randint(0,100)
                dataEntry = dataEntry + str(favorite_number) + "\t"

                number_cousins = random.randint(0,20)
                dataEntry = dataEntry + str(number_cousins) + "\t"

                number_uncles = random.randint(0,10)
                dataEntry = dataEntry + str(number_uncles) + "\t"

                number_aunts = random.randint(0,10)
                dataEntry = dataEntry + str(number_aunts) + "\t"

                number_children = random.randint(0,10)
                dataEntry = dataEntry + str(number_children) + "\t"

		#website = readWebsite.next().strip()
		#dataEntry = dataEntry + website
		
		dataFile.write(dataEntry+ '\n')

	readWebsite.close()

if __name__ == "__main__":
	main()
