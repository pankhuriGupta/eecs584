# Model of the BlinkDB optimization problem 

#################################### MODEL SECTION #############################################
# Contains declarations of the model objects 
# Common for all problems based on the corresponding model
################################################################################################


####-------------------- BASIC PARAMETERS ------------------####
param numQcs integer, >=0; 				  	# The total number of QCS we have with us out of which we have to choose
param numQueries integer, >= 0;				# Total number of queries in the system for which optimizing the problem

####------------------------ SETS ------------------------####

set Queries := {1..numQueries}; 
set Qcs := {1..numQcs}; 

# Derived Sets
#set y{j in 1..numQueries} := setof{j in 1..numQueries} (max{i in 1..numQcs} ( min(1,(DqcsI[i] / DqcsJ[j])) * z[i] ));
#forall{j in 1..numQueries}

####------------------------ PARAMETERS ------------------------####

# for calculaitng the objective function
param sparsityDelta{q in Queries} >=0;		# Describes the sparsity of the data
param probQ{q in Queries} >=0, <=1;			# Describes the probability of a QCS qj occurring
											# It is the frequency of queries with QCS qj in the past queries

# for calculating the first constraint
param storageCapacity >=0;					# The maximum storage capacity we have
param stratifiedSampleSize{i in Qcs} >=0;	# The size of the stratified sample associated with a QCS Phi
											# TODO: see if we need to use {i in Phi}

# for calculating the second constraint
param DqcsI{i in Qcs} >= 0;					# the size set of all unique x-values for a qcs Phi in Table T
param DqcsJ{j in Queries} >=0;				# the size set of all unique x-values for a qcs Qj in Table T
param domainQueries{j in Queries, i in Qcs}, binary,  >=0;		
											# This tells whether a qcs[i] is a subset or superset of qcs[qj] 
											# If it is, only then will domainQueries[j,i] will be 1, else 0

param fraction{j in Queries,i in Qcs} := min(1,(DqcsI[i] / DqcsJ[j])) * domainQueries[j,i] ;

# Extra Parameters
#param test{j in Queries} := max{i in Qcs} (fraction[j,i]);

####--------------------- VARIABLES ----------------------####
var z{i in Qcs}, binary;					# The variables for which we have to find the values
											# A binary variable representing if a QCS should be picked up or not
											# 0 : do not pick up (or build)
											# 1	: pick up (choose or build)

var y{j in Queries} >=0;

####----------------  DERIVED SETS ------------------####
#set y{j in Queries} := setof{ (max{i in Qcs} ( min(1,(DqcsI[i] / DqcsJ[j])) * z[i] )) : {j in 1..numQueries}};


####----------------  OBJECTIVE FUNCTION ------------------####
maximize obj: sum{q in Queries} probQ[q]*z[q]*sparsityDelta[q];
#maximize obj: sum{q in Queries} probQ[q]*y[q]*sparsityDelta[q];
#maximize obj: sum{q in Queries} probQ[q]*sparsityDelta[q]*(max{i in Qcs}fraction[q,i]*z[i]);


####--------------------  CONSTRAINTS ---------------------####

subject to storage: 0 <= sum{i in Qcs} stratifiedSampleSize[i]*z[i] <=storageCapacity;
#subject to coverage{j in Queries :  max{i in Qcs}fraction[j,i]*z[i] >= y[j]};
#subject to coverage:
#   y{j in Queries} <= max{i in Qcs}fraction[j,i]*z[i];

#################################### SOLVE SECTION #############################################
# Specifying solve statement (if no sove stmt is used, one is assumed at the end of the model section)
################################################################################################
solve;
display {j in Queries}fraction[j,1];
display z;
display y;
display obj;
#display test;
#display max{j in Queries,i in Qcs}fraction[j,i]*z[i];
#display {j in Queries}max{i in Qcs}fraction[j,i]*z[i];
#################################### DATA SECTION #############################################
# Optional part of the model desciption
# Contains data specific for a particular problem intance
# The data and the model section can be placed in separate files
################################################################################################


# End of the model file
end;
