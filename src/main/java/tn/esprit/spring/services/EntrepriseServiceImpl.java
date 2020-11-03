package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EntrepriseRepository;

@Service
public class EntrepriseServiceImpl implements IEntrepriseService 
{
	private static final Logger l = Logger.getLogger(EntrepriseServiceImpl.class);

	@Autowired
    EntrepriseRepository entrepriseRepoistory;
	@Autowired
	DepartementRepository deptRepoistory;
	
	
	
	
	public int ajouterEntreprise(Entreprise entreprise) 
	{
		try 
		{
			l.info("Fonction ajouterEntreprise() : ");
			l.debug("Nous allons commencer l ajout de l entreprise");
		
			entrepriseRepoistory.save(entreprise);
			l.debug("Nous avons lancer  l'ajout de l entreprise" + entreprise.getId());
			l.debug("l'ajout de l entreprise est termine.");
			l.info("l ajout de l entreprise a ete faite SANS ERREUR.");
		}
		catch (Exception e) 
		{
			l.error("ERREUR !! Au niveau de ajouterEntreprise() :"+e);
		}
		return entreprise.getId();
	}

	
	
	
	public int ajouterDepartement(Departement dep) 
	{
		try 
		{
			l.info("Fonction ajouterDepartement() : ");
			l.debug("Nous allons commencer l ajout du departement.");
		
			deptRepoistory.save(dep);
			l.debug("Nous avons lancer  l'ajout du departement : " + dep.getId());
			l.debug("l'ajout du departement est termine.");
			l.info("l ajout du departement a ete faite SANS ERREUR.");
		}
		catch (Exception e) 
		{
			l.error("ERREUR !! Au niveau de ajouterDepartement() :"+e);
		}
		return dep.getId();
	}
	
	
	
	
	public void affecterDepartementAEntreprise(int depId, int entrepriseId) 
	{
		//Le bout Master de cette relation N:1 est departement  
				//donc il faut rajouter l'entreprise a departement 
				// ==> c'est l'objet departement(le master) qui va mettre a jour l'association
				//Rappel : la classe qui contient mappedBy represente le bout Slave
				//Rappel : Dans une relation oneToMany le mappedBy doit etre du cote one.
		
		try 
		{
				l.info("Fonction affecterDepartementAEntreprise() : ");
				
				Optional<Entreprise> ent = entrepriseRepoistory.findById(entrepriseId);
				Optional<Departement> dep = deptRepoistory.findById(depId);
				
				if(ent.isPresent())
				{
					l.debug("Nous allons faire la recuperation de l'entreprise.");
					Entreprise entreprise = ent.get();
					l.debug("Nous avons recuperer l'entreprise." + entreprise.getName());
					
					if(dep.isPresent())
					{
						l.debug("Nous allons faire la recuperation du departement.");
						Departement departement = dep.get();
						l.debug("Nous avons recuperer le departement." + departement.getName());
						
						l.debug("Nous allons faire l affectation.");
						departement.setEntreprise(entreprise);
						
						deptRepoistory.save(departement);
						
						l.debug("Nous avons terminer l'affectation .");
						l.info("Affectation faite SANS ERREUR");
					}
				}
			}
		catch (Exception e) 
		{
				l.error("ERREUR !! Au niveau de affecterDepartementAEntreprise() :"+e);
		}
	}
	
	
	
	public List<String> getAllDepartementsNamesByEntreprise(int entrepriseId) 
	{
		List<String> depNames = new ArrayList<>();
		Optional<Entreprise> ent = entrepriseRepoistory.findById(entrepriseId);

		try 
		{
			if(ent.isPresent())
			{
				l.info("Fonction getAllDepartementsNamesByEntreprise() : ");
				l.debug("Nous allons faire la recuperation des departements.");
				Entreprise entreprise = ent.get();
				
				for(Departement dep : entreprise.getDepartements())
				{
					depNames.add(dep.getName());
				}
			
				l.debug("Nous allons faire la recuperation des departements de l'entreprise " + entreprise.getName());
				l.debug("Nous avons finis la recuperation .");
				l.info("La recuperation des departement a ete faite SANS ERREUR.");
				
			}
		}
		catch (Exception e) 
		{
			l.error("ERREUR !! Au niveau de getAllDepartementsNamesByEntreprise() :"+e);
		}
		
		return depNames;
	}
	
	
	

	@Transactional
	public void deleteEntrepriseById(int entrepriseId) 
	{
		Optional<Entreprise> ent = entrepriseRepoistory.findById(entrepriseId);
		if(ent.isPresent())
		{
			Entreprise entreprise = ent.get();
			entrepriseRepoistory.delete(entreprise);
		}
	}

	@Transactional
	public void deleteDepartementById(int depId) 
	{
		Optional<Departement> dep = deptRepoistory.findById(depId);
		if(dep.isPresent())
		{
			Departement departement = dep.get();
			deptRepoistory.delete(departement);
		}
	}


	public Entreprise getEntrepriseById(int entrepriseId) 
	{
		Entreprise entrepriseManagedEntity = new Entreprise();	
		Optional<Entreprise> ent = entrepriseRepoistory.findById(entrepriseId);
		
		try 
		{
			l.info("Fonction getEntrepriseById() : ");
			l.debug("Nous allons faire la recuperation de l'entreprise.");
			
			if(ent.isPresent())
			{
				Entreprise entreprise = ent.get();
				l.debug("Recuperation des donnees de l'entreprise " + entreprise.getName());
				l.debug("Nous avons finis la recuperation de l entreprise .");
				l.info("La recuperation de l entreprise  a ete faite SANS ERREUR.");
			}	
		}
		catch (Exception e) 
		{
			l.error("ERREUR !! Au niveau de getEntrepriseById() :"+e);
		}
		return 	entrepriseManagedEntity;
	}	
	

}
