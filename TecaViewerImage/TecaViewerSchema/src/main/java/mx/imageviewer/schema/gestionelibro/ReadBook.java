//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.19 alle 11:04:31 AM CET 
//


package mx.imageviewer.schema.gestionelibro;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element ref="{http://www.imageViewer.mx/schema/gestioneLibro}datiBibliografici"/>
 *         &lt;element ref="{http://www.imageViewer.mx/schema/gestioneLibro}immagini"/>
 *         &lt;element ref="{http://www.imageViewer.mx/schema/gestioneLibro}stru" maxOccurs="unbounded" minOccurs="0"/>
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
    "datiBibliografici",
    "immagini",
    "stru"
})
@XmlRootElement(name = "readBook")
public class ReadBook {

    @XmlElement(namespace = "http://www.imageViewer.mx/schema/gestioneLibro", required = true)
    protected DatiBibliografici datiBibliografici;
    @XmlElement(namespace = "http://www.imageViewer.mx/schema/gestioneLibro", required = true)
    protected Immagini immagini;
    @XmlElement(namespace = "http://www.imageViewer.mx/schema/gestioneLibro")
    protected List<Stru> stru;

    /**
     * Recupera il valore della proprietà datiBibliografici.
     * 
     * @return
     *     possible object is
     *     {@link DatiBibliografici }
     *     
     */
    public DatiBibliografici getDatiBibliografici() {
        return datiBibliografici;
    }

    /**
     * Imposta il valore della proprietà datiBibliografici.
     * 
     * @param value
     *     allowed object is
     *     {@link DatiBibliografici }
     *     
     */
    public void setDatiBibliografici(DatiBibliografici value) {
        this.datiBibliografici = value;
    }

    /**
     * Recupera il valore della proprietà immagini.
     * 
     * @return
     *     possible object is
     *     {@link Immagini }
     *     
     */
    public Immagini getImmagini() {
        return immagini;
    }

    /**
     * Imposta il valore della proprietà immagini.
     * 
     * @param value
     *     allowed object is
     *     {@link Immagini }
     *     
     */
    public void setImmagini(Immagini value) {
        this.immagini = value;
    }

    /**
     * Gets the value of the stru property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stru property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStru().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Stru }
     * 
     * 
     */
    public List<Stru> getStru() {
        if (stru == null) {
            stru = new ArrayList<Stru>();
        }
        return this.stru;
    }

}
