#!/usr/bin/python

import sys

base=sys.argv[1]

def main():
    os.system("./bin/blinkdb -i CREATE TABLE students( id DOUBLE, fname STRING, lname STRING, gpa FLOAT, major STRING, contact_number STRING, gender STRING, city STRING, state STRING , country STRING, zipcode STRING, height FLOAT, weight FLOAT, number_siblings INT, favorite_number INT, number_cousins INT, number_uncles INT, number_aunts INT, number_children INT) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' STORED AS TEXTFILE; ")

   os.system("./bin/blinkdb -i LOAD DATA LOCAL INPATH '" + base + "/blinkdb/data/data' OVERWRITE INTO TABLE students; ")
        
    # Quitting from blink db
    print("Qutting blinkdb")
    os.system('./bin/blinkdb -e "quit;"')


if __name__ == "__main__":
    main()


