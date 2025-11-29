package cruds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.Paths;

@EnableCaching
@SpringBootApplication
public class CrudImagemApplication {

	public static void main(String[] args) {
		ensureSuperUser();

		String logFolder = System.getProperty("logFolder");
		if (logFolder == null) {
			logFolder = System.getProperty("user.home") + "/Desktop/S3 local/logs";
			System.setProperty("logFolder", logFolder);
		}
		SpringApplication.run(CrudImagemApplication.class, args);
	}

	private static void ensureSuperUser() {
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
			props.load(fis);
		} catch (Exception e) {
			System.err.println("Não foi possível carregar application.properties");
			e.printStackTrace();
			return;
		}

		String url = props.getProperty("spring.datasource.url");
		String user = props.getProperty("spring.datasource.username");
		String password = props.getProperty("spring.datasource.password");

		try (Connection conn = DriverManager.getConnection(url, user, password);
			 Statement stmt = conn.createStatement()) {
			String script = new String(Files.readAllBytes(Paths.get("scripts/init-super-user.sql")));
			for (String sql : script.split(";")) {
				if (!sql.trim().isEmpty()) {
					stmt.execute(sql);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
