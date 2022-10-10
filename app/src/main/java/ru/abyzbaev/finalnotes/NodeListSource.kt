package ru.abyzbaev.finalnotes

import ru.abyzbaev.finalnotes.NodeListResponse
import ru.abyzbaev.finalnotes.NodeListSource
import java.util.ArrayList

interface NodeListSource {
    fun init(nodeListResponse: NodeListResponse?): NodeListSource?
    fun getNodeList(): ArrayList<Node>
    fun size(): Int
    fun deleteNode(position: Int)
    fun updateNode(node: Node?)
    fun addNode(node: Node?)
}