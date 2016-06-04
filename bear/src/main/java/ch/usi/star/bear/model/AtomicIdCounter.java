// http://www.javacirecep.com/utility/java-ways-to-generate-unique-ids-in-java/

package ch.usi.star.bear.model;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicIdCounter {
    private static AtomicLong counter = new AtomicLong(0);
 
    public static long nextId() {
        return counter.incrementAndGet();     
    } 
}
