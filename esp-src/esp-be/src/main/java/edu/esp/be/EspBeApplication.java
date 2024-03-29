package edu.esp.be;

import edu.esp.database.DBFacadeImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import javax.sql.DataSource;

@ComponentScan(basePackages = {"edu.esp.be", "edu.esp.database", "edu.esp.system_entities", "edu.esp.controllers"})
@CrossOrigin(origins = "http://localhost:4200")
@SpringBootApplication
public class EspBeApplication implements CommandLineRunner {

	private final DBFacadeImp dbFacade;
	private final ApplicationContext applicationContext;

	@Autowired
	public EspBeApplication(DBFacadeImp dbFacade, ApplicationContext applicationContext){
		this.dbFacade = dbFacade;
		this.applicationContext = applicationContext;
		DataSource ds = (DataSource) this.applicationContext.getBean(DataSource.class);		// in case of being needed
//		Connection c = ds.getConnection();
	}

	public static void main(String[] args) {
		SpringApplication.run(EspBeApplication.class, args);
		System.out.println("Ready to serve ..");
	}
	@Override
	public void run(String... args) throws Exception {
//		dbFacade.test();
	}
}
