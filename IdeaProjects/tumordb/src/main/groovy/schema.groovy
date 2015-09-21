//Tumor database schema builder
//Author Lauren Wolfe, for ISB

g = TitanFactory.open('conf/titan-cassandra.properties')
mgmt = g.getManagementSystem()

//This will be generated as "feature_type:geneId"
objectID = mgmt.makePropertyKey('objectID').dataType(String.class).make()
tumor_type = mgmt.makePropertyKey('tumor_type').dataType(String.class).make()
type = mgmt.makePropertyKey('type').dataType(String.class).make()
name = mgmt.makePropertyKey('name').dataType(String.class).make()
chr = mgmt.makePropertyKey('chr').dataType(String.class).make()
start = mgmt.makePropertyKey('start').dataType(Integer.class).make()
end = mgmt.makePropertyKey('end').dataType(Integer.class).make()
strand = mgmt.makePropertyKey('strand').dataType(Character.class).make()
tumor_type = mgmt.makePropertyKey('tumor_type').dataType(String.class).make()
version = mgmt.makePropertyKey('version').dataType(String.class).make()
feature_type = mgmt.makePropertyKey('feature_type').dataType(String.class).make()
annotation = mgmt.makePropertyKey('annotation').dataType(String.class).make()

//Labels
bioentity = mgmt.makeVertexLabel('bioentity').make();
pairwise = mgmt.makeEdgeLabel('pairwise').multiplicity(Multiplicity.MULTI).make()
datasetslice = mgmt.makeEdgeLabel('datasetslice').multiplicity(Multiplicity.MULTI).make()
proximal = mgmt.makeEdgeLabel('proximal').multiplicity(Multiplicity.MULTI).make()
codesfor = mgmt.makeEdgeLabel('codesfor').multiplicity(Multiplicity.MULTI).make()

/*
Edge properties -- inline comment corresponds to column #:
# 1 feature A
# 2 feature B (order is alphabetical, and has no effect on result)
# 3 Spearman correlation coefficient (range is [-1,+1], also can be "NA" if cannot be calculated or is not appropriate to the data)
# 4 number of samples used for pairwise test (non-NA overlap of feature A and feature B)
# 5 -log10(p-value)  (uncorrected)
# 6 log10(Bonferroni correction factor)
# 7 -log10(corrected p-value)   [ col #7 = min ( (col #5 - col #6), 0 ) ]
# 8 # of non-NA samples in feature A that were not used in pairwise test
# 9 -log(p-value) that the samples from A that were not used are "different" from those that were
#10 (same as col #8 but for feature B)
#11 (same as col #9 but for feature B)
#12 genomic distance between features A and B (if not on same chromosome or one or both do not have coordinates, then this value is set to 500000000)
*/
correlation = mgmt.makePropertyKey('correlation').dataType(Decimal.class).make() //3
sample_size = mgmt.makePropertyKey('sample_size').dataType(Decimal.class).make() //4
min_log_p_uncorrected = mgmt.makePropertyKey('min_log_p_uncorrected').dataType(Decimal.class).make() //5
bonferroni = mgmt.makePropertyKey('bonferroni').dataType(Decimal.class).make() //6
min_log_p_corrected = mgmt.makePropertyKey('min_log_p_corrected').dataType(Decimal.class).make() //7
excluded_sample_count_a = mgmt.makePropertyKey('excluded_sample_count_a').dataType(Decimal.class).make() //8
min_log_p_unused_a = mgmt.makePropertyKey('min_log_p_unused_a').dataType(Decimal.class).make() //9
excluded_sample_count_b = mgmt.makePropertyKey('excluded_sample_count_b').dataType(Decimal.class).make() //10
min_log_p_unused_b = mgmt.makePropertyKey('min_log_p_unused_b').dataType(Decimal.class).make() //11
genomic_distance = mgmt.makePropertyKey('genomic_distance').dataType(Decimal.class).make() //12
feature_types = mgmt.makePropertyKey('feature_types').dataType(String.class).make()

//Create index of ObjectId to speed map building
mgmt.buildIndex('byObjectID', Vertex.class).addKey(objectID).unique().buildCompositeIndex()
mgmt.buildIndex('byType', Vertex.class).addKey(type).buildMixedIndex("search")
mgmt.buildIndex

mgmt.commit()