package util;

import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CustomLinkedList<E extends Task> {

    private Node<E> head;

    private Node<E> tail;

    private final LinkedHashMap<Integer, Node<E>> historyMap = new LinkedHashMap<>();

    public void linkLast(E task) {
        final Node<E> newNode = new Node<>(tail, task, null);
        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
    }

    public void add(E element) {
        removeNode(historyMap.get(element.getId()));
        linkLast(element);
        historyMap.put(element.getId(), tail);
    }

    private void removeNode(Node<E> nodeToDelete) {
        if (nodeToDelete == null) {
            return;
        }
        if (nodeToDelete.prev != null) {
            nodeToDelete.prev = nodeToDelete.next;
        } else {
            head = nodeToDelete.next;
        }
        if (nodeToDelete.next != null) {
            nodeToDelete.next = nodeToDelete.prev;
        }
        historyMap.remove(nodeToDelete.data.getId());
    }

    public void remove(Integer id) {
        removeNode(historyMap.get(id));
    }

    public ArrayList<E> toArrayList() {
        ArrayList<E> list = new ArrayList<>();
        for (Node<E> node : historyMap.values()) {
            list.add(node.data);
        }
        return list;
    }

    public void clearHistory() {
        for (Node<E> node : historyMap.values()) {
            removeNode(node);
        }
        historyMap.clear();
    }
}