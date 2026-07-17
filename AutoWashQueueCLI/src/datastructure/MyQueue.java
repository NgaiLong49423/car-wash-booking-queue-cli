package datastructure;

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
}
