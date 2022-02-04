package ch.fincons.library.repository;

import ch.fincons.library.Application;
import ch.fincons.library.entities.Articoli;
import ch.fincons.library.entities.Barcode;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest()
@ContextConfiguration(classes = Application.class)
@TestMethodOrder(OrderAnnotation.class)
public class ArticoliRepositoryTest
{
	@Autowired
	private ArticoliRepository articoliRepository;
	
	@Test
	@Order(1)
	public void TestInsArticolo()
	{
		Articoli articolo = new Articoli("12346","Articolo di Test",50,new Date(2014, 02, 11));

		Set<Barcode> Eans = new HashSet<>();
		Eans.add(new Barcode("12345678", "CD", articolo));
		
		articolo.setBarcode(Eans);
		
		articoliRepository.save(articolo);
		
		assertThat(articoliRepository.findByCodArt("12346"))
		.extracting(Articoli::getDescrizione)
		.isEqualTo("Articolo di Test");
	}
	
	@Test
	@Order(2)
	public void TestSelByDescrizioneLike()
	{
		List<Articoli> items = articoliRepository.findByDescrizioneLike("Articolo di Test");
		assertEquals(1, items.size());
	}
	
	@Test
	@Order(3)
	public void TestfindByEan() throws Exception
	{
		assertThat(articoliRepository.SelByEan("12345678"))
				.extracting(Articoli::getDescrizione)
				.isEqualTo("Articolo di Test");
				
	}
	
	@Test
	@Order(4)
	public void TestDelArticolo()
	{
		Articoli articolo = articoliRepository.findByCodArt("12346");
		
		articoliRepository.delete(articolo);
		
	}
	

}