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
    
    public void enqueueFront(T data){
        Node<T> newNode = new Node<>(data);
        
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            newNode.next = front;
            front = newNode;
        }
        size++;
    }
    
    public void dequeueNodeByID(String id){
        Node<T> current = front;
        Node<T> temp = null;
        
        while (current != null) {
            Booking b = (Booking) current.data;
            if (b.getBookingId().equals(id)) {
                if (current == front) {
                    front = front.next;
                    if (front == null) {
                        rear = null;
                    } else {
                        temp.next = current.next;
                        if (current == rear) {
                            rear = temp;
                        }
                    }

                    size--;
                    return;
                }
            }
            temp = current;
            current=current.next;
        }
    }
}
