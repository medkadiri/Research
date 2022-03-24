package io.microhooks;


import javax.persistence.Id;

import io.microhooks.ddd.Sink;

@Sink(source = "Test_io.microhooks.TestEntity")
public class SinkTestEntity {
    
    @Id
    private long id;

}
