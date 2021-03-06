//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.07 at 03:53:46 PM CEST 
//


package mx.imageviewer.schema.gestioneopera;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://www.imageViewer.mx/schema/gestioneOpera}volume" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.imageViewer.mx/schema/gestioneOpera}libro" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="autore" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="genere" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute ref="{http://www.imageViewer.mx/schema/gestioneOpera}href"/>
 *       &lt;attribute name="titolo" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "volume",
    "libro"
})
@XmlRootElement(name = "libro")
public class Libro {

    protected List<Volume> volume;
    protected List<Libro> libro;
    @XmlAttribute(name = "autore", required = true)
    protected String autore;
    @XmlAttribute(name = "genere", required = true)
    protected String genere;
    @XmlAttribute(name = "href", namespace = "http://www.imageViewer.mx/schema/gestioneOpera")
    @XmlSchemaType(name = "anyURI")
    protected String href;
    @XmlAttribute(name = "titolo", required = true)
    protected String titolo;

    /**
     * Gets the value of the volume property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the volume property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVolume().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Volume }
     * 
     * 
     */
    public List<Volume> getVolume() {
        if (volume == null) {
            volume = new ArrayList<Volume>();
        }
        return this.volume;
    }

    /**
     * Gets the value of the libro property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the libro property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLibro().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Libro }
     * 
     * 
     */
    public List<Libro> getLibro() {
        if (libro == null) {
            libro = new ArrayList<Libro>();
        }
        return this.libro;
    }

    /**
     * Gets the value of the autore property.
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
     * Sets the value of the autore property.
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
     * Gets the value of the genere property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGenere() {
        return genere;
    }

    /**
     * Sets the value of the genere property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGenere(String value) {
        this.genere = value;
    }

    /**
     * Gets the value of the href property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHref() {
        return href;
    }

    /**
     * Sets the value of the href property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHref(String value) {
        this.href = value;
    }

    /**
     * Gets the value of the titolo property.
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
     * Sets the value of the titolo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitolo(String value) {
        this.titolo = value;
    }

}
