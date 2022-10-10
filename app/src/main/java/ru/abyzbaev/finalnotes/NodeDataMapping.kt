package ru.abyzbaev.finalnotes


import java.util.HashMap

object NodeDataMapping {
    @JvmStatic
    fun toNode(
        id: String?,
        doc: Map<String?, Any?>
    ): Node {
        return Node(
            doc[Fields.ID] as String?,
            doc[Fields.TITLE] as String?,
            doc[Fields.DESCRIPTION] as String?
        )
    }

    @JvmStatic
    fun toDocument(node: Node): Map<String, Any?> {
        val answer: MutableMap<String, Any?> = HashMap()
        answer[Fields.ID] = node.id
        answer[Fields.TITLE] = node.title
        answer[Fields.DESCRIPTION] = node.description
        return answer
    }

    object Fields {
        const val ID = "ID"
        const val TITLE = "TITLE"
        const val DESCRIPTION = "DESCRIPTION"
    }
}