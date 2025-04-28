package cruds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class CrudImagemApplication {

	public static void main(String[] args) {
		String logFolder = System.getProperty("logFolder");
		if (logFolder == null) {
			logFolder = System.getProperty("user.home") + "/Desktop/S3 local/logs";
			System.setProperty("logFolder", logFolder);
		}
		SpringApplication.run(CrudImagemApplication.class, args);
	}

}
