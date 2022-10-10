package ru.abyzbaev.finalnotes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class NodeListSourceFirebaseImpl implements NodeListSource{
    private static final String NOTES_COLECTION = "notes";
    private static final String TAG = "[NodeListSourceFirebaseImpl]";
    private static final String ID = "ID";


    //База данных Firebase
    private FirebaseFirestore store = FirebaseFirestore.getInstance();

    //Коллекция документов
    private CollectionReference collection = store.collection(NOTES_COLECTION);

    //Загружаемый список заметок
    private ArrayList<Node> notes = new ArrayList<>();

    @Override
    public NodeListSource init(NodeListResponse nodeListResponse) {
        //получить всю коллекцию отсортированную по полю ID
        collection.orderBy(NodeDataMapping.Fields.ID, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            notes = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> doc = document.getData();
                                String id = document.getId();
                                Node node = NodeDataMapping.toNode(id, doc);
                                notes.add(node);
                            }
                            Log.d(TAG, "success " + notes.size() + " qnt");
                            nodeListResponse.initialized(NodeListSourceFirebaseImpl.this);
                        }
                        else {
                            Log.d(TAG, "get failed " + task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "get failed with ", e);
                    }
                });
        return this;
    }

    @Override
    public ArrayList<Node> getNodeList(){
        return notes;
    }

    @Override
    public int size() {
        if(notes == null){
            return 0;
        }
        else{
            return notes.size();
        }
    }

    @Override
    public void deleteNode(int position) {
        // Удалить документ с определённым идентификатором
        collection.document(notes.get(position).getId()).delete();
        notes.remove(position);

    }

    @Override
    public void updateNode(Node node) {
        String id = node.getId();
        // Изменить документ по идентификатору
        collection.document(id).set(NodeDataMapping.toDocument(node));
    }

    @Override
    public void addNode(Node node) {
        collection.add(NodeDataMapping.toDocument(node)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String id = documentReference.getId();
                node.setId(id);
                collection.document(id).set(NodeDataMapping.toDocument(node));

            }
        });
    }


}
