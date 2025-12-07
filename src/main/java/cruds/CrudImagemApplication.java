package cruds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

		// Carrega application.properties do classpath
		try (InputStream fis = CrudImagemApplication.class
				.getClassLoader()
				.getResourceAsStream("application.properties")) {

			if (fis == null) {
				System.err.println("application.properties não encontrado no classpath");
				return;
			}

			props.load(fis);

		} catch (Exception e) {
			System.err.println("Falha ao carregar application.properties");
			e.printStackTrace();
			return;
		}

		String url = System.getenv("DB_URL");
		if (url == null || url.isEmpty()) {
			url = props.getProperty("spring.datasource.url");
		}

		String user = System.getenv("DB_USERNAME");
		if (user == null || user.isEmpty()) {
			user = props.getProperty("spring.datasource.username");
		}

		String password = System.getenv("DB_PASSWORD");
		if (password == null || password.isEmpty()) {
			password = props.getProperty("spring.datasource.password");
		}


		try (Connection conn = DriverManager.getConnection(url, user, password);
			 Statement stmt = conn.createStatement()) {

			// Carrega o SQL do classpath
			InputStream scriptStream = CrudImagemApplication.class
					.getClassLoader()
					.getResourceAsStream("scripts/init-super-user.sql");

			if (scriptStream == null) {
				System.err.println("init-super-user.sql não encontrado no classpath");
				return;
			}

			String script = new String(scriptStream.readAllBytes());

			for (String sql : script.split(";")) {
				if (!sql.trim().isEmpty()) {
					stmt.execute(sql);
				}
			}

		} catch (Exception e) {
			System.err.println("Erro ao executar init-super-user.sql");
			e.printStackTrace();
		}
	}

}