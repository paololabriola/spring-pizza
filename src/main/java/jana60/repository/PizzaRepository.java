package jana60.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jana60.model.Pizza;

public interface PizzaRepository extends CrudRepository<Pizza, Integer>{

	List<Pizza> findAllByOrderByPrezzoAsc();
	
	List<Pizza> findByNomeContainingIgnoreCase(String queryNome);
	
	public Integer countByNome(String nome);
	
	public Pizza findByNome(String nome);
	
}