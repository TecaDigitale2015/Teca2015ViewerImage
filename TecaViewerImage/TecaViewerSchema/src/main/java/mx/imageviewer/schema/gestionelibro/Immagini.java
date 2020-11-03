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
 *         &lt;element name="immagine" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.imageViewer.mx/schema/gestioneLibro}nomenclatura"/>
 *                   &lt;element name="altezza" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="larghezza" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="fileIIIF" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute ref="{http://www.imageViewer.mx/schema/gestioneLibro}sequenza"/>
 *                 &lt;attribute name="visPaginaDoppia" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                 &lt;attribute name="usage" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="numImg" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="isCostola" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "immagine"
})
@XmlRootElement(name = "immagini")
public class Immagini {

    @XmlElement(required = true)
    protected List<Immagini.Immagine> immagine;
    @XmlAttribute(name = "numImg")
    protected Integer numImg;
    @XmlAttribute(name = "isCostola")
    protected Boolean isCostola;

    /**
     * Gets the value of the immagine property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the immagine property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImmagine().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Immagini.Immagine }
     * 
     * 
     */
    public List<Immagini.Immagine> getImmagine() {
        if (immagine == null) {
            immagine = new ArrayList<Immagini.Immagine>();
        }
        return this.immagine;
    }

    /**
     * Recupera il valore della proprietà numImg.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumImg() {
        return numImg;
    }

    /**
     * Imposta il valore della proprietà numImg.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumImg(Integer value) {
        this.numImg = value;
    }

    /**
     * Recupera il valore della proprietà isCostola.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsCostola() {
        if (isCostola == null) {
            return false;
        } else {
            return isCostola;
        }
    }

    /**
     * Imposta il valore della proprietà isCostola.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCostola(Boolean value) {
        this.isCostola = value;
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
     *       &lt;sequence>
     *         &lt;element ref="{http://www.imageViewer.mx/schema/gestioneLibro}nomenclatura"/>
     *         &lt;element name="altezza" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="larghezza" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="fileIIIF" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute ref="{http://www.imageViewer.mx/schema/gestioneLibro}sequenza"/>
     *       &lt;attribute name="visPaginaDoppia" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *       &lt;attribute name="usage" type="{http://www.w3.org/2001/XMLSchema}string" />
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
        "altezza",
        "larghezza",
        "fileIIIF"
    })
    public static class Immagine {

        @XmlElement(namespace = "http://www.imageViewer.mx/schema/gestioneLibro", required = true)
        protected String nomenclatura;
        protected int altezza;
        protected int larghezza;
        @XmlElement(required = true)
        protected String fileIIIF;
        @XmlAttribute(name = "ID")
        protected String id;
        @XmlAttribute(name = "sequenza", namespace = "http://www.imageViewer.mx/schema/gestioneLibro")
        protected Integer sequenza;
        @XmlAttribute(name = "visPaginaDoppia")
        protected Boolean visPaginaDoppia;
        @XmlAttribute(name = "usage")
        protected String usage;

        /**
         * Nomenclatura dell'immagine da visualizzare
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
         * Recupera il valore della proprietà altezza.
         * 
         */
        public int getAltezza() {
            return altezza;
        }

        /**
         * Imposta il valore della proprietà altezza.
         * 
         */
        public void setAltezza(int value) {
            this.altezza = value;
        }

        /**
         * Recupera il valore della proprietà larghezza.
         * 
         */
        public int getLarghezza() {
            return larghezza;
        }

        /**
         * Imposta il valore della proprietà larghezza.
         * 
         */
        public void setLarghezza(int value) {
            this.larghezza = value;
        }

        /**
         * Recupera il valore della proprietà fileIIIF.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFileIIIF() {
            return fileIIIF;
        }

        /**
         * Imposta il valore della proprietà fileIIIF.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFileIIIF(String value) {
            this.fileIIIF = value;
        }

        /**
         * Recupera il valore della proprietà id.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getID() {
            return id;
        }

        /**
         * Imposta il valore della proprietà id.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setID(String value) {
            this.id = value;
        }

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

        /**
         * Recupera il valore della proprietà visPaginaDoppia.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isVisPaginaDoppia() {
            return visPaginaDoppia;
        }

        /**
         * Imposta il valore della proprietà visPaginaDoppia.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setVisPaginaDoppia(Boolean value) {
            this.visPaginaDoppia = value;
        }

        /**
         * Recupera il valore della proprietà usage.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUsage() {
            return usage;
        }

        /**
         * Imposta il valore della proprietà usage.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUsage(String value) {
            this.usage = value;
        }

    }

}
