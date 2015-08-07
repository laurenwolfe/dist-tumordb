
def TitanVertex getOrCreateVertex(vertex,  graph, context, log) {
    String uniqueKey = 'objectID'
    Object uniqueValue = vertex.getProperty(uniqueKey)
    Vertex titanVertex

    if (null == uniqueValue)
        throw new RuntimeException(vertex + " has no value for key " + uniqueKey)

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