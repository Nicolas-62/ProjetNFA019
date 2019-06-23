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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import rdv.gestion.model.User;
import rdv.gestion.repository.MedecinRepository;
import rdv.gestion.repository.PatientRepository;
import rdv.gestion.repository.UserRepository;

@Controller
// model_id = id de l'entité concernée, patient ou medecin
@SessionAttributes({ "droits" , "id", "model_id"})
public class LoginController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PatientRepository patientRepository;
	@Autowired
	MedecinRepository medecinRepository;

	// page d'accueil...
	@RequestMapping(value = { "/" , "/pageLogin"}, method = RequestMethod.GET)
	public String pagelogin(@ModelAttribute("user") User user, Model model) {
		model.addAttribute("titre", String.format("login"));
		return "pageLogin";
	}
	/*
	 * Redirige vers l'espace utilisateur en fonction de ses droits,
	 * vérifie son login et son mot de passe.
	 */
	@RequestMapping(value = { "/pageLogin" }, method = RequestMethod.POST)
	public String pagelogin(@Valid User user, BindingResult results, 
			Model model, HttpSession session,
			@RequestParam(required = false, value = "deconnect") String deconnect) {
		model.addAttribute("titre", String.format("login"));
		//destruction de la session précedente
		// redirection vers le LogoutController
		if(deconnect != null) {
			return "redirect:/logout";			
		}
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
				User userFound = userRepository.findUser(user.getIdentifiant(), user.getPassword());
				// si aucun utilisateur a été trouvé et donc pas de droits trouvés
				if (userFound == null) {
					model.addAttribute("erreur", String.format("mot de passe incorect"));
					return "pageLogin";
					// si les droits ne correspondent pas avec ceux selectionnés :
				} else if (userFound.getDroits() != user.getDroits()) {
					model.addAttribute("erreur", String.format("Vous ne pouvez vous connecter avec ces droits"));
					return "pageLogin";
					// sinon on redirige vers la page autorisée
				} else {
					session.setAttribute("droits", userFound.getDroits());
					session.setAttribute("id", userFound.getId());				
					if (userFound.getDroits() == 1) {
						return "redirect:/adminPatients";
					} else if (userFound.getDroits() == 2) {	
						// on récupère l'id du medecin à partir de l'id user stocké en session et on le stocke en session
						session.setAttribute("model_id", medecinRepository.findByUser_id(Integer.parseInt(session.getAttribute("id").toString())).getId());						
						return "redirect:/medecinListeRv";
						// droits=3 : droits d'accès patient
					} else {
						// on récupère l'id du patient à partir de l'id user stocké en session et on le stocke en session
						session.setAttribute("model_id", patientRepository.findByUser_id(Integer.parseInt(session.getAttribute("id").toString())).getId());						
						return "redirect:/patientListeRv";
					}
				}
			}
		}
	}	
}

	