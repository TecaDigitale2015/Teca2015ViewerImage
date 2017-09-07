package mx.teca2015.viewerImage.interfacie;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import mx.imageviewer.schema.gestionelibro.ReadBook;
import mx.imageviewer.schema.gestioneopera.Opera;
import mx.imageviewer.schema.gestionepagina.ImageViewer;
import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;

/**
 * Questa interfaccia viene utilizzata per gestire l'interfaccia con l'archivio
 * che gestisce le immagini da visualizzare
 * 
 * @author Massimiliano Randazzo
 *
 */
public abstract class IImageViewer {

	private Logger log = Logger.getLogger(IImageViewer.class);

	protected boolean showOpere = false;

	/**
	 * Metodo utilizzato per ricavare le informazioni necessarie per disegnare
	 * la pagina html che contiene il visualizzatore, l'oggetto XML che viene
	 * generato verrà convertito in HTML tramite un foglio Xls
	 * 
	 * @return
	 */
	public abstract ImageViewer initPage(HttpServletRequest request,
			HttpServletResponse response, String serverName) throws ServletException, IOException;

	/**
	 * Metodo utilizzato per inviare l'immagine verso il client
	 * 
	 * @param request
	 *            Variabile utilizzata per gestire le informazioni provenienti
	 *            dal client
	 * @param response
	 *            Variabile utilizzata per gestire le informazoni verso il
	 *            client
	 */
	public abstract void showImage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;

	/**
	 * Metodo utilizzato per reperire le informazioni relative al libro da
	 * visualizare
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public abstract ReadBook readBook(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;

	/**
	 * Metodo utilizzato per reperire le informazioni relative al catalogo da
	 * visualizare
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public abstract Opera readCatalogo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;

	/**
	 * Metodo utilizzato per reperire le informazioni relative al catalogo da
	 * visualizare
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public abstract ImageViewer showCatalogo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;

	/**
	 * Metodo utilizzato per reperire le informazioni relative alle Stru
	 * dell'opera
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public abstract Opera readStru(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;

	public abstract String jSon(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;

	/**
	 * Metodo utilizzato per estrarre il foglio di stile da applicare per la
	 * generazione della pagina Web
	 * 
	 * @param serverName
	 *            Nome del server Name
	 * @return
	 */
	public String getFoglioXsl(String serverName) {
		String fileXsl = "";
		String all = null;
		String spec  = null;
		
		try {
			all = Configuration.getValue("imageViewer.ALL"
					+ (showOpere ? ".xslOpere" : ".xsl"));
			spec = Configuration.getValue(
					"imageViewer." + serverName
					+ (showOpere ? ".xslOpere" : ".xsl"));
			if (spec != null){
				fileXsl = spec;
			} else if (all != null){
				fileXsl = all;
			}
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		}
		return fileXsl;
	}

	public boolean isShowOpere() {
		return showOpere;
	}
}
