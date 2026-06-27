package datastructure;

/**
 * MyMap<K, V>
 * Cấu trúc dữ liệu lưu dữ liệu theo dạng key-value.
 *
 * Key-value nghĩa là:
 * key   -> value
 * khóa  -> giá trị
 *
 * Ví dụ trong dự án:
 * "PLATINUM" -> 7
 * "GOLD"     -> 5
 * "SILVER"   -> 3
 * "MEMBER"   -> 1
 *
 * K là kiểu dữ liệu của key.
 * V là kiểu dữ liệu của value.
 *
 * Ví dụ:
 * MyMap<String, Integer>
 * MyMap<String, Booking>
 * MyMap<String, Customer>
 */
public class MyMap<K, V> {

    /**
     * Entry<K, V>
     * Một Entry là một cặp key-value trong map.
     *
     * Ví dụ:
     * key = "GOLD"
     * value = 5
     *
     * Để Entry là private class vì Entry chỉ phục vụ nội bộ MyMap.
     */
    private class Entry {
        K key;
        V value;

        /**
         * Constructor tạo một cặp key-value mới.
         */
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Entry[] entries; // Mảng lưu các cặp key-value
    private int size;        // Số lượng cặp key-value hiện có
    private int capacity;    // Sức chứa hiện tại của mảng

    /**
     * Constructor khởi tạo map với sức chứa mặc định là 10.
     */
    public MyMap() {
        capacity = 10;
        entries = new MyMap.Entry[capacity];
        size = 0;
    }

    /**
     * Constructor khởi tạo map với sức chứa ban đầu do người dùng truyền vào.
     *
     * Nếu initialCapacity <= 0 thì dùng mặc định là 10.
     */
    public MyMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            capacity = 10;
        } else {
            capacity = initialCapacity;
        }

        entries = new MyMap.Entry[capacity];
        size = 0;
    }

    /**
     * Thêm hoặc cập nhật một cặp key-value.
     *
     * Nếu key chưa tồn tại:
     * thêm Entry mới vào map.
     *
     * Nếu key đã tồn tại:
     * cập nhật value mới cho key đó.
     *
     * Ví dụ:
     * put("GOLD", 5)
     * put("GOLD", 6)
     *
     * Kết quả:
     * "GOLD" -> 6
     */
    public void put(K key, V value) {
        int index = findIndexByKey(key);

        /*
         * Nếu key đã tồn tại thì cập nhật value.
         */
        if (index != -1) {
            entries[index].value = value;
            return;
        }

        /*
         * Nếu key chưa tồn tại thì thêm Entry mới.
         */
        ensureCapacity();

        entries[size] = new Entry(key, value);
        size++;
    }

    /**
     * Lấy value theo key.
     *
     * Ví dụ:
     * get("GOLD")
     * trả về 5 nếu map đang có "GOLD" -> 5.
     *
     * Nếu không tìm thấy key thì trả về null.
     */
    public V get(K key) {
        int index = findIndexByKey(key);

        if (index == -1) {
            return null;
        }

        return entries[index].value;
    }

    /**
     * Xóa một cặp key-value theo key và trả về value bị xóa.
     *
     * Ví dụ:
     * Map có:
     * "MEMBER" -> 1
     *
     * remove("MEMBER")
     * trả về 1 và xóa Entry đó khỏi map.
     *
     * Nếu không tìm thấy key thì trả về null.
     */
    public V remove(K key) {
        int index = findIndexByKey(key);

        if (index == -1) {
            return null;
        }

        V removedValue = entries[index].value;

        /*
         * Dịch các phần tử phía sau lên trước 1 vị trí
         * để lấp chỗ trống sau khi xóa.
         */
        for (int i = index; i < size - 1; i++) {
            entries[i] = entries[i + 1];
        }

        entries[size - 1] = null;
        size--;

        return removedValue;
    }

    /**
     * Kiểm tra key có tồn tại trong map hay không.
     *
     * Trả về true nếu key tồn tại.
     * Trả về false nếu key không tồn tại.
     *
     * Ví dụ:
     * containsKey("GOLD") -> true
     * containsKey("DIAMOND") -> false
     */
    public boolean containsKey(K key) {
        return findIndexByKey(key) != -1;
    }

    /**
     * Kiểm tra map có rỗng hay không.
     *
     * Trả về true nếu map không có Entry nào.
     * Trả về false nếu map có ít nhất một Entry.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Trả về số lượng cặp key-value hiện có trong map.
     *
     * Ví dụ:
     * PLATINUM -> 7
     * GOLD     -> 5
     * SILVER   -> 3
     * MEMBER   -> 1
     *
     * size() trả về 4.
     */
    public int size() {
        return size;
    }

    /**
     * Xóa toàn bộ dữ liệu trong map.
     *
     * Sau khi clear:
     * toàn bộ Entry bị xóa
     * size = 0
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            entries[i] = null;
        }

        size = 0;
    }

    /**
     * Duyệt và in toàn bộ cặp key-value ra màn hình.
     *
     * Hàm này hữu ích cho app CLI khi muốn hiển thị cấu hình,
     * ví dụ booking window theo hạng thành viên.
     */
    public void display() {
        if (isEmpty()) {
            System.out.println("Map is empty.");
            return;
        }

        for (int i = 0; i < size; i++) {
            System.out.println(entries[i].key + " -> " + entries[i].value);
        }
    }

    /**
     * Tìm vị trí của Entry dựa theo key.
     *
     * Nếu tìm thấy key thì trả về index.
     * Nếu không tìm thấy thì trả về -1.
     *
     * Vì MyMap bản này dùng mảng tuyến tính,
     * nên phải duyệt từ đầu đến cuối để tìm key.
     */
    private int findIndexByKey(K key) {
        for (int i = 0; i < size; i++) {
            if (isKeyEqual(entries[i].key, key)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * So sánh hai key có bằng nhau không.
     *
     * Cần xử lý riêng trường hợp key bị null để tránh lỗi NullPointerException.
     *
     * NullPointerException nghĩa là lỗi xảy ra khi gọi hàm trên biến đang null.
     */
    private boolean isKeyEqual(K key1, K key2) {
        if (key1 == null && key2 == null) {
            return true;
        }

        if (key1 == null || key2 == null) {
            return false;
        }

        return key1.equals(key2);
    }

    /**
     * Tăng sức chứa của mảng nếu map đã đầy.
     *
     * Cách hoạt động:
     * 1. Nếu size < capacity thì chưa cần tăng.
     * 2. Nếu size == capacity thì tạo mảng mới có capacity gấp đôi.
     * 3. Copy dữ liệu từ mảng cũ sang mảng mới.
     */
    private void ensureCapacity() {
        if (size < capacity) {
            return;
        }

        int newCapacity = capacity * 2;
        Entry[] newEntries = new MyMap.Entry[newCapacity];

        for (int i = 0; i < size; i++) {
            newEntries[i] = entries[i];
        }

        entries = newEntries;
        capacity = newCapacity;
    }
}