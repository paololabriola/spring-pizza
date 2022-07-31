package jana60.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import jana60.model.FormImmagine;
import jana60.model.Immagine;
import jana60.service.ServiceImmagine;

@Controller
@RequestMapping("/immagine")
public class ImmagineController {

	@Autowired
	ServiceImmagine service;
	
	// Mostra la lista delle Immagini collegate ad una pizza e la form per aggiungerne una.
	@GetMapping("/{pizzaId}")
	public String ImmaginiPizza(@PathVariable(name = "pizzaId") Integer pizzaPrimaryKey, Model model) {
		
		List<Immagine> immagini = service.getImmagineByPizzaId(pizzaPrimaryKey);
		
		FormImmagine formImmagine = service.creaFormImmagine(pizzaPrimaryKey);
		
		model.addAttribute("immagini", immagini);
		model.addAttribute("formImmagine", formImmagine);
		
		return "immagini";
		
	}
	
	// Per salvare l'immagine su db.
	@PostMapping("/salva")
	public String salvaImmagine(@ModelAttribute("formImmagine") FormImmagine formImmagine) {
		
		try {
			
			Immagine immagineSalvata = service.creaImmagine(formImmagine);
			return "redirect:/immagine/" + immagineSalvata.getPizza().getId();
			
		} catch(IOException e) {
			
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Impossibile salvare l'immagine");
			
		}
		
	}
	
	@RequestMapping(value = "/{immagineId}/content", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getContentImmagine(@PathVariable(name = "immagineId")Integer immagineId) {
		
		byte[] content = service.prendiImmagini(immagineId);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		
		return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
		
	}
	
}
