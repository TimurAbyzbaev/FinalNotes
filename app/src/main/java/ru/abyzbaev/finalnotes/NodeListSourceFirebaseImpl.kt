package ru.abyzbaev.finalnotes

import android.util.Log
import ru.abyzbaev.finalnotes.NodeDataMapping.toNode

import ru.abyzbaev.finalnotes.NodeDataMapping.toDocument
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*


class NodeListSourceFirebaseImpl : NodeListSource {
    //База данных Firebase
    private val store = FirebaseFirestore.getInstance()

    //Коллекция документов
    private val collection = store.collection(NOTES_COLECTION)

    //Загружаемый список заметок
    private var nodeList = ArrayList<Node>()

    override fun init(nodeListResponse: NodeListResponse?): NodeListSource? {
        //получить всю коллекцию отсортированную по полю ID
        collection.orderBy(NodeDataMapping.Fields.ID, Query.Direction.DESCENDING).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //var nodeList = ArrayList<Node>()
                    for (document in task.result!!) {
                        val doc = document.data
                        val id = document.id
                        val node = toNode(id, doc)
                        nodeList.add(node)
                    }
                    Log.d(TAG, "success " + nodeList.size + " qnt")
                    nodeListResponse!!.initialized(this@NodeListSourceFirebaseImpl)
                } else {
                    Log.d(TAG, "get failed " + task.exception)
                }
            }.addOnFailureListener { e -> Log.d(TAG, "get failed with ", e) }
        return this
    }

    override fun getNodeList(): ArrayList<Node> {
        return nodeList
    }

    override fun size(): Int {
        return nodeList.size
    }

    override fun deleteNode(position: Int) {
        // Удалить документ с определённым идентификатором
        collection.document(nodeList[position].id!!).delete()
        nodeList.removeAt(position)
    }

    override fun updateNode(node: Node?) {
        val id = node!!.id
        // Изменить документ по идентификатору
        collection.document(id!!).set(toDocument(node))
    }

    override fun addNode(node: Node?) {
        collection.add(toDocument(node!!)).addOnSuccessListener { documentReference ->
            val id = documentReference.id
            node.id = id
            collection.document(id).set(toDocument(node))
        }
    }

    companion object {
        private const val NOTES_COLECTION = "notes"
        private const val TAG = "[NodeListSourceFirebaseImpl]"
        private const val ID = "ID"
    }
}