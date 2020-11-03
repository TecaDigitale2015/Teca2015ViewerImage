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
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element ref="{http://www.imageViewer.mx/schema/gestioneLibro}nomenclatura"/>
 *         &lt;choice>
 *           &lt;element ref="{http://www.imageViewer.mx/schema/gestioneLibro}stru" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="immagine" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute ref="{http://www.imageViewer.mx/schema/gestioneLibro}sequenza"/>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
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
    "nomenclatura",
    "stru",
    "immagine"
})
@XmlRootElement(name = "stru")
public class Stru {

    @XmlElement(namespace = "http://www.imageViewer.mx/schema/gestioneLibro", required = true)
    protected String nomenclatura;
    @XmlElement(namespace = "http://www.imageViewer.mx/schema/gestioneLibro")
    protected List<Stru> stru;
    protected Stru.Immagine immagine;

    /**
     * Nomenclatura della struttura da visualizzare
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomenclatura() {
        return nomenclatura;
    }

    /**
     * Imposta il valore della proprietà nomenclatura.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomenclatura(String value) {
        this.nomenclatura = value;
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

    /**
     * Recupera il valore della proprietà immagine.
     * 
     * @return
     *     possible object is
     *     {@link Stru.Immagine }
     *     
     */
    public Stru.Immagine getImmagine() {
        return immagine;
    }

    /**
     * Imposta il valore della proprietà immagine.
     * 
     * @param value
     *     allowed object is
     *     {@link Stru.Immagine }
     *     
     */
    public void setImmagine(Stru.Immagine value) {
        this.immagine = value;
    }


    /**
     * <p>Classe Java per anonymous complex type.
     * 
     * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute ref="{http://www.imageViewer.mx/schema/gestioneLibro}sequenza"/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Immagine {

        @XmlAttribute(name = "sequenza", namespace = "http://www.imageViewer.mx/schema/gestioneLibro")
        protected Integer sequenza;

        /**
         * Recupera il valore della proprietà sequenza.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getSequenza() {
            return sequenza;
        }

        /**
         * Imposta il valore della proprietà sequenza.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setSequenza(Integer value) {
            this.sequenza = value;
        }

    }

}
