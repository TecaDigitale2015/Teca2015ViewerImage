//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.19 alle 11:04:31 AM CET 
//


package mx.imageviewer.schema.gestionelibro;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="autore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="titolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="urlDatiBibliografici" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="pubblicazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unitaFisica" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "autore",
    "titolo",
    "urlDatiBibliografici",
    "pubblicazione",
    "unitaFisica"
})
@XmlRootElement(name = "datiBibliografici")
public class DatiBibliografici {

    protected String autore;
    @XmlElement(required = true)
    protected String titolo;
    @XmlSchemaType(name = "anyURI")
    protected String urlDatiBibliografici;
    protected String pubblicazione;
    protected Object unitaFisica;

    /**
     * Recupera il valore della proprietà autore.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutore() {
        return autore;
    }

    /**
     * Imposta il valore della proprietà autore.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutore(String value) {
        this.autore = value;
    }

    /**
     * Recupera il valore della proprietà titolo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Imposta il valore della proprietà titolo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitolo(String value) {
        this.titolo = value;
    }

    /**
     * Recupera il valore della proprietà urlDatiBibliografici.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlDatiBibliografici() {
        return urlDatiBibliografici;
    }

    /**
     * Imposta il valore della proprietà urlDatiBibliografici.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlDatiBibliografici(String value) {
        this.urlDatiBibliografici = value;
    }

    /**
     * Recupera il valore della proprietà pubblicazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPubblicazione() {
        return pubblicazione;
    }

    /**
     * Imposta il valore della proprietà pubblicazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPubblicazione(String value) {
        this.pubblicazione = value;
    }

    /**
     * Recupera il valore della proprietà unitaFisica.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getUnitaFisica() {
        return unitaFisica;
    }

    /**
     * Imposta il valore della proprietà unitaFisica.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setUnitaFisica(Object value) {
        this.unitaFisica = value;
    }

}
