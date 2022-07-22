package jana60.controller;

import java.util.List;

import javax.swing.text.AbstractDocument.BranchElement;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jana60.model.Pizza;
import jana60.repository.PizzaRepository;
import net.bytebuddy.implementation.bind.MethodDelegationBinder.BindingResolver;

@Controller
@RequestMapping("/menu")
public class MenuController {

	@Autowired
	public PizzaRepository repoPizza;
	
	@GetMapping
	public String menu(Model model) {
		
		List<Pizza> pizze = (List<Pizza>) repoPizza.findAllByOrderByPrezzoAsc();
		model.addAttribute("pizze", pizze);
		
		return "menu";
		
	}
	
	@GetMapping("/aggiungi")
	public String formPizza(Model model) {
		
		model.addAttribute("pizza", new Pizza());
		
		return "aggiungi";
		
	}
	
	@PostMapping("/salva")
	public String salva(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult br) {
		
		if(br.hasErrors())
			return "aggiungi";
		
		else {
			
			repoPizza.save(formPizza);
			
			return "redirect:/menu";
			
		}	
		
	}
	
}