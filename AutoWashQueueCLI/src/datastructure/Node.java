package datastructure;

/**
 * Node<T>
 * Một node dùng chung cho các cấu trúc dữ liệu dạng liên kết.
 *
 * T là kiểu dữ liệu tổng quát.
 * Ví dụ:
 * Node<Booking>
 * Node<Customer>
 */
public class Node<T> {
    T data;
    Node<T> next;

    /**
     * Tạo node mới chứa dữ liệu truyền vào.
     */
    public Node(T data) {
        this.data = data;
        this.next = null;
    }
}