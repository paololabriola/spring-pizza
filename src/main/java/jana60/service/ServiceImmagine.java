package jana60.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jana60.model.FormImmagine;
import jana60.model.Immagine;
import jana60.model.Pizza;
import jana60.repository.ImmagineRepository;
import jana60.repository.PizzaRepository;

@Service
public class ServiceImmagine {
	
	@Autowired
	private ImmagineRepository repoImg;
	
	@Autowired
	private PizzaRepository repoPizza;
	
	// A partire dall'id della pizza cerco tutte le immagini associate.
	public List<Immagine> getImmagineByPizzaId(Integer id) {
		
		Pizza pizza = repoPizza.findById(id).get();
		return repoImg.findByPizza(pizza);
		
	}
	
	// A partire dall'id della pizza creo un istanza di FormImmagine associata a quella pizza.
	public FormImmagine creaFormImmagine(Integer id) {
		
		Pizza pizza = repoPizza.findById(id).get();
		
		FormImmagine img = new FormImmagine();
		
		img.setPizza(pizza);
		
		return img;
				
	}
	
	// A partire dall'oggetto FormImmagine creo un oggetto di tipo Immagine da salvare su db.
	public Immagine creaImmagine(FormImmagine formImmagine) throws IOException {
		
		Immagine imgDaSalvare = new Immagine();
		
		imgDaSalvare.setPizza(formImmagine.getPizza());
		
		if(formImmagine.getContentMultipart() != null) {
			
			byte[] contenutoSerializzato = formImmagine.getContentMultipart().getBytes();
			imgDaSalvare.setContent(contenutoSerializzato);
			
		}
		
		return repoImg.save(imgDaSalvare);
			
	}
	
	// A partire dall'id dell'immagine restituisco le immagini.
	public byte[] prendiImmagini(Integer id) {
		
		Immagine img = repoImg.findById(id).get();
		
		return img.getContent();
		
	}

}
