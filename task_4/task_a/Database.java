import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {
    private Map<String, String> data = new HashMap<>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    public void addRecord(String name, String phone) {
        lock.writeLock().lock();
        try {
            data.put(name, phone);
            saveToDatabase();
        } 
        finally {
            lock.writeLock().unlock();
        }
    }

    public String findPhoneByName(String name) {
        lock.readLock().lock();
        try {
            return data.get(name);
        } 
        finally {
            lock.readLock().unlock();
        }
    }

    public String findNameByPhone(String phone) {
        lock.readLock().lock();
        try {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                if (entry.getValue().equals(phone)) {
                    return entry.getKey();
                }
            }
            return "Not found";
        } 
        finally {
            lock.readLock().unlock();
        }
    }

    public void loadFromDatabase() {
        try (BufferedReader reader = new BufferedReader(new FileReader("database.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                data.put(parts[0], parts[1]);
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToDatabase() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("database.txt"))) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                writer.write(entry.getKey() + " - " + entry.getValue());
                writer.newLine();
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}