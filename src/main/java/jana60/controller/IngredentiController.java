package jana60.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jana60.model.Ingrediente;
import jana60.repository.IngredienteRepository;

@Controller
@RequestMapping("/ingredienti")
public class IngredentiController {
	
	@Autowired
	public IngredienteRepository repoIngrediente;
	
	@GetMapping
	public String listaIngredienti(Model model) {
		
		List<Ingrediente> ingredienti = (List<Ingrediente>) repoIngrediente.findAllByOrderByNome();
		
		model.addAttribute("ingredienti", ingredienti);
		model.addAttribute("nuovoIngrediente", new Ingrediente());
		
		return "ingredienti";
		
	}
	
	@PostMapping("/salva")
	public String salvaIngrediente(@Valid @ModelAttribute("nuovoIngrediente") Ingrediente formIngrediente, BindingResult br, Model model) {
		
		if (br.hasErrors()) {
		      
		      model.addAttribute("ingredienti", repoIngrediente.findAllByOrderByNome());
		      return "ingredienti";

		    } else {
		      
		      repoIngrediente.save(formIngrediente);
		      return "redirect:/ingredienti";
		    }
		
	}

}
