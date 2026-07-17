package datastructure;

/**
 * MyPriorityQueue<T>
 * Hàng đợi ưu tiên tự cài đặt bằng Max Heap.
 *
 * Max Heap nghĩa là phần tử có độ ưu tiên cao nhất luôn nằm ở vị trí gốc,
 * tức là heap[0].
 *
 * T extends Comparable<T> nghĩa là kiểu T phải biết tự so sánh với T khác.
 * Ví dụ:
 * Booking implements Comparable<Booking>
 */
public class MyPriorityQueue<T extends Comparable<T>> {

    private Object[] heap;     // Mảng dùng để lưu heap
    private int size;          // Số lượng phần tử hiện có trong heap
    private int capacity;      // Sức chứa hiện tại của mảng

    /**
     * Constructor khởi tạo priority queue với sức chứa mặc định là 10.
     */
    public MyPriorityQueue() {
        capacity = 10;
        heap = new Object[capacity];
        size = 0;
    }

    /**
     * Constructor khởi tạo priority queue với sức chứa ban đầu do người dùng truyền vào.
     *
     * Nếu initialCapacity <= 0 thì dùng mặc định là 10.
     */
    public MyPriorityQueue(int initialCapacity) {
        if (initialCapacity <= 0) {
            capacity = 10;
        } else {
            capacity = initialCapacity;
        }

        heap = new Object[capacity];
        size = 0;
    }

    /**
     * Thêm phần tử mới vào priority queue.
     *
     * Cách hoạt động:
     * 1. Thêm phần tử mới vào cuối mảng heap.
     * 2. Gọi heapifyUp để đẩy phần tử lên đúng vị trí nếu nó có độ ưu tiên cao hơn node cha.
     *
     * heapifyUp nghĩa là chỉnh heap từ dưới lên sau khi thêm phần tử mới.
     */
    public void insert(T data) {
        if (data == null) {
            return;
        }

        ensureCapacity();

        heap[size] = data;
        heapifyUp(size);
        size++;
    }

    /**
     * Lấy và xóa phần tử có độ ưu tiên cao nhất.
     *
     * Trong Max Heap, phần tử ưu tiên cao nhất luôn nằm ở heap[0].
     *
     * Cách hoạt động:
     * 1. Lưu lại phần tử ở heap[0] để trả về.
     * 2. Đưa phần tử cuối cùng lên heap[0].
     * 3. Giảm size.
     * 4. Gọi heapifyDown để đẩy phần tử ở gốc xuống đúng vị trí.
     *
     * heapifyDown nghĩa là chỉnh heap từ trên xuống sau khi xóa phần tử gốc.
     *
     * Nếu heap rỗng thì trả về null.
     */
    public T poll() {
        if (isEmpty()) {
            return null;
        }

        T rootData = getElement(0);

        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;

        if (!isEmpty()) {
            heapifyDown(0);
        }

        return rootData;
    }

    /**
     * Xem phần tử có độ ưu tiên cao nhất nhưng không xóa.
     *
     * Trong Max Heap, phần tử ưu tiên cao nhất nằm ở heap[0].
     *
     * Nếu heap rỗng thì trả về null.
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }

        return getElement(0);
    }

    /**
     * Kiểm tra priority queue có rỗng không.
     *
     * Trả về true nếu không có phần tử nào.
     * Trả về false nếu có ít nhất một phần tử.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Trả về số lượng phần tử hiện có trong priority queue.
     */
    public int size() {
        return size;
    }

    /**
     * Xóa toàn bộ phần tử trong priority queue.
     *
     * Sau khi clear:
     * size = 0
     * các vị trí cũ trong heap được set về null.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }

        size = 0;
    }

    /**
     * Returns a snapshot from highest to lowest priority and restores this heap
     * before returning, so monitoring operations do not change queue state.
     */
    public MyLinkedList<T> snapshotInPriorityOrder() {
        MyLinkedList<T> items = new MyLinkedList<>();
        while (!isEmpty()) {
            items.addLast(poll());
        }
        for (int i = 0; i < items.size(); i++) {
            insert(items.get(i));
        }
        return items;
    }

    /**
     * Duyệt và in toàn bộ phần tử trong heap ra màn hình.
     *
     * Lưu ý:
     * Thứ tự in ra là thứ tự trong mảng heap,
     * không chắc là thứ tự từ ưu tiên cao đến thấp hoàn toàn.
     *
     * Nếu muốn lấy đúng thứ tự ưu tiên, phải dùng poll() nhiều lần.
     */
    public void display() {
        if (isEmpty()) {
            System.out.println("Priority queue is empty.");
            return;
        }

        for (int i = 0; i < size; i++) {
            System.out.println(heap[i]);
        }
    }

    /**
     * Đẩy phần tử tại vị trí index lên trên nếu nó có độ ưu tiên cao hơn node cha.
     *
     * Dùng sau khi insert phần tử mới vào cuối heap.
     *
     * Quy tắc Max Heap:
     * node cha phải có độ ưu tiên >= node con.
     */
    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = parentIndex(index);

            T currentData = getElement(index);
            T parentData = getElement(parent);

            /*
             * Nếu currentData có độ ưu tiên cao hơn parentData,
             * đổi chỗ current với parent.
             */
            if (currentData.compareTo(parentData) > 0) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    /**
     * Đẩy phần tử tại vị trí index xuống dưới nếu nó có độ ưu tiên thấp hơn node con.
     *
     * Dùng sau khi poll() xóa phần tử gốc và đưa phần tử cuối lên gốc.
     *
     * Quy tắc Max Heap:
     * node cha phải có độ ưu tiên >= node con.
     */
    private void heapifyDown(int index) {
        while (true) {
            int left = leftChildIndex(index);
            int right = rightChildIndex(index);
            int largest = index;

            /*
             * Nếu con trái tồn tại và có độ ưu tiên cao hơn largest,
             * cập nhật largest thành con trái.
             */
            if (left < size && getElement(left).compareTo(getElement(largest)) > 0) {
                largest = left;
            }

            /*
             * Nếu con phải tồn tại và có độ ưu tiên cao hơn largest,
             * cập nhật largest thành con phải.
             */
            if (right < size && getElement(right).compareTo(getElement(largest)) > 0) {
                largest = right;
            }

            /*
             * Nếu largest vẫn là index hiện tại,
             * nghĩa là node hiện tại đã đúng vị trí.
             */
            if (largest == index) {
                break;
            }

            swap(index, largest);
            index = largest;
        }
    }

    /**
     * Trả về vị trí node cha của node tại index.
     *
     * Công thức heap lưu bằng mảng:
     * parent = (index - 1) / 2
     */
    private int parentIndex(int index) {
        return (index - 1) / 2;
    }

    /**
     * Trả về vị trí node con trái của node tại index.
     *
     * Công thức heap lưu bằng mảng:
     * left child = index * 2 + 1
     */
    private int leftChildIndex(int index) {
        return index * 2 + 1;
    }

    /**
     * Trả về vị trí node con phải của node tại index.
     *
     * Công thức heap lưu bằng mảng:
     * right child = index * 2 + 2
     */
    private int rightChildIndex(int index) {
        return index * 2 + 2;
    }

    /**
     * Đổi chỗ hai phần tử trong heap.
     */
    private void swap(int index1, int index2) {
        Object temp = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = temp;
    }

    /**
     * Tăng sức chứa của mảng heap nếu heap đã đầy.
     *
     * Cách làm:
     * 1. Nếu size < capacity thì chưa cần tăng.
     * 2. Nếu size == capacity thì tạo mảng mới có kích thước gấp đôi.
     * 3. Copy dữ liệu từ mảng cũ sang mảng mới.
     */
    private void ensureCapacity() {
        if (size < capacity) {
            return;
        }

        int newCapacity = capacity * 2;
        Object[] newHeap = new Object[newCapacity];

        for (int i = 0; i < size; i++) {
            newHeap[i] = heap[i];
        }

        heap = newHeap;
        capacity = newCapacity;
    }

    /**
     * Lấy phần tử tại index và ép kiểu về T.
     *
     * Vì Java không cho tạo trực tiếp mảng generic T[],
     * nên ta dùng Object[] rồi ép kiểu khi lấy dữ liệu ra.
     *
     * Generic nghĩa là kiểu dữ liệu tổng quát.
     */
    @SuppressWarnings("unchecked")
    private T getElement(int index) {
        return (T) heap[index];
    }
}
