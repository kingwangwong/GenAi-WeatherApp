//import org.junit.Test;
//import static org.junit.jupiter.Assert.*;
//
//public class WeatherAppTest {
//
//    @Test
//    public void testSendHttpRequest_ValidUrl() throws Exception {
//        String url = "https://nominatim.openstreetmap.org/search?q=London&format=json&limit=1";
//        String response = WeatherApp.sendHttpRequest(url);
//        assertNotNull(response);
//        assertTrue(response.startsWith("["));
//    }
//
//    @Test
//    public void testSendHttpRequest_InvalidUrl() {
//        String url = "https://invalid.openstreetmap.org/search";
//        Exception exception = assertThrows(Exception.class, () -> {
//            WeatherApp.sendHttpRequest(url);
//        });
//        assertNotNull(exception.getMessage());
//    }
//}

import org.junit.Test;
import static org.junit.Assert.*;

public class WeatherAppTest {

    @Test
    public void testSendHttpRequest_ValidUrl() throws Exception {
        String url = "https://nominatim.openstreetmap.org/search?q=London&format=json&limit=1";
        String response = WeatherApp.sendHttpRequest(url);
        assertNotNull("Response should not be null", response);
        assertTrue("Response should start with [", response.startsWith("["));
    }

    @Test
    public void testSendHttpRequest_InvalidUrl() {
        String url = "https://invalid.openstreetmap.org/search";
        try {
            WeatherApp.sendHttpRequest(url);
            fail("Exception expected for invalid URL");
        } catch (Exception e) {
            assertNotNull("Exception message should not be null", e.getMessage());
        }
    }
}