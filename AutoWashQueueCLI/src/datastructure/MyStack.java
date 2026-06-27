package datastructure;

/**
 * MyStack<T>
 * Ngăn xếp LIFO tự cài đặt bằng linked node.
 *
 * LIFO nghĩa là Last In, First Out:
 * phần tử nào vào sau thì được lấy ra trước.
 *
 * Stack này không dùng lại MyLinkedList.
 * Stack chỉ dùng chung Node<T> để tránh lặp code node.
 *
 * T là kiểu dữ liệu tổng quát.
 * Ví dụ:
 * MyStack<Booking>
 * MyStack<String>
 */
public class MyStack<T> {

    private Node<T> top; // Node trên cùng của stack
    private int size;   // Số lượng phần tử hiện có trong stack

    /**
     * Constructor khởi tạo stack rỗng.
     *
     * Khi stack rỗng:
     * top = null
     * size = 0
     */
    public MyStack() {
        top = null;
        size = 0;
    }

    /**
     * Thêm phần tử mới lên đỉnh stack.
     *
     * Đây là thao tác đưa dữ liệu mới vào stack.
     *
     * Ví dụ:
     * Stack hiện tại:
     * B
     * A
     *
     * push(C)
     *
     * Kết quả:
     * C
     * B
     * A
     *
     * Trong dự án của bạn:
     * dùng để lưu booking vừa hoàn tất vào lịch sử undo.
     */
    public void push(T data) {
        if (data == null) {
            return;
        }

        Node<T> newNode = new Node<>(data);

        newNode.next = top;
        top = newNode;

        size++;
    }

    /**
     * Lấy và xóa phần tử trên đỉnh stack.
     *
     * Đây là thao tác lấy phần tử được thêm gần nhất.
     *
     * Ví dụ:
     * Stack hiện tại:
     * C
     * B
     * A
     *
     * pop()
     *
     * Trả về: C
     *
     * Stack còn lại:
     * B
     * A
     *
     * Nếu stack rỗng thì trả về null.
     */
    public T pop() {
        if (isEmpty()) {
            return null;
        }

        T removedData = top.data;
        top = top.next;
        size--;

        return removedData;
    }

    /**
     * Xem phần tử trên đỉnh stack nhưng không xóa.
     *
     * Ví dụ:
     * Stack hiện tại:
     * C
     * B
     * A
     *
     * peek()
     *
     * Trả về: C
     *
     * Stack vẫn giữ nguyên:
     * C
     * B
     * A
     *
     * Hàm này dùng khi muốn xem booking gần nhất có thể undo
     * nhưng chưa muốn lấy nó ra khỏi stack.
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }

        return top.data;
    }

    /**
     * Kiểm tra stack có rỗng hay không.
     *
     * Trả về true nếu stack không có phần tử nào.
     * Trả về false nếu stack có ít nhất một phần tử.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Trả về số lượng phần tử hiện có trong stack.
     *
     * Ví dụ:
     * Stack:
     * C
     * B
     * A
     *
     * size() trả về 3.
     */
    public int size() {
        return size;
    }

    /**
     * Xóa toàn bộ phần tử trong stack.
     *
     * Sau khi clear:
     * top = null
     * size = 0
     *
     * Có thể dùng khi reset dữ liệu,
     * load lại chương trình,
     * hoặc xóa lịch sử undo.
     */
    public void clear() {
        top = null;
        size = 0;
    }

    /**
     * Duyệt và in toàn bộ phần tử trong stack ra màn hình.
     *
     * Thứ tự in là từ đỉnh stack xuống dưới.
     *
     * Ví dụ:
     * C
     * B
     * A
     *
     * Vì đây là app CLI nên hàm display() hữu ích
     * để hiển thị hoặc debug lịch sử booking đã hoàn tất.
     */
    public void display() {
        if (isEmpty()) {
            System.out.println("Stack is empty.");
            return;
        }

        Node<T> current = top;

        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }
}