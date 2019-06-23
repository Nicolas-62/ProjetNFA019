package rdv.gestion.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
/*
 * Supprime la session et renvoie à l'accueil
 */
@RequestMapping("/logout")
public class LogoutController {
	  @RequestMapping(method=RequestMethod.GET)
	  public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
	    session.invalidate();
	    return "redirect:/pageLogin";
	  }

}
