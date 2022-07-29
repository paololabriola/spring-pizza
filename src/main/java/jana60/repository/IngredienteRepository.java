package jana60.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jana60.model.Ingrediente;

public interface IngredienteRepository extends CrudRepository<Ingrediente, Integer> {

	List<Ingrediente> findAllByOrderByNome();
	
}
