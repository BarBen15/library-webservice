package ch.fincons.library.service;

import ch.fincons.library.entities.Articoli;
import ch.fincons.library.repository.ArticoliRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ArticoliServiceImpl implements ArticoliService
{
	@Autowired
	ArticoliRepository articoliRepository;

	@Override
	public List<Articoli> SelByDescrizione(String descrizione)
	{
		return articoliRepository.findByDescrizioneLike(descrizione.toUpperCase());
	}

	@Override
	public Articoli SelByCodArt(String codArt) 
	{
		return articoliRepository.findByCodArt(codArt);
	}

	@Override
	public Articoli SelByBarcode(String barcode) 
	{
		return articoliRepository.SelByEan(barcode);
	}

	@Override
	@Transactional
	public void DelArticolo(Articoli articolo) 
	{
		articoliRepository.delete(articolo);
	}

	@Override
	@Transactional
	public void InsArticolo(Articoli articolo) 
	{
		articoliRepository.save(articolo);
	}

}

