package rdv.gestion.controllers;

import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import rdv.gestion.model.Medecin;
import rdv.gestion.repository.MedecinRepository;
import rdv.gestion.repository.PatientRepository;
import rdv.gestion.repository.RvRepository;
import rdv.gestion.repository.UserRepository;

@Controller
/*
 * Attributs de session
 */
@SessionAttributes({ "droits" , "model_id"})
public class MedecinController {
	/*
	 * Titre de la page
	 */
	@ModelAttribute("titre")
	private String titre() {
		String titre = "Medecin";
		return titre;
	}
	/*
	 * Liste pour stocker les médecins
	 */
	private static Iterable<Medecin> medecinsAll;
	
	/*
	 * Méthode pour comparer deux chaînes quelque soit la casse
	 */
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
	
	/*
	 * Retourne la page de recherche d'un médecin et l'ensemble des médecins
	 */
	@RequestMapping(value = { "medecinCherche" }, method = RequestMethod.GET)
	public String medecinCherche(Model model, HttpSession session) {
		// si l'utilisateur n'a pas les droits de médecin il est renvoyé à la page d'accueil
		if (!"2".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {	
			/* on met l'ensemble des médecins dans l'objet iterable */
			medecinsAll = medecinRepository.findAll();
			model.addAttribute("medecins", medecinsAll);
			return "medecinCherche";
		}
	}
	/*
	 * Retourne la liste des patients avec lesquels le médecin a rendez-vous
	 */
	@RequestMapping(value = { "medecinListeRv" }, method = RequestMethod.GET)
	public String medecin(Model model, HttpSession session) {
		if (!"2".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {	
			model.addAttribute("patients", patientRepository.findByRvMedecin(Integer.parseInt(session.getAttribute("model_id").toString())));
			return "medecinListeRv";
		}
	}
	/*
	 * Retourne le détail du patient cliqué, la liste des rendez-vous avec ce patient,
	 * et la liste des patients avec lesquels le médecin a rendez-vous.
	 */
	@RequestMapping(value = { "medecinListeRv/getPatient/{patient_id}"}, method = RequestMethod.GET)
	public String medecinGetPatientRv(@PathVariable("patient_id") Integer patient_id, Model model,
			HttpSession session) {
		if (!"2".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {		
			model.addAttribute("patients", patientRepository.findByRvMedecin(Integer.parseInt(session.getAttribute("model_id").toString())));
			model.addAttribute("patientRv", rvRepository.findByPatientMedecin(patient_id, Integer.parseInt(session.getAttribute("model_id").toString())));
			model.addAttribute("patient", patientRepository.findById(patient_id).get());
			return "medecinListeRv";
		}
	}
	/*
	 * retourne la liste des médecins qui matchent avec la chaîne de caratère envoyée par
	 * l'utilisateur.
	 */
	@RequestMapping(value = { "/medecinCherche" }, method = RequestMethod.POST)
	public String medecinCherche(@ModelAttribute("medecin") Medecin medecin,
			@RequestBody String requestBody, @RequestParam(required = false, value = "rechercher") String slug,
			Model model, HttpSession session) {
		if (!"2".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {
			/* on met l'ensemble des médecins dans l'objet iterable */
			medecinsAll = medecinRepository.findAll();
			/* l'iterator va permettre se supprimer de l'objet iterable
			 * les médecins qui ne matchent pas.
			 */
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
			/*
			 * on passe au modèle l'objet iterable, le seul type d'objet que thymeleaf
			 * peut parcourir.
			 */
			model.addAttribute("medecins", medecinsAll);
			return "medecinCherche";
		}
	}
	/*
	 * Retourne le détail d'un médecin cliqué, et la liste des médecins trouvés 
	 * dans laquelle celui ci est présent.
	 */
	@RequestMapping(value = {"medecinCherche/getMedecin/{medecin_id}"}, method = RequestMethod.GET)
	public String medecinChercheGetMedecin(@PathVariable("medecin_id") Integer medecin_id, Model model,
			HttpSession session) {
		if (!"2".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {
			model.addAttribute("medecinInfo",medecinRepository.findById(medecin_id).get());
			model.addAttribute("medecins", medecinsAll);
			return "medecinCherche";
		}
	}
}
