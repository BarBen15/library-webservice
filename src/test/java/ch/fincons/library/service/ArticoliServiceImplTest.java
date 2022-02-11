package ch.fincons.library.service;

import ch.fincons.library.entities.Articoli;
import ch.fincons.library.entities.Barcode;
import ch.fincons.library.repository.ArticoliRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class ArticoliServiceImplTest {

    @Mock
    private ArticoliRepository articoliRepository;

    private ArticoliService articoliService;

    @Before
    public void init(){
        articoliService = new ArticoliServiceImpl(articoliRepository);
    }

    @Test
    public void findAll() {

        Set<Barcode> barcode1 = new HashSet<>(Arrays.asList(Barcode.builder()
                                                    .barcode("1234")
                                                    .idTipoArt("libro")
                                                    .articolo(new Articoli("1234","Articoli Test", 100, new Date(2014, 02, 11), null))
                                                    .build()));

        Set<Barcode> barcode2 = new HashSet<>(Arrays.asList(Barcode.builder()
                                                    .barcode("12345")
                                                    .idTipoArt("CD")
                                                    .articolo(new Articoli("12345","Articoli Test", 50, new Date(2021, 02, 11), null))
                                                    .build()));

        List<Articoli> articoli = Arrays.asList(new Articoli("1234","Articoli Test", 100, new Date(2014, 02, 11), barcode1),
                                                new Articoli("12345","Articoli Test1", 50, new Date(2021, 01, 3), barcode2));


        Mockito.when(articoliRepository.findAll()).thenReturn(articoli);
        List<Articoli> artServ = articoliService.findAll();

        assertNotNull(articoli);

        assertTrue(articoliService.findAll().size() == 2);
        assertEquals("1234", artServ.get(0).getCodArt());
        
    }

    @Test
    public void selByDescrizione() {
    }

    @Test
    public void selByCodArt() {
    }

    @Test
    public void selByBarcode() {
    }

    @Test
    public void delArticolo() {
    }

    @Test
    public void insArticolo() {
    }
}