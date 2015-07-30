//For testing, output count
id = 0

//Filename will need to be looped here from another file containing filenames and perhaps tumor
//type (or could just rtrim the tumor type from filenames.)
//Example filename: stad.all.16jan15.TP.pwpv

def boolean read(FaunusVertex v, String file_iter) {
//    def details = file_iter.split('\\.')

//    new File(file_iter).eachLine({ final String line ->
        tumor_type = 'stad'
        version = '14jan15'

        //Pull in line from the tsv
        def (object1,
                object2,
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
        def (dataType1,
                featureType1,
                name1,
                chr1,
                start1,
                end1,
                strand1,
                annotation1) = object1.split(':')

        //Split bioentity column into component data
        def (dataType2,
                featureType2,
                name2,
                chr2,
                start2,
                end2,
                strand2,
                annotation2) = object2.split(':')

        def objectID1
        def objectID2

        //This is for filtering by annotation type, currently both bioentities need to be code_potential_somatic for the
        //code block to execute.
        if (annotation1 == "code_potential_somatic" && annotation2 == "code_potential_somatic") {

            //Generate objectIDs by concatenating the tumor type, feature type and gene name
            switch (featureType1) {
                case "GEXB":
                    id1 = tumor_type + 'gene' + name1
                    objectID1 = tumor_type + ':Gene:' + name1
                    break
                case "GNAB":
                    id1 = tumor_type + 'gene' + name1
                    objectID1 = tumor_type + ':Gene:' + name1
                    break
                case "CNVR":
                    id1 = tumor_type + 'gene' + name1
                    objectID1 = tumor_type + ':Gene:' + name1
                    break
                case "RPPA":
                    id1 = tumor_type + 'protein' + name1
                    objectID1 = tumor_type + ':Protein:' + name1
                    break
                case "METH":
                    id1 = tumor_type + 'methylation' + name1
                    objectID1 = tumor_type + ':Methylation:' + name1
                    break
                case "MIRN":
                    id1 = tumor_type + 'mirna' + name1
                    objectID1 = tumor_type + ':miRNA:' + name1
                    break
                default:
                    id1 = tumor_type + featureType1 + name1
                    objectID1 = tumor_type + ':' + featureType1 + ':' + name1
                    break
            }

            switch (featureType2) {
                case "GEXB":
                    id2 = tumor_type + 'gene' + name2
                    objectID2 = tumor_type + ':Gene:' + name2
                    break
                case "GNAB":
                    id2 = tumor_type + 'gene' + name2
                    objectID2 = tumor_type + ':Gene:' + name2
                    break
                case "CNVR":
                    id2 = tumor_type + 'gene' + name2
                    objectID2 = tumor_type + ':Gene:' + name2
                    break
                case "RPPA":
                    id2 = tumor_type + 'protein' + name2
                    objectID2 = tumor_type + ':Protein:' + name2
                    break
                case "METH":
                    id2 = tumor_type + 'methylation' + name2
                    objectID2 = tumor_type + ':Methylation:' + name2
                    break
                case "MIRN":
                    id2 = tumor_type + 'mirna' + name2
                    objectID2 = tumor_type + ':miRNA:' + name2
                    break
                default:
                    id2 = tumor_type + featureType2 + name2
                    objectID2 = tumor_type + ':' + featureType1 + ':' + name2
                    break
            }

/*
        def id1 = Long.parseLong(id1, 36)
        id1 = id1.longValue()
        def id2 = Long.parseLong(id2, 36)
        id2 = id2.longValue()
*/
            def id1 = id
            def id2 = ++id

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

            def edge = v.addEdge(Direction.OUT, 'linkedTo', id1)
            edge.setProperty("sample_size", sample_size1)
            edge.setProperty("min_log_p_uncorrected", min_log_p_uncorrected1)
            edge.setProperty("bonferroni", bonferroni1)
            edge.setProperty("excluded_sample_count_a", excluded_sample_count_a1)
            edge.setProperty("min_log_p_unused_a", min_log_p_unused_a1)
            edge.setProperty("excluded_sample_count_b", excluded_sample_count_b1)
            edge.setProperty("min_log_p_unused_b", min_log_p_unused_b1)
            edge.setProperty("genomic_distance", genomic_distance1)
            edge.setProperty("feature_types", featureType1 + ':' + featureType2)

            id++
        }
    //})
    return true
}