package ch.fincons.library.controller;

import ch.fincons.library.entities.Articoli;
import ch.fincons.library.entities.User;
import ch.fincons.library.exception.BindingException;
import ch.fincons.library.exception.DuplicateException;
import ch.fincons.library.exception.NotFoundException;
import ch.fincons.library.model.InfoMsg;
import ch.fincons.library.service.ArticoliService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/articoli")
@Log
@Api(value="AlphaShop", tags="Controller Operazioni di Gestione Dati Articoli")
public class ArticoliController
{
	//private static final Logger logger = LoggerFactory.getLogger(ArticoliController.class);
	
	@Autowired
	private ArticoliService articoliService;
	
	@Autowired
	private ResourceBundleMessageSource errMessage;


	// ------------------- Ricerca Per Barcode ------------------------------------
	@ApiOperation(
			value = "Ricerca tutti gli articoli",
			notes = "Restituisce i dati deli articoli in formato JSON",
			response = Articoli.class,
			produces = "application/json")
	@ApiResponses(value =
			{   @ApiResponse(code = 200, message = "Articoli trovati!"),
				@ApiResponse(code = 404, message = "Nessun articolo presente!"),
				@ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
				@ApiResponse(code = 401, message = "Non sei AUTENTICATO")
			})
	@GetMapping(value = "/cerca", produces = "application/json")
	public ResponseEntity<List<Articoli>> listAllArticoli() throws NotFoundException
	{
		log.info(String.format("****** Articoli presenti nel sistema *******") );

		List<Articoli> articoli = articoliService.findAll();

		if (articoli.isEmpty())
		{
			String ErrMsg = String.format("Articoli non presenti nel sistema");

			log.warning(ErrMsg);

			throw new NotFoundException(ErrMsg);

			//return new ResponseEntity<Articoli>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<Articoli>>(articoli, HttpStatus.OK);
	}

	
	// ------------------- Ricerca Per Barcode ------------------------------------
	@ApiOperation(
		      value = "Ricerca l'articolo per BARCODE", 
		      notes = "Restituisce i dati dell'articolo in formato JSON",
		      response = Articoli.class,
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 200, message = "L'articolo cercato è stato trovato!"),
	    @ApiResponse(code = 404, message = "L'articolo cercato NON è stato trovato!"),
	    @ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
	    @ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
	@GetMapping(value = "/cerca/ean/{barcode}", produces = "application/json")
	public ResponseEntity<Articoli> listArtByEan(@ApiParam("Barcode univoco dell'articolo") @PathVariable("barcode") String Barcode) throws NotFoundException
	{
		log.info(String.format("****** Otteniamo l'articolo con barcode %s *******", Barcode) );
		
		Articoli articolo = articoliService.selByBarcode(Barcode);
		
		if (articolo == null)
		{
			String ErrMsg = String.format("Il barcode %s non è stato trovato!", Barcode);
			
			log.warning(ErrMsg);
			
			throw new NotFoundException(ErrMsg);
			
			//return new ResponseEntity<Articoli>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Articoli>(articolo, HttpStatus.OK);
	}
	
	// ------------------- Ricerca Per Codice ------------------------------------
	@ApiOperation(
		      value = "Ricerca l'articolo per CODICE", 
		      notes = "Restituisce i dati dell'articolo in formato JSON",
		      response = Articoli.class, 
		      produces = "application/json")
	@ApiResponses(value =
	{ 	@ApiResponse(code = 200, message = "L'articolo cercato è stato trovato!"),
		@ApiResponse(code = 404, message = "L'articolo cercato NON è stato trovato!"),
		@ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
		@ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
	@GetMapping(value = "/cerca/codice/{codart}", produces = "application/json")
	public ResponseEntity<Articoli> listArtByCodArt(@PathVariable("codart") String CodArt)  throws NotFoundException
	{
		log.info(String.format("****** Otteniamo l'articolo con codice %s *******", CodArt));

		Articoli articolo = articoliService.selByCodArt(CodArt);

		if (articolo == null)
		{
			String ErrMsg = String.format("L'articolo con codice %s non è stato trovato!", CodArt);
			
			log.warning(ErrMsg);

			throw new NotFoundException(ErrMsg);
		}

		return new ResponseEntity<Articoli>(articolo, HttpStatus.OK);
	}

	// ------------------- Ricerca Per Titolo ------------------------------------
	@ApiOperation(
			value = "Ricerca l'articolo per titolo",
			notes = "Restituisce i dati dell'articolo in formato JSON",
			response = Articoli.class,
			produces = "application/json")
	@ApiResponses(value =
			{ 	@ApiResponse(code = 200, message = "L'articolo cercato è stato trovato!"),
					@ApiResponse(code = 404, message = "L'articolo cercato NON è stato trovato!"),
					@ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
					@ApiResponse(code = 401, message = "Non sei AUTENTICATO")
			})
	@GetMapping(value = "/cerca/titolo/{titolo}", produces = "application/json")
	public ResponseEntity<Articoli> listArtByTitolo(@PathVariable("titolo") String titolo)  throws NotFoundException
	{
		log.info(String.format("****** Otteniamo l'articolo con titolo %s *******", titolo));

		Articoli articolo = articoliService.selByTitolo(titolo);

		if (articolo == null)
		{
			String ErrMsg = String.format("L'articolo con titolo %s non è stato trovato!", titolo);

			log.warning(ErrMsg);

			throw new NotFoundException(ErrMsg);
		}

		return new ResponseEntity<Articoli>(articolo, HttpStatus.OK);
	}
	
	// ------------------- Ricerca Per Descrizione ------------------------------------
	@ApiOperation(
		      value = "Ricerca l'articolo per DESCRIZIONE o parte di essa", 
		      notes = "Restituisce un collezione di articoli in formato JSON",
		      response = Articoli.class, 
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 200, message = "L'articolo/i sono stati trovati"),
		@ApiResponse(code = 404, message = "Non è stato trovato alcun articolo"),
		@ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad accedere alle informazioni"),
		@ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
	@GetMapping(value = "/cerca/descrizione/{filter}", produces = "application/json")
	public ResponseEntity<List<Articoli>> listArtByDesc(@PathVariable("filter") String Filter) throws NotFoundException
	{
		log.info(String.format("****** Otteniamo gli articoli con Descrizione: %s *******",Filter));
		
		List<Articoli> articoli = articoliService.selByDescrizione(Filter + "%");
		
		if (articoli.size() == 0)
		{
			String ErrMsg = String.format("Non è stato trovato alcun articolo avente descrizione %s", Filter);
			
			log.warning(ErrMsg);
			
			throw new NotFoundException(ErrMsg);
			
		}
		
		return new ResponseEntity<List<Articoli>>(articoli, HttpStatus.OK);
	}
	
	// ------------------- INSERIMENTO ARTICOLO ------------------------------------
	@ApiOperation(
		      value = "Inserimento dati NUOVO articolo", 
		      notes = "Può essere usato solo per l'inserimento dati di un nuovo articolo",
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 200, message = "Dati articolo salvati con successo"),
		@ApiResponse(code = 400, message = "Uno o più dati articolo non validi"),
		@ApiResponse(code = 406, message = "Inserimento dati articolo esistente in anagrafica"),
		@ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad inserire dati"),
		@ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
	@PostMapping(value = "/inserisci", produces = "application/json")
	public ResponseEntity<?> createArt(@Valid @RequestBody Articoli articolo, BindingResult bindingResult) throws BindingException, DuplicateException
	{
		log.info(String.format("Salviamo l'articolo con codice %s",articolo.getCodArt()));
		
		if (bindingResult.hasErrors())
		{
			String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
			
			log.warning(MsgErr);

			throw new BindingException(MsgErr);
		}
		 
		//Disabilitare se si vuole gestire anche la modifica 
		Articoli checkArt =  articoliService.selByCodArt(articolo.getCodArt());

		if (checkArt != null)
		{
			String MsgErr = String.format("Articolo %s presente in anagrafica! " + "Impossibile utilizzare il metodo POST", articolo.getCodArt());
			
			log.warning(MsgErr);
			
			throw new DuplicateException(MsgErr);
		}
		
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();
		
		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectNode responseNode = mapper.createObjectNode();
		
		articoliService.insArticolo(articolo);
		
		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", "Inserimento Articolo " + articolo.getCodArt() + " Eseguito Con Successo");
		
		return new ResponseEntity<>(responseNode, headers, HttpStatus.CREATED);
	}
	
	// ------------------- MODIFICA ARTICOLO ------------------------------------
	@ApiOperation(
		      value = "MODIFICA dati articolo in anagrfica", 
		      notes = "Può essere usato solo per la modifica dati di un articolo presente in anagrafica",
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 200, message = "Dati articolo salvati con successo"),
		@ApiResponse(code = 400, message = "Uno o più dati articolo non validi"),
		@ApiResponse(code = 404, message = "Articolo non presente in anagrafica"),
		@ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad inserire dati"),
		@ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
	@RequestMapping(value = "/modifica", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<?> updateArt(@Valid @RequestBody Articoli articolo, BindingResult bindingResult) throws BindingException,NotFoundException
	{
		log.info(String.format("Modifichiamo l'articolo con codice %s", articolo.getCodArt()));
		
		if (bindingResult.hasErrors())
		{
			String MsgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
	
			log.warning(MsgErr);

			throw new BindingException(MsgErr);
		}
		
		Articoli checkArt =  articoliService.selByCodArt(articolo.getCodArt());

		if (checkArt == null)
		{
			String MsgErr = String.format("Articolo %s non presente in anagrafica! " + "Impossibile utilizzare il metodo PUT", articolo.getCodArt());
			
			log.warning(MsgErr);
			
			throw new NotFoundException(MsgErr);
		}
		
		HttpHeaders headers = new HttpHeaders();
		ObjectMapper mapper = new ObjectMapper();
		
		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectNode responseNode = mapper.createObjectNode();
		
		articoliService.insArticolo(articolo);
		
		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", "Modifica Articolo " + articolo.getCodArt() + " Eseguita Con Successo");

		return new ResponseEntity<>(responseNode, headers, HttpStatus.CREATED);
	}
	
	@ApiOperation(
		      value = "ELIMINAZIONE dati articolo in anagrfica", 
		      notes = "Si esegue una eliminazione a cascata dei barcode e degli ingredienti",
		      produces = "application/json")
	@ApiResponses(value =
	{   @ApiResponse(code = 200, message = "Dati articolo eliminati con successo"),
		@ApiResponse(code = 404, message = "Articolo non presente in anagrafica"),
		@ApiResponse(code = 403, message = "Non sei AUTORIZZATO ad inserire dati"),
		@ApiResponse(code = 401, message = "Non sei AUTENTICATO")
	})
	@DeleteMapping(value = "/elimina/{codart}", produces = "application/json")
	public ResponseEntity<InfoMsg> deleteArt(@PathVariable("codart") String CodArt) throws  NotFoundException
	{
		log.info(String.format("Eliminiamo l'articolo con codice %s", CodArt));
		
		Articoli articolo = articoliService.selByCodArt(CodArt);

		if (articolo == null)
		{
			String MsgErr = String.format("Articolo %s non presente in anagrafica!",CodArt);
			
			log.warning(MsgErr);
			
			throw new NotFoundException(MsgErr);
		}
		
		articoliService.delArticolo(articolo);
		
		String code = HttpStatus.OK.toString();
		String message = String.format("Eliminazione Articolo %s Eseguita Con Successo", CodArt);
		
		return new ResponseEntity<InfoMsg>(new InfoMsg(code, message), HttpStatus.OK);
	}

	@GetMapping(value = "/infoUser")
	public ResponseEntity<User> infoUser() throws  NotFoundException
	{
		log.info(String.format("Info Utente"));

		return new ResponseEntity<User>(new User(), HttpStatus.OK);
	}
}