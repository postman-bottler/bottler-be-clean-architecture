package online.bottler;

import org.springframework.stereotype.Component;

@Component
public class IdGenerator {
    private long currentId = 1;

    public synchronized long generateId() {
        return currentId++;
    }
}
