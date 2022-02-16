package ch.fincons.library.service;

import ch.fincons.library.entities.Articoli;
import ch.fincons.library.repository.ArticoliRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import org.json.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames={"libreria"})
public class ArticoliServiceImpl implements ArticoliService {

	private ArticoliRepository articoliRepository;

	@Autowired
	RestTemplate restTemplate;

	public ArticoliServiceImpl(ArticoliRepository articoliRepository) {
		this.articoliRepository = articoliRepository;
	}

	@Override
	@Cacheable(cacheNames = "articoli", key="#root.methodName")
	public List<Articoli> findAll()
	{
		return articoliRepository.findAll();
	}

	@Override
	@Cacheable
	public List<Articoli> selByDescrizione(String descrizione)
	{
		return articoliRepository.findByDescrizioneLike(descrizione.toUpperCase());
	}

	@Override
	@Cacheable
	public Articoli selByTitolo(String titolo)
	{
		return articoliRepository.findByTitolo(titolo);
	}

	@Override
	@Cacheable(cacheNames = "articolo", key="#codArt", sync = true)
	public Articoli selByCodArt(String codArt)
	{
		Articoli articolo = articoliRepository.findByCodArt(codArt);
		if (articolo != null) {
			String statusArt = getStatusArt(codArt, 3);
			JSONObject obj = new JSONObject(statusArt);
			String statoArticolo = obj.getString("status");
			log.info("Stato dell'articolo: " + statoArticolo);
			articolo.setStato(statoArticolo);
		} else {
			log.info("Codice articolo %s NON presente in archivio" + articolo.getCodArt());
		}

		return articolo;
	}


	@Override
	@Cacheable
	public Articoli selByBarcode(String barcode)
	{
		return articoliRepository.selByEan(barcode);
	}

	@Override
	@Transactional
	@Caching(evict = {
			@CacheEvict(cacheNames = "articoli", allEntries = true, beforeInvocation = true),
			@CacheEvict(cacheNames = "articolo", key = "#articolo.codArt")
	})
	public void delArticolo(Articoli articolo)
	{
		articoliRepository.delete(articolo);
	}

	@Override
	@Transactional
	@Caching(evict = {
			@CacheEvict(cacheNames = "articoli", allEntries = true, beforeInvocation = true),
			@CacheEvict(cacheNames = "articolo", key = "#articolo.codArt")
	})
	public void insArticolo(Articoli articolo)
	{
		articoliRepository.save(articolo);
	}


	private String getStatusArt(String codArt, int quantitaRichiesta)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity <String> entity = new HttpEntity<String>(headers);

		return restTemplate.exchange("http://localhost:5051/api/booking/cerca/codice/"+codArt+"/quantita/"+quantitaRichiesta, HttpMethod.GET, entity, String.class).getBody();
	}

}

