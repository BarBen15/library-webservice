package ch.fincons.library.controller;

import ch.fincons.library.Application;
import ch.fincons.library.entities.Articoli;
import ch.fincons.library.repository.ArticoliRepository;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@AutoConfigureMockMvc
@ContextConfiguration(classes = Application.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ArticoliControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    ArticoliRepository articoliRepository;

    @BeforeEach
    public void setup() throws JSONException, IOException
    {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
    }

    private String ApiBaseUrl = "/api/articoli";

    String JsonData =
            "{\n" +
                    "    \"codArt\": \"2000301\",\n" +
                    "    \"titolo\": \"ANIMALI\",\n" +
                    "    \"descrizione\": \"PINOCCHIO\",\n" +
                    "    \"prezzo\": 65,\n" +
                    "    \"dataStampa\": \"2019-05-14\",\r\n" +
                    "    \"barcode\": [\n" +
                    "        {\n" +
                    "            \"barcode\": \"800849\",\n" +
                    "            \"idTipoArt\": \"CD\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";

    @Test
    @Order(1)
    void testListArtByEan() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/cerca/ean/800849")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                //articoli
                .andExpect(jsonPath("$.codArt").exists())
                .andExpect(jsonPath("$.codArt").value("2000301"))
                .andExpect(jsonPath("$.titolo").exists())
                .andExpect(jsonPath("$.titolo").value("ANIMALI"))
                .andExpect(jsonPath("$.descrizione").exists())
                .andExpect(jsonPath("$.descrizione").value("PINOCCHIO"))
                .andExpect(jsonPath("$.prezzo").exists())
                .andExpect(jsonPath("$.prezzo").value(65))
                .andExpect(jsonPath("$.dataStampa").exists())
                .andExpect(jsonPath("$.dataStampa").value("2019-05-14"))
                //barcode
                .andExpect(jsonPath("$.barcode[0].barcode").exists())
                .andExpect(jsonPath("$.barcode[0].barcode").value("800849"))
                .andExpect(jsonPath("$.barcode[0].idTipoArt").exists())
                .andExpect(jsonPath("$.barcode[0].idTipoArt").value("CD"))

                .andDo(print());
    }

    private String Barcode = "80084999";

    @Test
    @Order(2)
    public void testErrlistArtByEan() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/cerca/ean/" + Barcode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonData)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codice").value(404))
                .andExpect(jsonPath("$.messaggio").value("Il barcode " + Barcode + " non è stato trovato!"))
                .andDo(print());
    }

    @Test
    @Order(3)
    public void testListArtByCodArt() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/cerca/codice/2000301")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonData))
                .andExpect(jsonPath("codArt").value("2000301"))
                .andDo(print())
                .andReturn();
    }

    private String codArt = "20009999";

    @Test
    @Order(4)
    public void testErrlistArtByCodArt() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/cerca/codice/" + codArt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonData)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codice").value(404))
                .andExpect(jsonPath("$.messaggio").value("L'articolo con codice " + codArt + " non è stato trovato!"))
                .andDo(print());
    }

    private String JsonData2 = "[" + JsonData + "]";

    @Test
    @Order(5)
    public void testListArtByDesc() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/cerca/descrizione/PINOCCHIO")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonData2))
                .andReturn();
    }

    @Test
    @Order(6)
    public void testErrlistArtByDesc() throws Exception
    {
        String Filter = "123ABC";

        mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/cerca/descrizione/" + Filter)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codice").value(404))
                .andExpect(jsonPath("$.messaggio").value("Non è stato trovato alcun articolo avente descrizione " + Filter))
                .andReturn();
    }

    String JsonDataIns =
            "{\r\n" +
                    "    \"codArt\": \"1234666\",\r\n" +
                    "    \"titolo\": \"Cani\",\r\n" +
                    "    \"descrizione\": \"Articoli Unit Test Inserimento\",\r\n" +
                    "    \"prezzo\": 60,\r\n" +
                    "    \"dataStampa\": \"2019-05-14\",\r\n" +
                    "    \"barcode\": [\r\n" +
                    "        {\r\n" +
                    "            \"barcode\": \"12345678\",\r\n" +
                    "            \"idTipoArt\": \"CD\"\r\n" +
                    "        }\r\n" +
                    "    ]\r\n" +
                    "}";

    @Test
    @Order(7)
    public void testInsArticolo() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.post(ApiBaseUrl + "/inserisci")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDataIns)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("200 OK"))
                .andExpect(jsonPath("$.message").value("Inserimento Articolo 1234666 Eseguito Con Successo"))

                .andDo(print());

        assertThat(articoliRepository.findByCodArt("1234666"))
                .extracting(Articoli::getCodArt)
                .isEqualTo("1234666");
    }

    @Test
    @Order(8)
    public void testErrInsArticolo2() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.post(ApiBaseUrl + "/inserisci")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDataIns)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.codice").value(406))
                .andExpect(jsonPath("$.messaggio").value("Articolo 1234666 presente in anagrafica! Impossibile utilizzare il metodo POST"))
                .andDo(print());
    }

    String ErrJsonData =
            "{\r\n" +
                    "    \"codArt\": \"1234666\",\r\n" +
                    "    \"titolo\": \"Cani\",\r\n" +
                    "    \"descrizione\": \"\",\r\n" +  //<<< Articolo privo di descrizione
                    "    \"prezzo\": 60,\r\n" +
                    "    \"dataStampa\": \"2019-05-14\",\r\n" +
                    "    \"barcode\": [\r\n" +
                    "        {\r\n" +
                    "            \"barcode\": \"12345678\",\r\n" +
                    "            \"idTipoArt\": \"CD\"\r\n" +
                    "        }\r\n" +
                    "    ]\r\n" +
                    "}";

    @Test
    @Order(9)
    public void testErrInsArticolo() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.post(ApiBaseUrl + "/inserisci")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ErrJsonData)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codice").value(400))
                .andExpect(jsonPath("$.messaggio").value("Il campo Descrizione deve avere un numero di caratteri compreso tra 6 e 80"))
                .andDo(print());
    }

    String JsonDataMod =
            "{\r\n" +
                    "    \"codArt\": \"1234666\",\r\n" +
                    "    \"titolo\": \"Cani\",\r\n" +
                    "    \"descrizione\": \"Articoli Unit Test Inserimento\",\r\n" +
                    "    \"prezzo\": 150,\r\n" + //<<< Modifica Prezzo Articolo a 150
                    "    \"dataStampa\": \"2019-05-14\",\r\n" +
                    "    \"barcode\": [\r\n" +
                    "        {\r\n" +
                    "            \"barcode\": \"12345678\",\r\n" +
                    "            \"idTipoArt\": \"CD\"\r\n" +
                    "        }\r\n" +
                    "    ]\r\n" +
                    "}";

    @Test
    @Order(10)
    public void testUpdArticolo() throws Exception
    {

        mockMvc.perform(MockMvcRequestBuilders.put(ApiBaseUrl + "/modifica")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonDataMod)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("200 OK"))
                .andExpect(jsonPath("$.message").value("Modifica Articolo 1234666 Eseguita Con Successo"))
                .andDo(print());

        assertThat(articoliRepository.findByCodArt("1234666"))
                .extracting(Articoli::getPrezzo)
                .isEqualTo(150);

    }

    String ErrJsonDataMod =
            "{\r\n" +
                    "    \"codArt\": \"123467\",\r\n" + //<<< Articolo NON esistente
                    "    \"descrizione\": \"Pesci\",\r\n" +
                    "    \"descrizione\": \"Articoli Unit Test Inserimento\",\r\n" +
                    "    \"prezzo\": 150,\r\n" +
                    "    \"dataStampa\": \"2019-05-14\",\r\n" +
                    "    \"barcode\": [\r\n" +
                    "        {\r\n" +
                    "            \"barcode\": \"12345678\",\r\n" +
                    "            \"idTipoArt\": \"CD\"\r\n" +
                    "        }\r\n" +
                    "    ]\r\n" +
                    "}";

    @Test
    @Order(11)
    public void testErrUpdArticolo() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.put(ApiBaseUrl + "/modifica")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ErrJsonDataMod)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codice").value(404))
                .andExpect(jsonPath("$.messaggio").value("Articolo 123467 non presente in anagrafica! Impossibile utilizzare il metodo PUT"))
                .andDo(print());
    }

    @Test
    @Order(12)
    public void testDelArticolo() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.delete(ApiBaseUrl + "/elimina/1234666")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200 OK"))
                .andExpect(jsonPath("$.message").value("Eliminazione Articolo 1234666 Eseguita Con Successo"))
                .andDo(print());
    }

    @Test
    @Order(13)
    public void testErrDelArticolo() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.delete(ApiBaseUrl + "/elimina/1234699")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ErrJsonDataMod)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codice").value(404))
                .andExpect(jsonPath("$.messaggio").value("Articolo 1234699 non presente in anagrafica!"))
                .andDo(print());
    }

    @Test
    @Order(14)
    void testGetAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ApiBaseUrl + "/cerca"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}