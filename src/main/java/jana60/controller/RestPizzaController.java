package jana60.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jana60.model.Pizza;
import jana60.repository.PizzaRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/pizza")
public class RestPizzaController {

	@Autowired
	private PizzaRepository repoPizza;
	
	@GetMapping
	public List<Pizza> getAllPizza() {
		
		return (List<Pizza>) repoPizza.findAll();
		
	}
	
	@GetMapping("/{idPizza}")
	public Pizza getBookById(@PathVariable Integer idPizza) {
		
		Optional<Pizza> result = repoPizza.findById(idPizza);
		
		if (result.isPresent()) {
			
			return result.get();
			
		} else {
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza con id " + idPizza + " not present");
			
		}
		
	}
	
}
