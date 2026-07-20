
package project_of._dsa;

public class DynamicArray<E> {
    private E[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public DynamicArray() {
        data = (E[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public DynamicArray(int initialCapacity) {
        data = (E[]) new Object[initialCapacity];
        size = 0;
    }

    public void add(E element) {
        if (size >= data.length) {
            resize(data.length * 2);
        }
        data[size++] = element;
    }

    public void insertAt(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (size >= data.length) {
            resize(data.length * 2);
        }
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = element;
        size++;
    }

    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return data[index];
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        E removed = data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--size] = null;
        if (size > 0 && size < data.length / 4) {
            resize(data.length / 2);
        }
        return removed;
    }

    public boolean remove(E element) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (data[i] != null && data[i].equals(element)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    public int indexOf(E element) {
        for (int i = 0; i < size; i++) {
            if (data[i] != null && data[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(E element) {
        return indexOf(element) != -1;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    private void resize(int newCapacity) {
        E[] newData = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    public E[] toArray() {
        E[] result = (E[]) new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = data[i];
        }
        return result;
    }

    public void quickSort(java.util.Comparator<E> comparator) {
        quickSort(0, size - 1, comparator);
    }

    private void quickSort(int low, int high, java.util.Comparator<E> comparator) {
        if (low < high) {
            int pi = partition(low, high, comparator);
            quickSort(low, pi, comparator);
            quickSort(pi + 1, high, comparator);
        }
    }

    private int partition(int low, int high, java.util.Comparator<E> comparator) {
        E pivot = data[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (comparator.compare(data[j], pivot) <= 0) {
                i++;
                E temp = data[i];
                data[i] = data[j];
                data[j] = temp;
            }
        }
        E temp = data[i + 1];
        data[i + 1] = data[high];
        data[high] = temp;
        return i;
    }
}
