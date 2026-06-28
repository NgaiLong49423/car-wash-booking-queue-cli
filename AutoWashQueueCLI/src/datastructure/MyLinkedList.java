package datastructure;

/**
 * MyLinkedList<T>
 * Danh sách liên kết đơn tự cài đặt.
 *
 * T là kiểu dữ liệu tổng quát.
 * Ví dụ:
 * MyLinkedList<Customer>
 * MyLinkedList<Booking>
 * MyLinkedList<Vehicle>
 */
public class MyLinkedList<T> {

    private Node<T> head; // Node đầu tiên của danh sách
    private Node<T> tail; // Node cuối cùng của danh sách
    private int size;     // Số lượng phần tử hiện có trong danh sách

    /**
     * Constructor khởi tạo danh sách rỗng.
     */
    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Thêm phần tử vào cuối danh sách.
     *
     * Ví dụ:
     * A -> B
     * addLast(C)
     * Kết quả: A -> B -> C
     */
    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    /**
     * Thêm phần tử vào đầu danh sách.
     *
     * Ví dụ:
     * A -> B
     * addFirst(X)
     * Kết quả: X -> A -> B
     */
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }

        size++;
    }

    /**
     * Lấy phần tử tại vị trí index.
     *
     * index bắt đầu từ 0.
     * Ví dụ:
     * A -> B -> C
     * get(0) trả về A
     * get(1) trả về B
     * get(2) trả về C
     */
    public T get(int index) {
        checkIndex(index);

        Node<T> current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.data;
    }

    /**
     * Cập nhật phần tử tại vị trí index bằng data mới.
     *
     * Ví dụ:
     * A -> B -> C
     * set(1, X)
     * Kết quả: A -> X -> C
     */
    public void set(int index, T data) {
        checkIndex(index);

        Node<T> current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        current.data = data;
    }

    /**
     * Xóa phần tử tại vị trí index và trả về phần tử bị xóa.
     *
     * Ví dụ:
     * A -> B -> C
     * remove(1)
     * Trả về B
     * Danh sách còn: A -> C
     */
    public T remove(int index) {
        checkIndex(index);

        if (index == 0) {
            return removeFirst();
        }

        if (index == size - 1) {
            return removeLast();
        }

        Node<T> previous = head;

        for (int i = 0; i < index - 1; i++) {
            previous = previous.next;
        }

        Node<T> removedNode = previous.next;
        previous.next = removedNode.next;
        size--;

        return removedNode.data;
    }

    /**
     * Xóa phần tử đầu tiên và trả về phần tử đó.
     *
     * Ví dụ:
     * A -> B -> C
     * removeFirst()
     * Trả về A
     * Danh sách còn: B -> C
     *
     * Nếu danh sách rỗng thì trả về null.
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        T removedData = head.data;
        head = head.next;
        size--;

        if (isEmpty()) {
            tail = null;
        }

        return removedData;
    }

    /**
     * Xóa phần tử cuối cùng và trả về phần tử đó.
     *
     * Ví dụ:
     * A -> B -> C
     * removeLast()
     * Trả về C
     * Danh sách còn: A -> B
     *
     * Nếu danh sách rỗng thì trả về null.
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        if (size == 1) {
            T removedData = head.data;
            head = null;
            tail = null;
            size = 0;
            return removedData;
        }

        Node<T> current = head;

        while (current.next != tail) {
            current = current.next;
        }

        T removedData = tail.data;
        tail = current;
        tail.next = null;
        size--;

        return removedData;
    }

    /**
     * Xem phần tử đầu tiên nhưng không xóa khỏi danh sách.
     *
     * Ví dụ:
     * A -> B -> C
     * getFirst()
     * Trả về A
     * Danh sách vẫn là: A -> B -> C
     *
     * Nếu danh sách rỗng thì trả về null.
     */
    public T getFirst() {
        if (isEmpty()) {
            return null;
        }

        return head.data;
    }

    /**
     * Xem phần tử cuối cùng nhưng không xóa khỏi danh sách.
     *
     * Ví dụ:
     * A -> B -> C
     * getLast()
     * Trả về C
     * Danh sách vẫn là: A -> B -> C
     *
     * Nếu danh sách rỗng thì trả về null.
     */
    public T getLast() {
        if (isEmpty()) {
            return null;
        }

        return tail.data;
    }

    /**
     * Kiểm tra danh sách có rỗng không.
     *
     * Trả về true nếu danh sách không có phần tử nào.
     * Trả về false nếu danh sách có ít nhất một phần tử.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Trả về số lượng phần tử hiện có trong danh sách.
     */
    public int size() {
        return size;
    }

    /**
     * Xóa toàn bộ phần tử trong danh sách.
     *
     * Sau khi clear:
     * head = null
     * tail = null
     * size = 0
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Duyệt và in toàn bộ phần tử trong danh sách ra màn hình.
     *
     * Hàm này phù hợp với app CLI vì cần hiển thị dữ liệu cho người dùng.
     */
    public void display() {
        if (isEmpty()) {
            System.out.println("List is empty.");
            return;
        }

        Node<T> current = head;

        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }

    /**
     * Kiểm tra index có hợp lệ không.
     *
     * Index hợp lệ là từ 0 đến size - 1.
     * Nếu index không hợp lệ thì ném lỗi IndexOutOfBoundsException.
     *
     * Exception nghĩa là lỗi xảy ra khi chương trình đang chạy.
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Invalid index: " + index + ". Valid index range is 0 to " + (size - 1)
            );
        }
    }
}