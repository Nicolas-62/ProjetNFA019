package rdv.gestion.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import rdv.gestion.model.User;
import rdv.gestion.repository.UserRepository;

@Controller
@SessionAttributes({ "droits" , "id"})
public class LoginController {

	public static boolean containsIgnoreCase(String str, String subString) {
		return str.toLowerCase().contains(subString.toLowerCase());
	}
	@Autowired
	UserRepository userRepository;


	// page d'accueil...
	@RequestMapping(value = { "/" , "/pageLogin"}, method = RequestMethod.GET)
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
					session.setAttribute("droits", droits);
					session.setAttribute("id", user.getId());
					
					if (droits == 1) {
						return "redirect:/adminPatients";
					} else if (droits == 2) {						
						return "redirect:/medecin";
						// droits=3 : droits d'accès patient
					} else {
						return "redirect:/patientRv";
					}
				}
			}
		}
	}	
}

	