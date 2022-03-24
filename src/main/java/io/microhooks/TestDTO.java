package io.microhooks;

import lombok.Value;

@Value
public class TestDTO {
    private final long id;
    private final String name;
    
    public TestDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
