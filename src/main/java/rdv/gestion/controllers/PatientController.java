package rdv.gestion.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import rdv.gestion.repository.MedecinRepository;
import rdv.gestion.repository.PatientRepository;

@Controller
@SessionAttributes({ "droits" , "id"})
public class PatientController {

	public static boolean containsIgnoreCase(String str, String subString) {
		return str.toLowerCase().contains(subString.toLowerCase());
	}

	@Autowired // sert à creer une instance voir pattern singleton
	MedecinRepository medecinRepository;
	@Autowired
	PatientRepository patientRepository;

	@RequestMapping(value = { "patientRv" }, method = RequestMethod.GET)
	public String patientRv( Model model, HttpSession session) {	
		if (!session.getAttribute("droits").equals(3)) {
			return "redirect:/pageLogin";
		} else {		
			model.addAttribute("titre", String.format("Medecin"));
			model.addAttribute("medecins", medecinRepository.findAll());
			return "patientRv";
		}
	}
	@RequestMapping(value = { "patientRv/getMedecin/{medecin_id}" }, method = RequestMethod.GET)
	public String patientRvGetMedecin(@PathVariable("medecin_id") Integer medecin_id, Model model, HttpSession session) {	
		if (!session.getAttribute("droits").equals(3)) {
			return "redirect:/pageLogin";
		} else {			
			model.addAttribute("titre", String.format("Medecin"));
			model.addAttribute("medecins", medecinRepository.findAll());
			model.addAttribute("medecinInfo", medecinRepository.findById(medecin_id).get());
			model.addAttribute("patient", patientRepository.findByUser_id(Integer.parseInt(session.getAttribute("id").toString())));
			return "patientRv";
		}
	}	
}