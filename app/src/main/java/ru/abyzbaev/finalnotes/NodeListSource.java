package ru.abyzbaev.finalnotes;

import java.util.ArrayList;

public interface NodeListSource {
    NodeListSource init(NodeListResponse nodeListResponse);
    ArrayList<Node> getNodeList();
    int size();
    void deleteNode(int position);
    void updateNode(Node node);
    void addNode(Node node);
}
