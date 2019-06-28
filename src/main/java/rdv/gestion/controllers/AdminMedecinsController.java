package rdv.gestion.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import rdv.gestion.repository.MedecinRepository;
import rdv.gestion.repository.UserRepository;

@Controller
@SessionAttributes({ "droits", "id" })
public class AdminMedecinsController {

	@Autowired // sert à creer une instance voir pattern singleton
	MedecinRepository medecinRepository;
	@Autowired
	UserRepository userRepository;

	@ModelAttribute("titre")
	private String titre() {
		String titre = "Medecins";
		return titre;
	}

	@ModelAttribute("specialites")
	private List<String> specialites() {
		List<String> specialites = new ArrayList<String>();
		specialites.add("Sexologue");
		specialites.add("Chirurgien");
		specialites.add("Oncologue");
		specialites.add("Gastro-enterologue");
		specialites.add("Angiologue");
		specialites.add("Cardiologue");
		specialites.add("Dentiste");
		specialites.add("Neurologue");
		specialites.add("Chirurgien-Plasticien");
		specialites.add("Ophtalmologiste");
		return specialites;
	}

	private static boolean containsIgnoreCase(String str, String subString) {
		return str.toLowerCase().contains(subString.toLowerCase());
	}
	/*
	 * Retourne la liste des médecins et le formulaire d'ajout/suppression d'un médecin.
	 */	
	@RequestMapping(value = { "/adminMedecins" }, method = RequestMethod.GET)
	public String adminMedecins(@ModelAttribute("medecin") Medecin medecin, HttpSession session, Model model) {		
		if (!"1".equals(session.getAttribute("droits").toString())) {
			
			return "redirect:/pageLogin";
		} else {
			model.addAttribute("medecins", medecinRepository.findAll());
			return "adminMedecins";
		}
	}
	/*
	 * retourne la liste des médecins, le détail du médecin cliqué,
	 * et le formulaire d'ajout/suppression d'un médecin.
	 */
	@RequestMapping(value = { "/adminMedecins/getMedecin/{medecin_id}" }, method = RequestMethod.GET)
	public String adminMedecinsGetMedecin(@PathVariable("medecin_id") Integer medecin_id, Model model,
			HttpSession session) {
		if (!"1".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {
			model.addAttribute("medecins", medecinRepository.findAll());
			// passage de l'objet patient trouvé dans la vue avec le nom de l'objet géré par
			// le formulaire
			model.addAttribute("medecin", medecinRepository.findById(medecin_id).get());
			return "adminMedecins";
		}
	}
	/*
	 * Retourne la liste des médecins qui matchent avec la chaine de caractère envoyée
	 * par l'utilisateur.
	 */
	@RequestMapping(value = { "/adminMedecins/researchMedecin" }, method = RequestMethod.POST)
	public String adminMedecinsResearchMedecin(@ModelAttribute("medecin") Medecin medecin,
			@RequestBody String requestBody, @RequestParam(required = false, value = "rechercher") String slug,
			Model model, HttpSession session) {
		if (!"1".equals(session.getAttribute("droits").toString())) {
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
			model.addAttribute("medecins", medecinsAll);
			return "adminMedecins";
		}
	}
	/*
	 * Sauvegarde, modifie un médecin.
	 */
	@RequestMapping(value = { "/adminMedecins/setMedecin" }, method = RequestMethod.POST)
	public String adminMedecinsSetMedecin(@RequestParam(required = false, value = "add") String addFlag,
			@RequestParam(required = false, value = "update") String updateFlag,
			@RequestParam(required = false, value = "delete") String deleteFlag, @Valid Medecin medecin,
			BindingResult results, RedirectAttributes redirectAttributes, Model model, HttpSession session) {
		if (!"1".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {
			model.addAttribute("medecins", medecinRepository.findAll());
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
						// on essaye de sauvegarder, l'identifiant doit être unique
						try {
							medecin.getUser().setDroits(3);
							userRepository.save(medecin.getUser());
							medecinRepository.save(medecin);						
						}catch(RuntimeException e) {
							redirectAttributes.addFlashAttribute("erreur",
									String.format("Cet identifiant existe déjà"));							
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
}
