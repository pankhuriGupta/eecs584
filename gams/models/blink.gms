$eolcom #

Scalar numQueries	number of queries /3/;
Scalar numQcs		number of qcs we have 	/5/;
Scalar storageCapacity	total storage capacity with us /100/;

Sets
	Queries All the Queries /1*3/
	Qcs All the qcs we already have /1*5/;

Parameters
	sparsityDelta(Queries)  how sparse the data is
	/ 1 20
	  2 40
	  3 50 /

	probQ(Queries)  what is the probability of each query
	/ 1 0.8
	  2 0.05
	  3 0.15 /

	stratifiedSampleSize(Qcs)  what is the size of each sample
	/ 1 60
	  2 30
	  3 40
	  4 53
	  5 32 /

	DqcsQueries(Queries)  size of qcs of the queries
	/ 1 6
	  2 3
	  3 5 /

	DqcsQcs(Qcs)   size of qcs in all the already formed qcs
	/ 1 4
	  2 2
	  3 1 
	  4 2 
	  5 4 /;

	Table domainQueries(Queries,Qcs)  whether the qcs of the queries intersect with already present qcs
		1  2  3  4  5
	1	1  0  0  1  1
	2	0  0  0  1  1
	3 	0  1  1  1  0  ;

Parameter fraction(Queries, Qcs)  fraction of coverage;
	fraction(Queries, Qcs) = domainQueries(Queries,Qcs) * min(1, DqcsQcs(Qcs)/DqcsQueries(Queries));

Variables
	obj  this is the objective function which we are optimizing
	z(Qcs) binary variable that defines should we pick a sample or not
	y(Queries) This gives the coverage aspect
 	temp(Queries,Qcs) find the temp product	

	Binary Variable z;
	Positive Variable y(Queries);
	
Equations
	objective	define the objective function
	storage		defining constraints on the storage
	coverage 	coverage for each query should be less than max fraction across all qcs;

	objective ..	obj =e= sum((Queries),probQ(Queries)*y(Queries)*sparsityDelta(Queries));
	storage .. 	sum((Qcs), stratifiedSampleSize(Qcs)*z(Qcs)) =l= storageCapacity;
	coverage .. 	smax((Qcs), fraction(Queries,Qcs)*z(Qcs)) =g= y(Queries);

Model blinkdb /all/;

Solve blinkdb using MINLP maximizing obj ;

Display blinkdb.modelstat, blinkdb.solvestat, w.l ;
option fraction :5:1:1; display fraction;
#option limcol=1; display  z ;
