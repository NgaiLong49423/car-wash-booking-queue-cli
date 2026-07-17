package datastructure;

import model.Booking;

public class MyQueue<T> {

    private Node<T> front;
    private Node<T> rear;
    private int size;

    public MyQueue() {
        front = null;
        rear = null;
        size = 0;
    }

    /**
     * Thêm phần tử vào cuối hàng đợi.
     */
    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);

        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }

        size++;
    }

    /**
     * Lấy và xóa phần tử đầu hàng đợi.
     */
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }

        T removedData = front.data;
        front = front.next;
        size--;

        if (isEmpty()) {
            rear = null;
        }

        return removedData;
    }

    /**
     * Xem phần tử đầu hàng đợi, không xóa.
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }

        return front.data;
    }

    /**
     * Kiểm tra queue có rỗng không.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Trả về số lượng phần tử trong queue.
     */
    public int size() {
        return size;
    }

    /**
     * Xóa toàn bộ queue.
     */
    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }

    /** Returns a read-only snapshot in FIFO order without changing this queue. */
    public MyLinkedList<T> snapshot() {
        MyLinkedList<T> items = new MyLinkedList<>();
        Node<T> current = front;
        while (current != null) {
            items.addLast(current.data);
            current = current.next;
        }
        return items;
    }

    /**
     * In toàn bộ queue.
     */
    public void display() {
        Node<T> current = front;

        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }

    /** Removes a booking by ID and returns whether a queue entry was removed. */
    public boolean removeBookingById(String id) {
        Node<T> current = front;
        Node<T> previous = null;

        while (current != null) {
            Booking b = (Booking) current.data;
            if (b.getBookingId().equalsIgnoreCase(id)) {
                if (current == front) {
                    front = front.next;
                } else {
                    previous.next = current.next;
                }
                if (current == rear) rear = previous;
                size--;
                if (size == 0) rear = null;
                return true;
            }
            previous = current;
            current=current.next;
        }
        return false;
    }

    /** Returns whether this queue contains a booking with the supplied ID. */
    public boolean containsBookingById(String id) {
        if (id == null) {
            return false;
        }

        Node<T> current = front;
        while (current != null) {
            Booking booking = (Booking) current.data;
            if (id.equalsIgnoreCase(booking.getBookingId())) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /** Legacy compatibility wrapper. */
    public void dequeueNodeByID(String id){
        removeBookingById(id);
    }
}
