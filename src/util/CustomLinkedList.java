package util;

import tasks.Task;

import java.util.*;
import java.util.stream.Collectors;

public class CustomLinkedList<E extends Task> {

    private Node<E> head;

    private Node<E> tail;

    private int size = 0;

    private final LinkedHashMap<Integer, Node<E>> historyMap = new LinkedHashMap<>();

    public Node<E> getFirstNode() {
        final Node<E> curHead = head;
        if (curHead == null)
            throw new NoSuchElementException();
        return curHead;
    }

    public void add(E element) {
        final Node<E> oldTail = tail;
        final Node<E> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        if (historyMap.containsKey(element.getId())) {
            Node<E> toRemove = historyMap.get(element.getId());
            removeNode(toRemove);
        }
        historyMap.put(element.getId(), tail);
        size++;
    }

    public void removeFirst() {
        removeNode(getFirstNode());
    }

    public E getLast() {
        final Node<E> curTail = tail;
        if (curTail == null)
            throw new NoSuchElementException();
        return tail.data;
    }

    public int size() {
        return this.size;
    }

//    Метод начинается с проверки, задан ли узел для удаления. Если переданный узел nodeToDelete равен null, то метод завершает свою работу, так как нечего удалять.
//
//    В случае, если у удаляемого узла есть предыдущий узел, то меняем указатель next предыдущего узла на следующий узел.
//    Если удаляемый узел является первым в списке, то обновляем указатель head на следующий узел.
//
//    Если у удаляемого узла есть следующий узел, то меняем указатель prev следующего узла на предыдущий узел.

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
    }

    public void remove(Integer id) {
        removeNode(historyMap.get(id));
        historyMap.remove(id);
    }

    public Set<E> toSet() {
        Set<E> set = new LinkedHashSet<>();
        for (Node<E> node : historyMap.values()) {
            set.add(node.data);
        }
        return set;
    }

    public void clearHistory() {
        for (Node<E> node : historyMap.values()) {
            removeNode(node);
        }
        historyMap.clear();
    }
}