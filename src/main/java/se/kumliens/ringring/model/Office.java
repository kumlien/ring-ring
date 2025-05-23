package se.kumliens.ringring.model;

import lombok.Data;

@Data
public class Office {
    String name, address;
    VirtualNumber virtualNumber;

    public boolean hasVirtualNumber() {
        return virtualNumber != null;
    }
}
