package rdv.gestion.controllers;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import rdv.gestion.model.Patient;
import rdv.gestion.model.Rv;
import rdv.gestion.repository.CreneauxRepository;
import rdv.gestion.repository.MedecinRepository;
import rdv.gestion.repository.PatientRepository;
import rdv.gestion.repository.RvRepository;

@Controller
@SessionAttributes({ "droits", "id", "patient_id" })
public class PatientAjoutRvController {

	@ModelAttribute("titre")
	private String titre() {
		String titre = "Patient";
		return titre;
	}
	@Autowired // sert à creer une instance voir pattern singleton
	MedecinRepository medecinRepository;
	@Autowired
	PatientRepository patientRepository;
	@Autowired
	RvRepository rvRepository;
	@Autowired
	CreneauxRepository creneauxRepository;

	@RequestMapping(value = { "/patientAjoutRv" }, method = RequestMethod.GET)
	public String patientAjoutRv(Model model, HttpSession session) {		
		if (!"3".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {			
			model.addAttribute("medecins", medecinRepository.findAll());
			return "patientAjoutRv";
		}
	}

	@RequestMapping(value = { "/patientAjoutRv/getMedecin/{medecin_id}" }, method = RequestMethod.GET)
	public String patientAjoutRvGetMedecin(@PathVariable("medecin_id") Integer medecin_id, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		if (!"3".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {
			if (!medecinRepository.findById(medecin_id).isPresent()) {
				redirectAttributes.addFlashAttribute("message",
						String.format("Ce medecin n'existe pas."));	
				return "redirect:/patientAjoutRv";
			} else {
				// on recherche le patient par son id "patient_id" stocké dans la session
				model.addAttribute("patient", patientRepository.findById(Integer.parseInt(session.getAttribute("patient_id").toString())));
				model.addAttribute("medecinInfo", medecinRepository.findById(medecin_id).get());
				model.addAttribute("medecins", medecinRepository.findAll());
				return "/patientAjoutRv";
			}
		}
	}

	@RequestMapping(value = { "/patientAjoutRv/addRv" }, method = RequestMethod.POST)
	public String patientAjoutRvAddRv(Model model, HttpSession session, @RequestParam(value = "date") String date,
			@RequestParam(value = "creneaux") String creneau_id,
			RedirectAttributes redirectAttributes){
		System.out.println("date : "+date+ "creneau id : "+creneau_id);
		if (!"3".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {
			// création du rendez vous
			Rv rv = new Rv();
			// récupération et conversion de la date
			Date stringToDate;
			try {
				stringToDate=new SimpleDateFormat("yyyy-MM-dd").parse(date);
			}catch(Exception e) {
				redirectAttributes.addFlashAttribute("message",
						String.format("Le format de la date est incorrect."));
				return "redirect:/patientAjoutRv";
			}
			rv.setDate(stringToDate);
			rv.setCreneaux(creneauxRepository.findById(Integer.parseInt(creneau_id)).get());
			rv.setPatient(patientRepository.findById(Integer.parseInt(session.getAttribute("patient_id").toString())).get()); 
			// on essaye de sauvegarder le creneaux, si il est déjà pris un erreur sql est levé
			try {
				rvRepository.save(rv);
			}catch(RuntimeException e) {
				redirectAttributes.addFlashAttribute("message",
						String.format("Ce creneau est déjà réservé"));
				return "redirect:/patientAjoutRv";				
			}
			redirectAttributes.addFlashAttribute("message",
					String.format("Rendez vous ajouté !"));
			return "redirect:/patientAjoutRv";
		}
	}
}