package app;

import datastructure.MyLinkedList;

/**
 *
 * @author Lenovo
 */
public class Main {
    public static void main(String[] args) {
        MyLinkedList<String> list = new MyLinkedList<>();

        list.addLast("A");
        list.addLast("B");
        list.addFirst("X");

        list.display();

        System.out.println("First: " + list.getFirst());
        System.out.println("Last: " + list.getLast());
        System.out.println("Size: " + list.size());
    }
}
