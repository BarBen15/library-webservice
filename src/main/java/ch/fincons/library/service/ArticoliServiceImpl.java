package ch.fincons.library.service;

import ch.fincons.library.entities.Articoli;
import ch.fincons.library.repository.ArticoliRepository;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames={"libreria"})
public class ArticoliServiceImpl implements ArticoliService {

	private ArticoliRepository articoliRepository;

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
	@Cacheable(cacheNames = "articolo", key="#codArt", sync = true)
	public Articoli selByCodArt(String codArt)
	{
		return articoliRepository.findByCodArt(codArt);
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

}

