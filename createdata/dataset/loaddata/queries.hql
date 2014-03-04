INSERT OVERWRITE DIRECTORY '/home/ec2-user/blinkdb/data/createdata/dataset/loaddata/sample_output' select count(*), one, two from temp group by one, two;
INSERT OVERWRITE DIRECTORY '/home/ec2-user/blinkdb/data/createdata/dataset/loaddata/sample_output' select count(*), one, two from temp group by one, two;
quit;
