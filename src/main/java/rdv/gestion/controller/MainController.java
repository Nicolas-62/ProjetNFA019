package rdv.gestion.controller;

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

import rdv.gestion.model.Medecin;
import rdv.gestion.model.Patient;
import rdv.gestion.model.User;
import rdv.gestion.repository.MedecinRepository;
import rdv.gestion.repository.PatientRepository;
import rdv.gestion.repository.UserRepository;

@Controller
@SessionAttributes({ "droits" })
public class MainController {

	public static boolean containsIgnoreCase(String str, String subString) {
		return str.toLowerCase().contains(subString.toLowerCase());
	}

	@Autowired // sert à creer une instance voir pattern singleton
	MedecinRepository medecinRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PatientRepository patientRepository;

	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("msg", String.format("Nom du medecin : nico"));
		return "index";
	}

	@RequestMapping(value = { "medecin" }, method = RequestMethod.GET)
	public String medecin(Model model) {
		return "medecin";
	}

	@RequestMapping(value = { "patientRv" }, method = RequestMethod.GET)
	public String patientRv( Model model, HttpSession session) {	
		if (!session.getAttribute("droits").equals(2)) {
			return "redirect:/pageLogin";
		} else {		
			model.addAttribute("titre", String.format("Patient"));
			model.addAttribute("medecins", medecinRepository.findAll());
			return "patientRv";
		}
	}
	@RequestMapping(value = { "patientRv/getMedecin/{medecin_id}" }, method = RequestMethod.GET)
	public String patientRvGetMedecin(@PathVariable("patient_id") Integer medecin_id, Model model, HttpSession session) {	
		if (!session.getAttribute("droits").equals(2)) {
			return "redirect:/pageLogin";
		} else {			
			model.addAttribute("titre", String.format("Patient"));
			model.addAttribute("medecins", medecinRepository.findAll());
			return "patientRv";
		}
	}	

	@RequestMapping(value = { "/adminMedecins" }, method = RequestMethod.GET)
	public String adminMedecins(@ModelAttribute("medecin") Medecin medecin, HttpSession session, Model model) {
		if (!session.getAttribute("droits").equals(1)) {
			return "redirect:/pageLogin";
		} else {
			model.addAttribute("titre", String.format("Administration"));
			model.addAttribute("medecins", medecinRepository.findAll());
			return "adminMedecins";
		}
	}

	@RequestMapping(value = { "/adminPatients" }, method = RequestMethod.GET)
	public String adminPatients(@ModelAttribute("patient") Patient patient, Model model, HttpSession session) {
		if (!session.getAttribute("droits").equals(1)) {
			return "redirect:/pageLogin";
		} else {
			model.addAttribute("titre", String.format("Administration"));
			model.addAttribute("patients", patientRepository.findAll());
			return "adminPatients";
		}
	}

	@RequestMapping(value = { "/adminMedecins/getMedecin/{medecin_id}" }, method = RequestMethod.GET)
	public String adminMedecinsGetMedecin(@PathVariable("patient_id") Integer medecin_id, Model model,
			HttpSession session) {
		if (!session.getAttribute("droits").equals(1)) {
			return "redirect:/pageLogin";
		} else {
			model.addAttribute("titre", String.format("Administration"));
			model.addAttribute("medecins", medecinRepository.findAll());
			// passage de l'objet patient trouvé dans la vue avec le nom de l'objet géré par
			// le formulaire
			model.addAttribute("medecin", medecinRepository.findById(medecin_id).get());

			return "adminMedecins";
		}
	}

	@RequestMapping(value = { "/adminPatients/getPatient/{patient_id}" }, method = RequestMethod.GET)
	public String adminPatientsGetPatient(@PathVariable("patient_id") Integer patient_id, Model model,
			HttpSession session) {
		if (!session.getAttribute("droits").equals(1)) {
			return "redirect:/pageLogin";
		} else {
			model.addAttribute("titre", String.format("Administration"));
			model.addAttribute("patients", patientRepository.findAll());
			// passage de l'objet patient trouvé dans la vue avec le nom de l'objet géré par
			// le formulaire
			model.addAttribute("patient", patientRepository.findById(patient_id).get());

			return "adminPatients";
		}
	}

	@RequestMapping(value = { "/adminMedecins/researchMedecin" }, method = RequestMethod.POST)
	public String adminMedecinsResearchMedecin(@ModelAttribute("medecin") Medecin medecin,
			@RequestBody String requestBody, @RequestParam(required = false, value = "rechercher") String slug,
			Model model, HttpSession session) {
		if (!session.getAttribute("droits").equals(1)) {
			return "redirect:/pageLogin";
		} else {
			Iterable<Medecin> medecinsAll = medecinRepository.findAll();
			Iterator<Medecin> medecins = medecinsAll.iterator();
			while (medecins.hasNext()) {
				Medecin p = medecins.next();
				if (containsIgnoreCase(p.getNom(), slug) || containsIgnoreCase(p.getPrenom(), slug)
						|| containsIgnoreCase(p.getSpecialite(), slug) || containsIgnoreCase(p.getTel(), slug)
						|| containsIgnoreCase(p.getMail(), slug) || containsIgnoreCase(p.getAdresse(), slug)
						|| containsIgnoreCase(p.getVille(), slug) || containsIgnoreCase(p.getCp(), slug)) {
				} else {
					medecins.remove();
				}
			}
			model.addAttribute("titre", String.format("Administration"));
			model.addAttribute("medecins", medecinsAll);
			return "adminMedecins";
		}
	}

	@RequestMapping(value = { "/adminPatients/researchPatient" }, method = RequestMethod.POST)
	public String adminPatientsResearchPatient(@ModelAttribute("patient") Patient patient,
			@RequestBody String requestBody, @RequestParam(required = false, value = "rechercher") String slug,
			Model model, HttpSession session) {
		if (!session.getAttribute("droits").equals(1)) {
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
			model.addAttribute("titre", String.format("Administration"));
			model.addAttribute("patients", patientsAll);
			return "adminPatients";
		}
	}

	@RequestMapping(value = { "/adminMedecins/setMedecin" }, method = RequestMethod.POST)
	public String adminMedecinsSetMedecin(@RequestParam(required = false, value = "add") String addFlag,
			@RequestParam(required = false, value = "update") String updateFlag,
			@RequestParam(required = false, value = "delete") String deleteFlag, @Valid Medecin medecin,
			BindingResult results, RedirectAttributes redirectAttributes, Model model, HttpSession session) {
		if (!session.getAttribute("droits").equals(1)) {
			return "redirect:/pageLogin";
		} else {
			model.addAttribute("titre", String.format("Administration"));
			model.addAttribute("medecins", medecinRepository.findAll());
			System.out.println("user id : " + medecin.getUser().getId());
			if (results.hasErrors()) {
				return "adminMedecins";
			} else {
				// AJOUTER
				if (addFlag != null) {
					// si l'identifiant et le mot de passe ne sont pas remplis
					if (medecin.getUser().getIdentifiant().length() < 5
							&& medecin.getUser().getPassword().length() < 5) {
						redirectAttributes.addFlashAttribute("erreur",
								String.format("Il faut renseigner un identifiant et un mot de passe"));
					} else {
						// si l'identifiant est unique
						if (userRepository.findByIdentifiant(medecin.getUser().getIdentifiant()) == null) {
							// on sauvegarde
							System.out.println(medecin.getUser());
							medecin.getUser().setDroits(3);
							userRepository.save(medecin.getUser());
							medecinRepository.save(medecin);
						} else {
							redirectAttributes.addFlashAttribute("erreur",
									String.format("Ce numéro de Sécu existe déjà"));
						}
					}
					// MODIFIER
				} else if (updateFlag != null) {
					medecin.getUser().setDroits(3);
					userRepository.save(medecin.getUser());
					medecinRepository.save(medecin);
					// SUPPRIMER
				} else if (deleteFlag != null) {
					medecinRepository.delete(medecin);
					userRepository.delete(medecin.getUser());
				}
				return "redirect:/adminMedecins";
			}
		}
	}

	@RequestMapping(value = { "/adminPatients/setPatient" }, method = RequestMethod.POST)
	public String adminPatientsSetPatient(@RequestParam(required = false, value = "add") String addFlag,
			@RequestParam(required = false, value = "update") String updateFlag,
			@RequestParam(required = false, value = "delete") String deleteFlag, @Valid Patient patient,
			BindingResult results, RedirectAttributes redirectAttributes, Model model, HttpSession session) {
		if (!session.getAttribute("droits").equals(1)) {
			return "redirect:/pageLogin";
		} else {
			model.addAttribute("titre", String.format("Administration"));
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

	@RequestMapping(value = { "/pageLogin" }, method = RequestMethod.GET)
	public String pagelogin(@ModelAttribute("user") User user, Model model) {
		model.addAttribute("titre", String.format("login"));
		return "pageLogin";
	}

	@RequestMapping(value = { "/pageLogin" }, method = RequestMethod.POST)
	public String pagelogin(@Valid User user, BindingResult results, Model model, HttpSession session) {
		model.addAttribute("titre", String.format("login"));
		if (results.hasErrors()) {
			return "pageLogin";
		} else {
			// si aucun utilisateur avec cet identifiant n'a été trouvé :
			if (userRepository.findByIdentifiant(user.getIdentifiant()) == null) {
				model.addAttribute("titre", String.format("login"));
				model.addAttribute("erreur", String.format("Identifiant incorect"));
				return "pageLogin";
			} else {
				/*
				 * sinon on recherche les droits de l'utilisateur en fonction de l'identifiant
				 * et du password donné
				 */
				Integer droits = userRepository.getDroitsFromUser(user.getIdentifiant(), user.getPassword());
				// si aucun utilisateur a été trouvé et donc pas de droits trouvés
				if (droits == null) {
					model.addAttribute("erreur", String.format("mot de passe incorect"));
					return "pageLogin";
					// si les droits ne correspondent pas avec ceux selectionnés :
				} else if (droits != user.getDroits()) {
					model.addAttribute("erreur", String.format("Vous ne pouvez vous connecter avec ces droits"));
					return "pageLogin";
					// sinon on redirige vers la page autorisée
				} else {
					System.out.println("droits : " + droits);
					if (droits == 1) {
						session.setAttribute("droits", 1);
						return "redirect:/adminPatients";
					} else if (droits == 2) {
						session.setAttribute("droits", 2);
						return "redirect:/medecin";
						// droits=3 : droits d'accès patient
					} else {
						session.setAttribute("droits", 3);
						return "redirect:/patientRv";
					}
				}
			}
		}
	}
}
