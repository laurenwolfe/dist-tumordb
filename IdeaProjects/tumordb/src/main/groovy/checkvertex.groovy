def TitanVertex getOrCreateVertex(FaunusVertex vertex, TitanGraph graph, TaskInputOutputContext context, Logger log) {

    String uniqueKey = 'objectID'
    Object uniqueValue = vertex.getProperty(uniqueKey)
    TitanVertex titanVertex

    Iterator<Vertex> it = graph.query().has(uniqueKey, uniqueValue).vertices().iterator()

    if(it.hasNext()) {
        titanVertex = it.next()
        if (it.hasNext())
            log.info("The key {} has duplicated value {}", uniqueKey, uniqueValue)
    } else {
        titanVertex = graph.addVertex(vertex.getId())
    }

    return titanVertex;
}