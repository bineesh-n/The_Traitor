/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package the_traitor;

/**
 * Hash Table To Store&Sort Contacts, store infinite number of components.
 * @author Bineesh
 */
public class ComponentBuffer {
    
    private ContactItem[] HashTable;
    private Component Header;
    private boolean sort = false;
    
    public ComponentBuffer(boolean requireSorting) {
        this.sort = requireSorting;
    }
    /**
     * Adds a contact item
     * @param cit contact item object
     */
    public void add(ContactItem cit) {
        
        if(!sort) {
            if(Header == null) {
                Header = cit;
                return;
            }
            Component tmp = Header;
            while(tmp.next != null) {
                tmp = tmp.next;
            }
            
            tmp.next = cit;
            return;
        }
        
        String HashBase = cit.name;
        if(HashBase.equals("")) {
            HashBase = " ";
        }
        
        char HashLabel = HashBase.charAt(0);
        int HashIndex = detectHashIndex(HashLabel);
        insertAt(HashIndex, HashBase, cit);
    }
    /**
     * 
     * @param ta TextArea Object
     */
    public void add(TextArea ta) {
        
        if(!sort) {
            if(Header == null) {
                Header = ta;
                return;
            }
            Component tmp = Header;
            while(tmp.next != null) {
                tmp = tmp.next;
            }
            
            tmp.next = ta;
        }
        
    }
    /**
     * 
     * @param l label object
     */
    public void add(Label l) {
        
        if(!sort) {
            if(Header == null) {
                Header = l;
                return;
            }
            Component tmp = Header;
            while(tmp.next != null) {
                tmp = tmp.next;
            }
            
            tmp.next = l;
        }
        
        
    }
    
    private int detectHashIndex(char HashLabel) {
        if((int)HashLabel < 65) {
            return 0;
        }
        else if(HashLabel >= 'A' && HashLabel <= 'z') {
            int index = HashLabel, val = 27;
            if(index > 64 && index < 92) {
                val = index - 64;
            }
            else if(index > 96 && index < 124){
                val = index - 96;
            }
            return val;
        }
        else {
            return 27;
        }
    }
    
    private void insertAt(int HashIndex, String Base, ContactItem cit) {
        
        if(HashTable == null) {
            HashTable = new ContactItem[28];
        }
        
        if(HashTable[HashIndex] == null) {
            HashTable[HashIndex] = cit;
        }
        else {
            sortInsert(HashIndex, Base, cit);
        }
        
    }
    
    private void sortInsert(int HeaderIndex, String Key, ContactItem cit) {
        
        ContactItem Header = HashTable[HeaderIndex];
        Component node1 = Header.next, node2 = Header;
        boolean inserted = false;
        
        if(Header.name.compareTo(Key) > -1) {
            cit.next = Header;
            HashTable[HeaderIndex] = cit;
            return;
        }
        
        while(node1 != null && !inserted) {
            int firstComp = node1.name.compareTo(Key);
            int secComp = node2.name.compareTo(Key);
            
            if(firstComp > -1 && secComp < 0) {
                node2.next = cit;
                cit.next = node1;
                inserted = true;
            }
            
            node1 = node1.next;
            node2 = node2.next;
        }
        
        if(!inserted) {
            node2.next = cit;
        }
        
    }
    /**
     * 
     * @return component head
     */
    public Component getComponents() {
        
        if(!sort) {
            setPositions();
            return Header;
        }
        else if(HashTable == null) {
            return null;
        }
        
        boolean HeaderSet = false;
        for(int i = 0; i < 28; i++) {
            
            if(HashTable[i] != null && !HeaderSet) {
                Header = HashTable[i];
                HeaderSet = true;
            }
            
            int j = linkNext(i);
            if(j > 0) {
                i = j - 1;
            }
            else if(j == -2) {
                break;
            }
            
        }
        
        setPositions();
        
        return Header;
    }
    /**
     * Give links and make the components flow.
     */
    private void setPositions() {
        
        Component ptr = Header, prev = null;
        
        
        while(ptr != null) {
            
            if(prev != null) {
                ptr.prev = prev;
            }
            
            prev = ptr;
            
            if(ptr.next == null) {
                Header.prev = ptr;
            }
            
            ptr = ptr.next;
            
        }
        
    }
    
    private int linkNext(int HashIndex) {
       // System.out.println(HashIndex);
        Component ptr = HashTable[HashIndex];
        
        if(ptr == null) {
            return -1;
        }
        
        while(ptr.next != null) {
            ptr = ptr.next;
        }
        
        int i = next(HashIndex + 1);
        if(i == -1) {
            return -2;
        }
        ptr.next = HashTable[i];
      //  System.out.println(ptr.name+"->"+HashTable[i].name);
        return i;
        
    }
    
    private int next(int HashIndex) {
        
        for( int i = HashIndex; i < 28; i++) {
            
            ContactItem com = HashTable[i];
            if(com != null) {
                return i;
            }
        }
        
        return -1;
        
    }
    
}
