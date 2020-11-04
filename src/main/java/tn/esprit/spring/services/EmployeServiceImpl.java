
package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.management.relation.Role;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class EmployeServiceImpl implements IEmployeService {

	private static final Logger l = Logger.getLogger(EntrepriseServiceImpl.class);
	
	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	ContratRepository contratRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;

	@Override
	public Employe authenticate(String login, String password) {
		return employeRepository.getEmployeByEmailAndPassword(login, password);
	}

	@Override
	public int addOrUpdateEmploye(Employe employe) {
		employeRepository.save(employe);
		return employe.getId();
	}


	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		l.info("Fonction mettreAjourEmailByEmployeId() : ");
		l.debug("Nous allons modifier l'email de l'employé "+employeId);
		
		Optional<Employe> emp = employeRepository.findById(employeId);
		if(emp.isPresent()){
			Employe employe = emp.get();
			employe.setEmail(email);
			employeRepository.save(employe);
			l.debug("la modification de l'email est termine.");
			l.info("la modification de l'email est termine SANS ERREUR.");
			return;
		}
		l.error("ERREUR !! Au niveau de update mettreAjourEmailByEmployeId()");
	}

	@Transactional	
	public void affecterEmployeADepartement(int employeId, int depId) {
		Optional<Departement> depManEnt = deptRepoistory.findById(depId);
		Optional<Employe> empManEnt = employeRepository.findById(employeId);
		if(depManEnt.isPresent()){
			Departement depManagedEntity = depManEnt.get();
			if(empManEnt.isPresent()){
				Employe employeManagedEntity = empManEnt.get();

				if(depManagedEntity.getEmployes() == null){

					List<Employe> employes = new ArrayList<>();
					employes.add(employeManagedEntity);
					depManagedEntity.setEmployes(employes);
				}else{

					depManagedEntity.getEmployes().add(employeManagedEntity);
				}

				// à ajouter? 
				deptRepoistory.save(depManagedEntity); 
			}
			
		}

	}
	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId)
	{
		Optional<Departement> departement = deptRepoistory.findById(depId);
		if(departement.isPresent()){
			Departement dep = departement.get();

			int employeNb = dep.getEmployes().size();
			for(int index = 0; index < employeNb; index++){
				if(dep.getEmployes().get(index).getId() == employeId){
					dep.getEmployes().remove(index);
					break;//a revoir
				}
			}
		}
		
	} 
	
	// Tablesapce (espace disque) 

	public int ajouterContrat(Contrat contrat) {
		contratRepoistory.save(contrat);
		return contrat.getReference();
	}

	public void affecterContratAEmploye(int contratId, int employeId) {
		Optional<Contrat> contManEnt = contratRepoistory.findById(contratId);
		Optional<Employe> empManEnt = employeRepository.findById(employeId);
		if(contManEnt.isPresent()){
			Contrat contratManagedEntity = contManEnt.get();
			if(empManEnt.isPresent()){
				Employe employeManagedEntity = empManEnt.get();

				contratManagedEntity.setEmploye(employeManagedEntity);
				contratRepoistory.save(contratManagedEntity);
			}
		}
		
		

	}

	public String getEmployePrenomById(int employeId) {
		Optional<Employe> emp = employeRepository.findById(employeId);
		if(emp.isPresent()){
			Employe employeManagedEntity = emp.get();
			return employeManagedEntity.getPrenom();
		}
		return "";
	}
	 
	public void deleteEmployeById(int employeId)
	{
		Optional<Employe> emp = employeRepository.findById(employeId);
		if(emp.isPresent()){
			Employe employe = emp.get();

			//Desaffecter l'employe de tous les departements
			//c'est le bout master qui permet de mettre a jour
			//la table d'association
			for(Departement dep : employe.getDepartements()){
				dep.getEmployes().remove(employe);
			}

			employeRepository.delete(employe);
		}
		
	}

	public void deleteContratById(int contratId) {
		Optional<Contrat> contrat = contratRepoistory.findById(contratId);
		if(contrat.isPresent()){
			Contrat contratManagedEntity = contrat.get();
			contratRepoistory.delete(contratManagedEntity);
		}
	}

	public int getNombreEmployeJPQL() {
		return employeRepository.countemp();
	}

	public List<String> getAllEmployeNamesJPQL() {
		return employeRepository.employeNames();

	}

	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}
	public void deleteAllContratJPQL() {
		employeRepository.deleteAllContratJPQL();
	}

	public float getSalaireByEmployeIdJPQL(int employeId) {
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	public Double getSalaireMoyenByDepartementId(int departementId) {
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}

	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
			Date dateFin) {
		return timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
	}

	public List<Employe> getAllEmployes() {
		List<Employe> ls = new ArrayList<Employe>();
		try {
			l.info("In getAllEmployes : ");
			l.debug("Je vais récupérer les employes");
			ls.add(new Employe(1,"Abdelaziz","Mezri","straktony94@gmail.com","Abdelaziz",true));
			l.debug("Je viens de récupérer les employes");
			l.debug("Je viens de finir getAllEmployes()");
			l.info("Out getAllEmployes() without errors.");
			
		}catch (Exception e) { 
			l.error("Erreur dans getAllEmployes() : " + e); 
		}
		
		return ls;
		
	}

}
