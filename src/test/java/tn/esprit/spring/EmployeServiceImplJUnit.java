package tn.esprit.spring;

import tn.esprit.spring.services.EmployeServiceImpl;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmployeServiceImplJUnit {

	EmployeServiceImpl es = new EmployeServiceImpl();

	@Test
	public void mettreAjourEmailByEmployeIdTest() {
		System.out.println(es.getAllEmployes());
	}

}
