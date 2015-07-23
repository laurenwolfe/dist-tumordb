//For testing, output count
x = 0
y = 0

//Filename will need to be looped here from another file containing filenames and perhaps tumor
//type (or could just rtrim the tumor type from filenames.)
//Example filename: stad.all.16jan15.TP.pwpv

boolean read(FaunusVertex v, String file_iter) {
    def details = file_iter.split('\\.')

    new File(file_iter).eachLine({ final String line ->

        tumor_type = details[0]
        version = details[2]

        println tumor_type

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
             genomic_distance1) = line.split('\t')

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
                    objectID1 = tumor_type + ':Gene:' + name1
                    break
                case "GNAB":
                    objectID1 = tumor_type + ':Gene:' + name1
                    break
                case "CNVR":
                    objectID1 = tumor_type + ':Gene:' + name1
                    break
                case "RPPA":
                    objectID1 = tumor_type + ':Protein:' + name1
                    break
                case "METH":
                    objectID1 = tumor_type + ':Methylation:' + name1
                    break
                case "MIRN":
                    objectID1 = tumor_type + ':miRNA:' + name1
                    break
                default:
                    objectID1 = tumor_type + ':' + featureType1 + ':' + name1
                    break
            }

            switch (featureType2) {
                case "GEXB":
                    objectID2 = tumor_type + ':Gene:' + name2
                    break
                case "GNAB":
                    objectID2 = tumor_type + ':Gene:' + name2
                    break
                case "CNVR":
                    objectID2 = tumor_type + ':Gene:' + name2
                    break
                case "RPPA":
                    objectID2 = tumor_type + ':Protein:' + name2
                    break
                case "METH":
                    objectID2 = tumor_type + ':Methylation:' + name2
                    break
                case "MIRN":
                    objectID2 = tumor_type + ':miRNA:' + name2
                    break
                default:
                    objectID2 = tumor_type + ':' + featureType1 + ':' + name2
                    break
            }

            //Does the vertex already exist? If not, create it in the db
            if (!g.getVertex(objectID1)) {
                v.setId(Long.valueOf(objectID1))
                v.setProperty("objectID", objectID1)
                v.setProperty("name", name1)
                v.setProperty("tumor_type", tumor_type)
                v.setProperty("version", version)

                //Some of these may be empty, so let's test for that.
                !chr1 ?: v.setProperty("chr", chr1)
                !start1 ?: v.setProperty("start", start1)
                !end1 ?: v.setProperty("end", end1)
                !strand1 ?: v.setProperty("strand", strand1)

                y++

            } else {
                v = g.getVertex(objectID1)
            }

            def edge = v.addEdge(Direction.OUT, 'linkedTo', Long.valueOf(objectID2))
            edge.setProperty("sample_size", sample_size1)
            edge.setProperty("min_log_p_uncorrected", min_log_p_uncorrected1)
            edge.setProperty("bonferroni", bonferroni1)
            edge.setProperty("excluded_sample_count_a", excluded_sample_count_a1)
            edge.setProperty("min_log_p_unused_a", min_log_p_unused_a1)
            edge.setProperty("excluded_sample_count_b", excluded_sample_count_b1)
            edge.setProperty("min_log_p_unused_b", min_log_p_unused_b1)
            edge.setProperty("genomic_distance", genomic_distance1)
            edge.setProperty("feature_types", featureType1 + ':' + featureType2)

            if (!g.getVertex(objectID2)) {
                v.setId(Long.valueOf(objectID2))
                v.setProperty("objectID", objectID2)
                v.setProperty("name", name2)
                v.setProperty("tumor_type", tumor_type)
                v.setProperty("version", version)

                //Some of these may be empty, so let's test for that.
                !chr2 ?: v.setProperty("chr", chr2)
                !start2 ?: v.setProperty("start", start2)
                !end2 ?: v.setProperty("end", end2)
                !strand2 ?: v.setProperty("strand", strand2)

                y++

            } else {
                v2 = bg.getVertex(objectID2)
            }

            x++
        }
    })

    println x + " edges generated"
    println y + " vertices generated"
}