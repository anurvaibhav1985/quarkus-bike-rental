package org.acme;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SnippetsTest {
    
    // Rate limiter test
    @Test
    public void testDates(){
        Instant now = Instant.now();
        Instant oneMinBack = now.minusSeconds(60);
        System.out.println(now.getEpochSecond());
        System.out.println(oneMinBack.getEpochSecond());

        // Put them into sorted set in redis , This is time series , if we have more than 3 requests , reject
    }
}
