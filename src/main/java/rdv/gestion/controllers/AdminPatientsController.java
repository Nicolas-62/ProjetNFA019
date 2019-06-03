package rdv.gestion.controllers;

import java.util.Iterator;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import rdv.gestion.model.Patient;
import rdv.gestion.repository.PatientRepository;
import rdv.gestion.repository.UserRepository;

@Controller
@SessionAttributes({ "droits", "id" })
public class AdminPatientsController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PatientRepository patientRepository;

	@ModelAttribute("titre")
	private String titre() {
		String titre = "Patients";
		return titre;
	}
	private static boolean containsIgnoreCase(String str, String subString) {
		return str.toLowerCase().contains(subString.toLowerCase());
	}

	@RequestMapping(value = { "/adminPatients" }, method = RequestMethod.GET)
	public String adminPatients(@ModelAttribute("patient") Patient patient, Model model, HttpSession session) {
		if (!"1".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {
			model.addAttribute("patients", patientRepository.findAll());
			return "adminPatients";
		}
	}

	@RequestMapping(value = { "/adminPatients/getPatient/{patient_id}" }, method = RequestMethod.GET)
	public String adminPatientsGetPatient(@PathVariable("patient_id") Integer patient_id, Model model,
			HttpSession session) {
		if (!"1".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {
			model.addAttribute("patients", patientRepository.findAll());
			// passage de l'objet patient trouvé dans la vue avec le nom de l'objet géré par
			// le formulaire
			model.addAttribute("patient", patientRepository.findById(patient_id).get());

			return "adminPatients";
		}
	}
	
	@RequestMapping(value = { "/adminPatients/researchPatient" }, method = RequestMethod.POST)
	public String adminPatientsResearchPatient(@ModelAttribute("patient") Patient patient,
			@RequestBody String requestBody, @RequestParam(required = false, value = "rechercher") String slug,
			Model model, HttpSession session) {
		if (!"1".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {
			Iterable<Patient> patientsAll = patientRepository.findAll();
			Iterator<Patient> patients = patientsAll.iterator();
			while (patients.hasNext()) {
				Patient p = patients.next();
				if (containsIgnoreCase(p.getNumSecu(), slug) || containsIgnoreCase(p.getNom(), slug)
						|| containsIgnoreCase(p.getPrenom(), slug) || containsIgnoreCase(p.getTel(), slug)
						|| containsIgnoreCase(p.getMail(), slug) || containsIgnoreCase(p.getAdresse(), slug)
						|| containsIgnoreCase(p.getVille(), slug) || containsIgnoreCase(p.getCp(), slug)) {
				} else {
					patients.remove();
				}
			}
			model.addAttribute("patients", patientsAll);
			return "adminPatients";
		}
	}

	@RequestMapping(value = { "/adminPatients/setPatient" }, method = RequestMethod.POST)
	public String adminPatientsSetPatient(@RequestParam(required = false, value = "add") String addFlag,
			@RequestParam(required = false, value = "update") String updateFlag,
			@RequestParam(required = false, value = "delete") String deleteFlag, @Valid Patient patient,
			BindingResult results, RedirectAttributes redirectAttributes, Model model, HttpSession session) {
		if (!"1".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {
			model.addAttribute("patients", patientRepository.findAll());
			System.out.println("user id : " + patient.getUser().getId());
			if (results.hasErrors()) {
				return "adminPatients";
			} else {
				// AJOUTER
				if (addFlag != null) {
					// si l'identifiant et le mot de passe ne sont pas remplis
					if (patient.getUser().getIdentifiant().length() < 5
							&& patient.getUser().getPassword().length() < 5) {
						redirectAttributes.addFlashAttribute("erreur",
								String.format("Il faut renseigner un identifiant et un mot de passe"));
					} else {
						// si le num de sécu est unique
						if (patientRepository.findByNumSecu(patient.getNumSecu()) == null) {
							// si l'identifiant est unique
							if (userRepository.findByIdentifiant(patient.getUser().getIdentifiant()) == null) {
								// on sauvegarde
								System.out.println(patient.getUser());
								patient.getUser().setDroits(3);
								userRepository.save(patient.getUser());
								patientRepository.save(patient);
							} else {
								redirectAttributes.addFlashAttribute("erreur",
										String.format("Ce numéro de Sécu existe déjà"));
							}
						} else {
							redirectAttributes.addFlashAttribute("erreur",
									String.format("Cet identifiant existe déjà"));
						}
					}
					// MODIFIER
				} else if (updateFlag != null) {
					patient.getUser().setDroits(3);
					userRepository.save(patient.getUser());
					patientRepository.save(patient);
					// SUPPRIMER
				} else if (deleteFlag != null) {
					patientRepository.delete(patient);
					userRepository.delete(patient.getUser());
				}
				return "redirect:/adminPatients";
			}
		}
	}
}
