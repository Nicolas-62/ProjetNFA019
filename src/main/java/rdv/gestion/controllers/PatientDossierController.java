package rdv.gestion.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import rdv.gestion.repository.PatientRepository;

@Controller
/*
 * Attributs de session
 */
@SessionAttributes({ "droits", "id", "model_id" })
public class PatientDossierController {
	@Autowired
	PatientRepository patientRepository;
	/*
	 * Titre de la page
	 */	
	@ModelAttribute("titre")
	private String titre() {
		String titre = "Patient";
		return titre;
	}
	/*
	 * Affiche le dossier d'un patient dans la page patientdossier.html
	 */
	@RequestMapping(value = { "/patientDossier" }, method = RequestMethod.GET)
	public String patientAjoutRv(Model model, HttpSession session) {		
		if (!"3".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {			
			model.addAttribute("patient", patientRepository.findById(Integer.parseInt(session.getAttribute("model_id").toString())).get());
			return "patientDossier";
		}
	}
}
