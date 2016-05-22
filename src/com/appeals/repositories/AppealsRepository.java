package com.appeals.repositories;

import java.util.HashMap;
import java.util.Map.Entry;

import com.appeals.object.Identifier;
import com.appeals.object.StudentAppeal;

public class AppealsRepository {
    
    private static final AppealsRepository theRepository = new AppealsRepository();
    private HashMap<String, StudentAppeal> backingStore = new HashMap<>(); // Default implementation, not suitable for production!

    public static AppealsRepository current() {
        return theRepository;
    }
    
    private AppealsRepository(){
    }
    
    public StudentAppeal get(Identifier identifier) {
        return backingStore.get(identifier.toString());
     }
    
    public StudentAppeal take(Identifier identifier) {
    	StudentAppeal appeal = backingStore.get(identifier.toString());
        remove(identifier);
        return appeal;
    }

    public Identifier store(StudentAppeal appeal) {
                
        Identifier id = new Identifier();                
        backingStore.put(id.toString(), appeal);
        return id;
    }
    
    public void store(Identifier identifier, StudentAppeal appeal) {
        backingStore.put(identifier.toString(), appeal);
    }

    public boolean has(Identifier identifier) {
        
        boolean result =  backingStore.containsKey(identifier.toString());
        
        return result;
    }

    public void remove(Identifier identifier) {
        backingStore.remove(identifier.toString());
    }
    
    public boolean appealPlaced(Identifier identifier) {
        return AppealsRepository.current().has(identifier);
    }
    
    public boolean appealNotPlaced(Identifier identifier) {
        return !appealPlaced(identifier);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Entry<String, StudentAppeal> entry : backingStore.entrySet()) {
            sb.append(entry.getKey());
            sb.append("\t:\t");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    public synchronized void clear() {
        backingStore = new HashMap<>();
    }

    public int size() {
        return backingStore.size();
    }
}
