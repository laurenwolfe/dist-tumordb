//Filename will need to be looped here from another file containing filenames and perhaps tumor
//type (or could just rtrim the tumor type from filenames.)
//Example filename: stad.all.16jan15.TP.pwpv

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

//Pulls each line from the file specified in script-input.properties
def boolean read(FaunusVertex v, String file_iter) {
    def details = file_iter.split('\\.')
    def Random random = new Random()
    def Date date = new Date()
    def tumor_type = 'stad'
    def version = '14jan15'

    //Pull in line from the tsv
    def object1, object2, correlation1, sample_size1, min_log_p_uncorrected1, bonferroni1,
        min_log_p_corrected1, excluded_sample_count_a1, min_log_p_unused_a1, excluded_sample_count_b1,
        min_log_p_unused_b1, genomic_distance1

    line = file_iter.split('\t')

    object1 = line[0]
    object2 = line[1]

    if (line.size() > 2) {
        correlation1 = line[2]
    } else {
        correlation1 = 0.0
    }

    if (line.size() > 3) {
        sample_size1 = line[3]
    } else {
        sample_size1 = 0.0
    }

    if (line.size() > 4) {
        min_log_p_uncorrected1 = line[4]
    } else {
        min_log_p_uncorrected1 = 0.0
    }

    if (line.size() > 5) {
        bonferroni1 = line[5]
    } else {
        bonferroni1 = 0.0
    }

    if (line.size() > 6) {
        min_log_p_corrected1 = line[6]
    } else {
        min_log_p_corrected1 = 0.0
    }

    if (line.size() > 7) {
        excluded_sample_count_a1 = line[7]
    } else {
        excluded_sample_count_a1 = 0.0
    }

    if (line.size() > 8) {
        min_log_p_unused_a1 = line[8]
    } else {
        min_log_p_unused_a1 = 0.0
    }

    if (line.size() > 9) {
        excluded_sample_count_b1 = line[9]
    } else {
        excluded_sample_count_b1 = 0.0
    }

    if (line.size() > 10) {
        min_log_p_unused_b1 = line[10]
    } else {
        min_log_p_unused_b1 = 0.0
    }

    if (line.size() > 11) {
        genomic_distance1 = line[11]
    } else {
        genomic_distance1 = 0.0
    }

    objArr1 = object1.split(':')
    objArr2 = object2.split(':')

    //Split bioentity column into component data
    def featureType1, name1, chr1, start1, end1, strand1, annotation1
    def featureType2, name2, chr2, start2, end2, strand2, annotation2

    if (objArr1.size() > 1) {
        featureType1 = objArr1[1]
    } else {
        featureType1 = " "
    }

    if (objArr1.size() > 2) {
        name1 = objArr1[2]
    } else {
        name1 = " "
    }

    if (objArr1.size() > 3) {
        chr1 = objArr1[3]
    } else {
        chr1 = " "
    }

    if (objArr1.size() > 4) {
        start1 = objArr1[4]
    } else {
        start1 = 0
    }

    if (objArr1.size() > 5) {
        end1 = objArr1[5]
    } else {
        end1 = 0
    }

    if (objArr1.size() > 6) {
        strand1 = objArr1[6]
    } else {
        strand1 = '-'
    }

    if (objArr1.size() > 7) {
        annotation1 = objArr1[7]
    } else {
        annotation1 = " "
    }


    if (objArr2.size() > 1) {
        featureType2 = objArr2[1]
    } else {
        featureType2 = " "
    }

    if (objArr2.size() > 2) {
        name2 = objArr2[2]
    } else {
        name2 = " "
    }

    if (objArr2.size() > 3) {
        chr2 = objArr2[3]
    } else {
        chr2 = " "
    }

    if (objArr2.size() > 4) {
        start2 = objArr2[4]
    } else {
        start2 = 0
    }

    if (objArr2.size() > 5) {
        end2 = objArr2[5]
    } else {
        end2 = 0
    }

    if (objArr2.size() > 6) {
        strand2 = objArr2[6]
    } else {
        strand2 = '-'
    }

    if (objArr2.size() > 7) {
        annotation2 = objArr2[7]
    } else {
        annotation2 = " "
    }

    //This is for filtering by annotation type, currently both bioentities need to be code_potential_somatic for the
    //code block to execute.
    if (featureType1 != " " && featureType2 != " " &&
            featureType1 != "CLIN" && featureType2 != "CLIN" && featureType1 != "SAMP" &&
            featureType2 != "SAMP" && !(featureType1 == "GNAB" && featureType2 == "GNAB" &&
            (annotation1 == 'code_potential_somatic' || annotation2 == 'code_potential_somatic'))) {

        //Generate objectIDs by concatenating the tumor type, feature type and gene name
        objectID1 = setObjectID(tumor_type, featureType1, name1)
        objectID2 = setObjectID(tumor_type, featureType2, name2)

        id1Long = Long.parseLong(Long.toString(date.getTime()) + Integer.toString(new Random().nextInt(899) + 100))
        id2Long = Long.parseLong(Long.toString(date.getTime()) + Integer.toString(new Random().nextInt(899) + 100))
        long id1 = id1Long.longValue()
        long id2 = id2Long.longValue()

        //Does the vertex already exist? If not, create it in the db
        v.setId(id1)
        v.setProperty("objectID", objectID1)
        v.setProperty("name", name1)
        v.setProperty("tumor_type", tumor_type)
        v.setProperty("version", version)
        v.setProperty("feature_type", featureType1)

        //Some of these may be empty, so let's test for that.
        !chr1 ?: v.setProperty("chr", chr1)
        !start1 ?: v.setProperty("start", start1)
        !end1 ?: v.setProperty("end", end1)
        !strand1 ?: v.setProperty("strand", strand1)
        !annotation1 ?: v.setProperty("annotation", annotation1)

        v.setId(id2)
        v.setProperty("objectID", objectID2)
        v.setProperty("name", name2)
        v.setProperty("tumor_type", tumor_type)
        v.setProperty("version", version)
        v.setProperty("feature_type", featureType2)

        //Some of these may be empty, so let's test for that.
        !chr2 ?: v.setProperty("chr", chr2)
        !start2 ?: v.setProperty("start", start2)
        !end2 ?: v.setProperty("end", end2)
        !strand2 ?: v.setProperty("strand", strand2)
        !annotation2 ?: v.setProperty("annotation", annotation2)

        //graph.addEdge(null, outVertex, inVertex, label) :
        def edge = v.addEdge(Direction.OUT, 'pairwise', id1)
        !correlation1 ?: edge.setProperty("correlation", correlation1)
        !sample_size1 ?: edge.setProperty("sample_size", sample_size1)
        !min_log_p_uncorrected1 ?: edge.setProperty("min_log_p_uncorrected", min_log_p_uncorrected1)
        !bonferroni1 ?: edge.setProperty("bonferroni", bonferroni1)
        !min_log_p_corrected1 ?: edge.setProperty("min_log_p_corrected", min_log_p_corrected1)
        !excluded_sample_count_a1 ?: edge.setProperty("excluded_sample_count_a", excluded_sample_count_a1)
        !min_log_p_unused_a1 ?: edge.setProperty("min_log_p_unused_a", min_log_p_unused_a1)
        !excluded_sample_count_b1 ?: edge.setProperty("excluded_sample_count_b", excluded_sample_count_b1)
        !min_log_p_unused_b1 ?: edge.setProperty("min_log_p_unused_b", min_log_p_unused_b1)
        !genomic_distance1 ?: edge.setProperty("genomic_distance", genomic_distance1)
        (!featureType2 || !featureType1) ?: edge.setProperty("feature_types", featureType1 + ':' + featureType2)

        return true
    } else {
        return false
    }
}