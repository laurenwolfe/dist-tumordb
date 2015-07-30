//Filename will need to be looped here from another file containing filenames and perhaps tumor
//type (or could just rtrim the tumor type from filenames.)
//Example filename: stad.all.16jan15.TP.pwpv

def boolean read(FaunusVertex v, String file_iter) {
//    def details = file_iter.split('\\.')

//    new File(file_iter).eachLine({ final String line ->
        tumor_type = 'stad'
        version = '14jan15'

        //Pull in line from the tsv
        def (   String object1,
                String object2,
                float correlation1,
                float sample_size1,
                float min_log_p_uncorrected1,
                float bonferroni1,
                float excluded_sample_count_a1,
                float min_log_p_unused_a1,
                float excluded_sample_count_b1,
                float min_log_p_unused_b1,
                float genomic_distance1) = file_iter.split('\t')

        //Split bioentity column into component data
        def (   String dataType1,
                String featureType1,
                String name1,
                String chr1,
                int start1,
                int end1,
                char strand1,
                String annotation1) = object1.split(':')

        //Split bioentity column into component data
        def (   String dataType2,
                String featureType2,
                String name2,
                String chr2,
                int start2,
                int end2,
                char strand2,
                String annotation2) = object2.split(':')

        def String[] idList1
        def String[] idList2
        def String objectID1
        def String objectID2

        //This is for filtering by annotation type, currently both bioentities need to be code_potential_somatic for the
        //code block to execute.
        if (annotation1 == "code_potential_somatic" && annotation2 == "code_potential_somatic") {

            //Generate objectIDs by concatenating the tumor type, feature type and gene name
            idList1[] = setObjectID(tumor_type, featureType1, name1)
            idList2[] = setObjectID(tumor_type, featureType2, name2)

            objectID1 = idList1[0]
            objectID2 = idList2[0]

            def Long longId1 = Long.parseLong(idList1[1], 36)
            def long id1 = longId1.longValue()
            def Long longId2 = Long.parseLong(idList2[2], 36)
            def long id2 = id2.longValue()

            //Does the vertex already exist? If not, create it in the db
            v.setId(id1)
            v.setProperty("objectID", objectID1)
            v.setProperty("name", name1)
            v.setProperty("tumor_type", tumor_type)
            v.setProperty("version", version)

            //Some of these may be empty, so let's test for that.
            !chr1 ?: v.setProperty("chr", chr1)
            !start1 ?: v.setProperty("start", start1)
            !end1 ?: v.setProperty("end", end1)
            !strand1 ?: v.setProperty("strand", strand1)

            v.setId(id2)
            v.setProperty("objectID", objectID2)
            v.setProperty("name", name2)
            v.setProperty("tumor_type", tumor_type)
            v.setProperty("version", version)

            //Some of these may be empty, so let's test for that.
            !chr2 ?: v.setProperty("chr", chr2)
            !start2 ?: v.setProperty("start", start2)
            !end2 ?: v.setProperty("end", end2)
            !strand2 ?: v.setProperty("strand", strand2)

            def edge = v.addEdge(Direction.OUT, 'linkedTo', id2)
            edge.setProperty("sample_size", sample_size1)
            edge.setProperty("min_log_p_uncorrected", min_log_p_uncorrected1)
            edge.setProperty("bonferroni", bonferroni1)
            edge.setProperty("excluded_sample_count_a", excluded_sample_count_a1)
            edge.setProperty("min_log_p_unused_a", min_log_p_unused_a1)
            edge.setProperty("excluded_sample_count_b", excluded_sample_count_b1)
            edge.setProperty("min_log_p_unused_b", min_log_p_unused_b1)
            edge.setProperty("genomic_distance", genomic_distance1)
            edge.setProperty("feature_types", featureType1 + ':' + featureType2)
        }
    //})
    return true
}


//Sets Object ID for each vertex
def String[] setObjectID(String tumor_type, String featureType, String name) {
    switch (featureType) {
        case "GEXB":
            objectID = tumor_type + ':Gene:' + name
            id = tumor_type + 'gene' + name
            break
        case "GNAB":
            objectID = tumor_type + ':Gene:' + name
            id = tumor_type + 'gene' + name
            break
        case "CNVR":
            objectID = tumor_type + ':Gene:' + name
            id = tumor_type + 'gene' + name
            break
        case "RPPA":
            objectID = tumor_type + ':Protein:' + name
            id = tumor_type + 'protein' + name
            break
        case "METH":
            objectID = tumor_type + ':Methylation:' + name
            id = tumor_type + 'methylation' + name
            break
        case "MIRN":
            objectID = tumor_type + ':miRNA:' + name
            id = tumor_type + 'mirna' + name
            break
        default:
            objectID = tumor_type + ':' + featureType + ':' + name
            id = tumor_type + featureType + name
            break
    }

    return [objectID, id]
}