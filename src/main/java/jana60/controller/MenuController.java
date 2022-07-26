package jana60.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jana60.model.Pizza;
import jana60.repository.IngredienteRepository;
import jana60.repository.PizzaRepository;

@Controller
@RequestMapping("/menu")
public class MenuController {

	@Autowired
	public PizzaRepository repoPizza;
	
	@Autowired
	public IngredienteRepository repoIngrediente;
	
	@GetMapping
	public String menu(Model model) {
		
		List<Pizza> pizze = (List<Pizza>) repoPizza.findAllByOrderByPrezzoAsc();
		model.addAttribute("pizze", pizze);
		
		return "menu";
		
	}
	
	@GetMapping("/dettagli/{pizzaId}")
	public String currentPizza(@PathVariable(name = "pizzaId") Integer pizzaPrimaryKey, Model model) {
		
		Pizza currentPizza = repoPizza.findById(pizzaPrimaryKey).get();
		model.addAttribute("currentPizza", currentPizza);
			
		return "currentPizza";
		
	}
		
	@GetMapping("/aggiungi")
	public String formPizza(Model model) {
		
		model.addAttribute("pizza", new Pizza());
		model.addAttribute("listaIngredienti", repoIngrediente.findAllByOrderByNome());
		
		return "aggiungi";
		
	}
	
	@GetMapping("/modifica/{pizzaId}")
	public String modifica(@PathVariable(name = "pizzaId") Integer pizzaId, Model model) {
		
		Optional<Pizza> selezionaId = repoPizza.findById(pizzaId);
		
		if(selezionaId.isPresent()) {
			
			model.addAttribute("pizza", selezionaId.get());
			model.addAttribute("listaIngredienti", repoIngrediente.findAllByOrderByNome());
			return "modifica";
			
		} else {
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La pizza cercata non esiste");
			
		}
		
	}
	
	@PostMapping("/salva")
	public String salva(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult br ,Model model) {
		
		boolean hasErrors = br.hasErrors();
		boolean validaNome = true;
		boolean nuovaPizza = true;
				
		if(formPizza.getId() != null) {
			
			nuovaPizza = false;
			
			Pizza pizzaPrecedenteAllaModifica = repoPizza.findById(formPizza.getId()).get();
			
			if(pizzaPrecedenteAllaModifica.getNome().equalsIgnoreCase(formPizza.getNome())) {
				
				validaNome = false;
				
			}
			
		}
		
		if(validaNome && repoPizza.countByNome(formPizza.getNome()) > 0) {
			
			br.addError(new FieldError("pizza", "nome", "Il nome deve essere unico"));
			
			hasErrors = true;
			
		}
		
		if(hasErrors)
			if(nuovaPizza) {
				
				model.addAttribute("listaIngredienti", repoIngrediente.findAllByOrderByNome());
				return "/aggiungi";
				
			}
				
			else {
				
				model.addAttribute("listaIngredienti", repoIngrediente.findAllByOrderByNome());
				return "/modifica";
				
			}
				
		else {
			
			try {
				
				repoPizza.save(formPizza);
				
			} catch(Exception e) {
				
				model.addAttribute("errorMessage", "Impossibile salvare le modifiche");
				model.addAttribute("listaIngredienti", repoIngrediente.findAllByOrderByNome());
				
			}
			
			return "redirect:/menu";
			
		}
		
	}
	
	@GetMapping("/elimina/{pizzaId}")
	public String elimina(@Valid @PathVariable("pizzaId") Integer pizzaId, RedirectAttributes ra) {
		
		Optional<Pizza> selezionaId = repoPizza.findById(pizzaId);
		
		if(selezionaId.isPresent()) {
			
			repoPizza.deleteById(pizzaId);
			ra.addFlashAttribute("successMessage", "La pizza " + selezionaId.get().getNome() + " è stata eliminata.");
			
			return "redirect:/menu";
			
		} else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La pizza che stai provando ad eliminare non esiste");
		
	}
	
	@GetMapping("/search")
	public String search(@RequestParam(name = "queryNome") String queryNome, Model model) {
		/*
		if(queryNome != null && queryNome.isEmpty())
			queryNome = null;
		*/
		
		List<Pizza> pizze = repoPizza.findByNomeContainingIgnoreCase(queryNome);
		model.addAttribute("pizze", pizze);
		
		return "/menu";
		
	}
	
}