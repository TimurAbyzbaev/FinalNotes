package ru.abyzbaev.finalnotes;

import java.util.HashMap;
import java.util.Map;

public class NodeDataMapping {
    public static class Fields{
        public final static String ID = "ID";
        public final static String TITLE = "TITLE";
        public final static String DESCRIPTION = "DESCRIPTION";
    }
    public static Node toNode(String id, Map<String, Object> doc){
        Node answer = new Node((String) doc.get(Fields.ID),
                (String) doc.get(Fields.TITLE),
                (String) doc.get(Fields.DESCRIPTION));
        return answer;
    }

    public static Map<String, Object> toDocument(Node node){
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.ID, node.getId());
        answer.put(Fields.TITLE, node.getTitle());
        answer.put(Fields.DESCRIPTION, node.getDescription());
        return answer;
    }
}
