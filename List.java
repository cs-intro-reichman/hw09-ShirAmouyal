
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;

    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }

    /** Returns the number of elements in this list. */
    public int getSize() {
        return size;
    }

    /** Returns the first element in the list */
    public CharData getFirst() {
        return first.cp;
    }

    /**
     * GIVE Adds a CharData object with the given character to the beginning of this
     * list.
     */
    public void addFirst(char chr) {
        CharData newCharData = new CharData(chr);
        Node newNode = new Node(newCharData);
        newNode.next = this.first; 
        first = newNode; 
        size++;
    }

    /** GIVE Textual representation of this list. */
    public String toString() {
        if (size == 0) {
            return "()";
        }

        String str = "(";
        Node current = first; 
        while (current != null) {
            str += current.cp.toString() + " "; 
            current = current.next; 
        }

        return str.substring(0, str.length() - 1) + ")";
    }

    public int indexOf(char chr) {
        Node current = first;
        int i = 0;
        while (current != null) {
            if (current.cp.equals(chr)) {
                return i;
            }
            current = current.next;
            i++;
        }
        return -1; 

    }

    public void update(char chr) {
        int count = 0;
        Node current = first;
        int indexToUpdate = indexOf(chr);

        if (indexToUpdate == -1) {
            addFirst(chr);
        } else {
            while (count < indexToUpdate) {
                count++;
                current = current.next;
            }
            current.cp.count++;
        }
    }

    public boolean remove(char chr) {
        Node p = null;
        Node current = first;
        while (current != null && !current.cp.equals(chr)) {
            p = current;
            current = current.next;
        }
        if (current == null) {
            return false; 
        }
        
        if (p == null) { 
            first = first.next;
        } else {
            p.next = current.next;
        }
        size--;
        return true;
    }

    public CharData get(int index) {
        if (index >= this.size || index < 0) {
            throw new IndexOutOfBoundsException("illegal index " + index);
        }
        Node current = this.first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.cp;
    }

    
    public CharData[] toArray() {
        CharData[] arr = new CharData[size];
        Node current = first;
        int i = 0;
        while (current != null) {
            arr[i++] = current.cp;
            current = current.next;
        }
        return arr;
    }

    
    public ListIterator listIterator(int index) {
        if (size == 0)
            return null;
        Node current = first;
        int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        return new ListIterator(current);

    }
}
