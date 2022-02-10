package ch.fincons.library.exception.service;

import ch.fincons.library.entities.Articoli;

import java.util.List;

public interface ArticoliService 
{
	public List<Articoli> findAll();

	public List<Articoli> selByDescrizione(String descrizione);
	
	public Articoli selByCodArt(String codArt);
	
	public Articoli selByBarcode(String barcode);
	
	public void delArticolo(Articoli articolo);
	
	public void insArticolo(Articoli articolo);

}
