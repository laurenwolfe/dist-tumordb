import java.awt.TexturePaintContext

//Filename will need to be looped here from another file containing filenames and perhaps tumor
//type (or could just rtrim the tumor type from filenames.)
//Example filename: stad.all.16jan15.TP.pwpv

def boolean read(FaunusVertex v, String file_iter) {
//    def details = file_iter.split('\\.')
      def Random random = new Random()
      def Date date = new Date()

//    new File(file_iter).eachLine({ final String line ->
        tumor_type = 'stad'
        version = '14jan15'

        //Pull in line from the tsv
        def (   String object1,
                String object2,
                correlation1,
                sample_size1,
                min_log_p_uncorrected1,
                bonferroni1,
                excluded_sample_count_a1,
                min_log_p_unused_a1,
                excluded_sample_count_b1,
                min_log_p_unused_b1,
                genomic_distance1) = file_iter.split('\t')

        //Split bioentity column into component data
        def (   String dataType1,
                String featureType1,
                String name1,
                String chr1,
                start1,
                end1,
                strand1,
                annotation1) = object1.split(':')

        //Split bioentity column into component data
        def (   String dataType2,
                String featureType2,
                String name2,
                String chr2,
                start2,
                end2,
                strand2,
                annotation2) = object2.split(':')

        def String idNoColon1
        def String idNoColon2
        def String objectID1
        def String objectID2

        //This is for filtering by annotation type, currently both bioentities need to be code_potential_somatic for the
        //code block to execute.
        if (annotation1 == "code_potential_somatic" && annotation2 == "code_potential_somatic") {

            //Generate objectIDs by concatenating the tumor type, feature type and gene name
            objectID1 = setObjectID(tumor_type, featureType1, name1)
            objectID2 = setObjectID(tumor_type, featureType2, name2)

//            id1 = Long.parseLong(Long.toString(date.getTime())) + Long.parseLong(Integer.toString(new Random().nextInt(899) + 100))
//            id2 = Long.parseLong(Long.toString(date.getTime())) + Long.parseLong(Integer.toString(new Random().nextInt(899) + 100))

//          id2 = date.getTime() + new Random().nextInt(899) + 100

            id1 = 8000000
            id2 = 1

            println "id1: " + id1 + " id2: " + id2

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

            id1++
            id2++
        }
    //})
    return true
}


//Sets Object ID for each vertex
def String setObjectID(String tumor_type, String featureType, String name) {
    switch (featureType) {
        case "GEXB":
            objectID = tumor_type + ':Gene:' + name
            break
        case "GNAB":
            objectID = tumor_type + ':Gene:' + name
            break
        case "CNVR":
            objectID = tumor_type + ':Gene:' + name
            break
        case "RPPA":
            objectID = tumor_type + ':Protein:' + name
            break
        case "METH":
            objectID = tumor_type + ':Methylation:' + name
            break
        case "MIRN":
            objectID = tumor_type + ':miRNA:' + name
            break
        default:
            objectID = tumor_type + ':' + featureType + ':' + name
            break
    }

    return objectID
}

def String makeID(String tumor_type, String featureType, String name) {
    switch (featureType) {
        case "GEXB":
            id = tumor_type + 'gene' + name
            break
        case "GNAB":
            id = tumor_type + 'gene' + name
            break
        case "CNVR":
            id = tumor_type + 'gene' + name
            break
        case "RPPA":
            id = tumor_type + 'protein' + name
            break
        case "METH":
            id = tumor_type + 'methylation' + name
            break
        case "MIRN":
            id = tumor_type + 'mirna' + name
            break
        default:
            id = tumor_type + featureType + name
            break
    }

    return id
}

