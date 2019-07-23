package de.unserewebseite.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import de.unserewebseite.form.GaesteForm;
import de.unserewebseite.model.Breit;
import de.unserewebseite.model.Gaeste;
import de.unserewebseite.model.Lang;
import de.unserewebseite.model.TempJetzt;
import de.unserewebseite.model.TempMax;
import de.unserewebseite.model.TempMin;

@Controller
public class MyController {

	private static List<Gaeste> gaeste = new ArrayList<Gaeste>();

	@Value("${welcome.message}")
	private String message;

	@Value("${error.message}")
	private String errorMessage;

	@Value("${remove.message}")
	private String removeMessage;

	@RequestMapping(value = { "/", "/HTML/index" }, method = RequestMethod.GET)
	public String indexSeite(Model model) {
		model.addAttribute("message", message);
		return "HTML/index";
	}

	@RequestMapping(value = { "/HTML/gaesteliste" }, method = RequestMethod.GET)
	public String listeGaeste(Model model) {
		model.addAttribute("gaeste", gaeste);
		return "HTML/gaesteliste";
	}

	@RequestMapping(value = { "/HTML/neuerEintrag" }, method = RequestMethod.GET)
	public String zeigeNeuerEintragSeite(Model model) {
		GaesteForm gaesteForm = new GaesteForm();
		model.addAttribute("GaesteForm", gaesteForm);
		return "HTML/neuerEintrag";
	}

	// Eintrag hinzufügen
	@RequestMapping(value = { "/HTML/neuerEintrag" }, method = RequestMethod.POST)
	public String saveGast(Model model, @ModelAttribute("GaesteForm") GaesteForm gaesteForm) {

		String email = gaesteForm.getEmail();
		String vorname = gaesteForm.getVorname();
		String nachname = gaesteForm.getNachname();

		// Syntaxüberprüfung
		if (email.contains("@") && email.contains(".com")
				|| email.contains(".de") && email != null && email.length() > 0 && vorname != null
						&& vorname.length() > 0 && nachname != null && nachname.length() > 0) {
			Gaeste newGast = new Gaeste(vorname, nachname, email);
			gaeste.add(newGast);
			return "redirect:/HTML/gaesteliste";
		}
		model.addAttribute("errorMessage", errorMessage);
		return "HTML/neuerEintrag";
	}

	// Eintrag loeschen
	@RequestMapping(value = { "/HTML/loescheEintrag" }, method = RequestMethod.GET)
	public String zeigeLoescheEintragSeite(Model model) {
		GaesteForm gaesteForm = new GaesteForm();
		model.addAttribute("GaesteForm", gaesteForm);
		return "HTML/loescheEintrag";
	}

	//Eintrag Löschen
	@RequestMapping(value = { "/HTML/loescheEintrag" }, method = RequestMethod.DELETE)
	public String loescheEintrag(Model model, @ModelAttribute("GaesteForm") GaesteForm gaesteForm) {
		String email = gaesteForm.getEmail();
		if (email != null && email.length() > 0) {
			Iterator<Gaeste> iter = gaeste.iterator();
			while (iter.hasNext()) {
				Gaeste gast = iter.next();
				String emailMail = gast.getEmail();
				if (emailMail.equals(email)) {
					int idx = gaeste.indexOf(gast);
					gaeste.remove(idx);
					return "redirect:/HTML/gaesteliste";
				}
			}
		}
		model.addAttribute("removeMessage", removeMessage);
		return "HTML/loescheEintrag";
	}

	// Geo- und Wetterinformationen von Frankfurt am Main
	@RequestMapping(value = { "/HTML/api" }, method = RequestMethod.GET)
	public String zeigeInfo(Model model) {
		String url = "https://api.openweathermap.org/data/2.5/weather?q=Frankfurt&appid=bb7758c6fd034bcb463b897f2c003346&units=metric";
		RestTemplate restTemplate = new RestTemplate();
		Lang lang = restTemplate.getForObject(url, Lang.class);
		Breit breit = restTemplate.getForObject(url, Breit.class);

		TempJetzt jetzt = restTemplate.getForObject(url, TempJetzt.class);
		TempMin min = restTemplate.getForObject(url, TempMin.class);
		TempMax max = restTemplate.getForObject(url, TempMax.class);

		double b_grad = lang.getCoord().getLat();
		double l_grad = breit.getCoord().getLon();

		double jetzt_temp = jetzt.getMain().getTemp();
		double min_temp = min.getMain().getTemp_min();
		double max_temp = max.getMain().getTemp_max();

		model.addAttribute("lat", b_grad);
		model.addAttribute("lon", l_grad);
		model.addAttribute("temp", jetzt_temp);
		model.addAttribute("temp_min", min_temp);
		model.addAttribute("temp_max", max_temp);

		return "HTML/api";
	}

	// Excel
	@RequestMapping(value = { "/HTML/gaesteliste" }, method = RequestMethod.POST)
	public String excelGaeste(Model model) throws IOException {
		model.addAttribute("gaeste", gaeste);

		String excel = "";
		FileWriter fw = new FileWriter("C:\\Users\\Volkan\\Desktop\\UserListe.csv");

		for (Gaeste gaeste2 : gaeste) {
			excel = String.format("%s;%s;%s", gaeste2.getEmail(), gaeste2.getVorname(), gaeste2.getNachname());
			fw.write(excel + "\n");
			fw.flush();
		}
		fw.close();

		return "HTML/gaesteliste";
	}
	
	// Eintrag ändern
//		@RequestMapping(value = { "/HTML/aendereEintrag" }, method = RequestMethod.GET)
//		public String zeigeAendereEintragSeite(Model model) {
//			GaesteForm gaesteForm = new GaesteForm();
//			model.addAttribute("GaesteForm", gaesteForm);
//			return "HTML/aendereEintrag";
//		}
//		
//		@RequestMapping(value = { "/HTML/aendereEintrag" }, method = RequestMethod.DELETE)
//		public String aendereEintrag(Model model, @ModelAttribute("GaesteForm") GaesteForm gaesteForm) {
//			
//			String email = gaesteForm.getEmail();
//			
//			for (Gaeste gaeste2 : gaeste) {
//				if (email != null && email.length() > 0 && email.equals(gaeste2.getEmail())) {
//					
//					return "redirect:/listStudents";
//				}
//			}
//			
//			model.addAttribute("removeMessage", removeMessage);
//			return "HTML/loescheEintrag";
//		}
}
