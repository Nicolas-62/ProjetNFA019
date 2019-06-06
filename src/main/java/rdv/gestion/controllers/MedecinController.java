package rdv.gestion.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import rdv.gestion.repository.MedecinRepository;
import rdv.gestion.repository.PatientRepository;
import rdv.gestion.repository.RvRepository;
import rdv.gestion.repository.UserRepository;

@Controller
@SessionAttributes({ "droits" , "model_id"})
public class MedecinController {

	public static boolean containsIgnoreCase(String str, String subString) {
		return str.toLowerCase().contains(subString.toLowerCase());
	}

	@Autowired // sert à creer une instance voir pattern singleton
	MedecinRepository medecinRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PatientRepository patientRepository;
	@Autowired
	RvRepository rvRepository;
	@RequestMapping(value = { "medecinCherche" }, method = RequestMethod.GET)
	public String medecinCherche(Model model) {
		return "medecinCherche";
	}

	@RequestMapping(value = { "medecinListeRv" }, method = RequestMethod.GET)
	public String medecin(Model model, HttpSession session) {
		model.addAttribute("patients", patientRepository.findByRvMedecin(Integer.parseInt(session.getAttribute("model_id").toString())));
		
		return "medecinListeRv";
	}
	@RequestMapping(value = { "medecinListeRv/getPatient/{patient_id}"}, method = RequestMethod.GET)
	public String medecinGetPatientRv(@PathVariable("patient_id") Integer patient_id, Model model,
			HttpSession session) {
		model.addAttribute("patients", patientRepository.findByRvMedecin(Integer.parseInt(session.getAttribute("model_id").toString())));
		model.addAttribute("patientRv", rvRepository.findByPatientMedecin(patient_id, Integer.parseInt(session.getAttribute("model_id").toString())));
		model.addAttribute("patient", patientRepository.findById(patient_id).get());
		return "medecinListeRv";
	}

}
