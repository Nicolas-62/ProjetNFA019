package rdv.gestion.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import rdv.gestion.model.Rv;
import rdv.gestion.repository.CreneauxRepository;
import rdv.gestion.repository.MedecinRepository;
import rdv.gestion.repository.PatientRepository;
import rdv.gestion.repository.RvRepository;

@Controller
@SessionAttributes({ "droits", "id", "patient_id" })
public class PatientListeRvController {

	@ModelAttribute("titre")
	private String titre() {
		String titre = "Patient";
		return titre;
	}
	@Autowired // sert à creer une instance voir pattern singleton
	MedecinRepository medecinRepository;
	@Autowired
	PatientRepository patientRepository;
	@Autowired
	RvRepository rvRepository;
	@Autowired
	CreneauxRepository creneauxRepository;

	@RequestMapping(value = { "/patientListeRv" }, method = RequestMethod.GET)
	public String patientListeRv(Model model, HttpSession session) {
		if (!"3".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {
			// on récupère la liste des rendez vous du patient
			model.addAttribute("rendezVous",rvRepository.findByPatient_id(Integer.parseInt(session.getAttribute("patient_id").toString())));
			return "patientListeRv";
		}
	}

	@RequestMapping(value = { "/patientListeRv/getRv/{rv_id}" }, method = RequestMethod.GET)
	public String patientListeRvGetRv(@PathVariable("rv_id") Integer rv_id, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		if (!"3".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {
			// on récupère la liste des rendez vous du patient
			model.addAttribute("rendezVous",rvRepository.findByPatient_id(Integer.parseInt(session.getAttribute("patient_id").toString())));			
			if (!rvRepository.findById(rv_id).isPresent()) {
				redirectAttributes.addFlashAttribute("message",
						String.format("Ce rendez vous n'existe pas"));			
				return "redirect:/patientlisteRv";
			} else {
				Rv rv = rvRepository.findById(rv_id).get();
				model.addAttribute("medecinInfo", rv.getCreneaux().getMedecin());
				model.addAttribute("rv", rv);
				return "/patientListeRv";
			}
		}
	}

	@RequestMapping(value = { "/patientListeRv/setRv/{rv_id}" }, method = RequestMethod.POST)
	public String patientListeRvSetRv(@PathVariable("rv_id") Integer rv_id, Model model, HttpSession session, 
			@RequestParam(value = "date") String date,
			@RequestParam(value = "creneaux") String creneau_id,
			@RequestParam(required = false, value = "update") String updateFlag,
			@RequestParam(required = false, value = "delete") String deleteFlag,
			RedirectAttributes redirectAttributes){
		if (!"3".equals(session.getAttribute("droits").toString())) {
			return "redirect:/pageLogin";
		} else {
			Rv rv = new Rv();
			rv.setId(rv_id);
			// MODIFIER RV
			if(updateFlag != null) {
				Date stringToDate;
				try {
					stringToDate=new SimpleDateFormat("yyyy-MM-dd").parse(date);
				}catch(Exception e) {
					redirectAttributes.addFlashAttribute("message",
							String.format("Le format de la date est incorrect."));
					return "redirect:/patientListeRv";
				}
				rv.setDate(stringToDate);
				rv.setCreneaux(creneauxRepository.findById(Integer.parseInt(creneau_id)).get());
				session.getAttribute("patient_id");
				rv.setPatient(patientRepository.findById(Integer.parseInt(session.getAttribute("patient_id").toString())).get());
				try {
					rvRepository.save(rv);
				}catch(RuntimeException e) {
					redirectAttributes.addFlashAttribute("message",
							String.format("Ce creneau est déjà réservé"));
					return "redirect:/patientListetRv";				
				}
				redirectAttributes.addFlashAttribute("message",
						String.format("Le rendez-vous a été modifié !"));
			// SUPPRIMER RV
			}else if(deleteFlag != null) {
				redirectAttributes.addFlashAttribute("message",
						String.format("Le rendez-vous a été supprimé !"));
				rvRepository.delete(rv);
			}
			return "redirect:/patientListeRv";
		}
	}
}