package tn.esprit.spring;

import static org.junit.Assert.assertEquals;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.services.TimesheetServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimesheetServiceImplTest {

	@Autowired
	TimesheetServiceImpl TimesheetServiceImpl;
	@Autowired
	EmployeRepository EmployeRepository;
	@Autowired
	MissionRepository MissionRepository;

	@Test
	public void testajouterMission() {
		Mission c = new Mission("melek","good job");
		assertEquals(8, TimesheetServiceImpl.ajouterMission(c));

	}
	
	@Test
	public void testaffecterMissionADepartement() {
		TimesheetServiceImpl.affecterMissionADepartement(1, 3);
	}
	
	@Test
	public void testajouterTimesheet() {
		Date dateDebut = Date.valueOf("2020-10-07");
		Date dateFin = Date.valueOf("2020-10-10");
		TimesheetServiceImpl.ajouterTimesheet(1, 1, dateDebut, dateFin);
	}
	
	@Test
	public void testvaliderTimesheet() {
		Date dateDebut = Date.valueOf("2020-10-07");
		Date dateFin = Date.valueOf("2020-10-10");
		TimesheetServiceImpl.validerTimesheet(1, 1, dateDebut, dateFin, 1);
	}
	
	@Test
	public void testfindAllMissionByEmployeJPQL() {
		TimesheetServiceImpl.findAllMissionByEmployeJPQL(1);
	}
	
	@Test
	public void testgetAllEmployeByMission() {
		TimesheetServiceImpl.getAllEmployeByMission(1);
	}




}
