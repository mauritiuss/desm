package AdministrationServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Questa classe è usata per far partire il Server
 */
@SpringBootApplication
public class AdministrationServer {

    public static void main(String[] args) {
        SpringApplication.run(AdministrationServer.class, args);
        System.out.println("Server running on http://localhost:8080");
    }
}
