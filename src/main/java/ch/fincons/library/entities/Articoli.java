package ch.fincons.library.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ARTICOLI")
@Data
@NoArgsConstructor  
@AllArgsConstructor // Costruttore con tutti i parametri
public class Articoli  implements Serializable
{
	private static final long serialVersionUID = 7361753083273455478L;
	
	@Id
	@Column(name = "CODART")
	@Size(min = 5, max = 20, message = "{Size.Articoli.codArt.Validation}")
	@NotNull(message = "{NotNull.Articoli.codArt.Validation}")
	@ApiModelProperty(notes = "Il Codice Interno Univoco dell'Articolo")
	private String codArt;
	
	@Column(name = "DESCRIZIONE")
	@Size(min = 6, max = 80, message = "{Size.Articoli.descrizione.Validation}")
	private String descrizione;
	
	@Column(name = "PREZZO")
	private Integer prezzo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_STAMPA")
	private Date dataStampa;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "articolo", orphanRemoval = true)
	@JsonManagedReference
	private Set<Barcode> barcode = new HashSet<>();
	
	public Articoli(String codArt, String descrizione, Integer prezzo, Date dataStampa)
	{
		this.codArt = codArt;
		this.descrizione = descrizione;
		this.prezzo = prezzo;
		this.dataStampa = dataStampa;
	}

}
