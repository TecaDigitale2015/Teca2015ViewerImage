//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.19 alle 11:04:31 AM CET 
//


package mx.imageviewer.schema.gestionelibro;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mx.imageviewer.schema.gestionelibro package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Nomenclatura_QNAME = new QName("http://www.imageViewer.mx/schema/gestioneLibro", "nomenclatura");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mx.imageviewer.schema.gestionelibro
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Stru }
     * 
     */
    public Stru createStru() {
        return new Stru();
    }

    /**
     * Create an instance of {@link Immagini }
     * 
     */
    public Immagini createImmagini() {
        return new Immagini();
    }

    /**
     * Create an instance of {@link Stru.Immagine }
     * 
     */
    public Stru.Immagine createStruImmagine() {
        return new Stru.Immagine();
    }

    /**
     * Create an instance of {@link DatiBibliografici }
     * 
     */
    public DatiBibliografici createDatiBibliografici() {
        return new DatiBibliografici();
    }

    /**
     * Create an instance of {@link ReadBook }
     * 
     */
    public ReadBook createReadBook() {
        return new ReadBook();
    }

    /**
     * Create an instance of {@link Immagini.Immagine }
     * 
     */
    public Immagini.Immagine createImmaginiImmagine() {
        return new Immagini.Immagine();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.imageViewer.mx/schema/gestioneLibro", name = "nomenclatura")
    public JAXBElement<String> createNomenclatura(String value) {
        return new JAXBElement<String>(_Nomenclatura_QNAME, String.class, null, value);
    }

}
