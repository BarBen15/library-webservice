package ch.fincons.library.repository;

import ch.fincons.library.entities.Articoli;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticoliRepository extends JpaRepository<Articoli,String>
{
	Articoli findByCodArt(String codArt);

	Articoli findByTitolo(String titolo);
	
	List<Articoli> findByDescrizioneLike(String descrizione);
	
	//JPQL
	@Query(value="SELECT a FROM Articoli a JOIN a.barcode b WHERE b.barcode IN (:ean)")
	Articoli selByEan(@Param("ean") String ean);

}
