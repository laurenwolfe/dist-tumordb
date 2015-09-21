//make master vertices list
verticesIt = g.getVertices()
uniqueVertices = [:]

if(verticesIt.hasNext()) {
    vertex = verticesIt.next()

    if(vertex.feature_type == "GEXP" || vertex.feature_type == "GNAB" || vertex.feature_type == "CNVR") {

    } else if(vertex.feature_type == "RPPA") {

    } else if(vertex.feature_type == "METH") {

    }


    if(!uniqueVertices.containsKey(vertex.name)) {
        uniqueVertices.put(vertex.name, vertex)
    }
}