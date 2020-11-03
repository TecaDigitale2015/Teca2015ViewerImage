package mx.teca2015.viewerImage.implement;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;

import it.sbn.iccu.metaag1.Bib;
import it.sbn.iccu.metaag1.Gen;
import it.sbn.iccu.metaag1.Img;
import it.sbn.iccu.metaag1.Img.Altimg;
import it.sbn.iccu.metaag1.Metadigit;
import mx.imageviewer.schema.gestionepagina.ImageViewer.IIPImage;
import mx.imageviewer.schema.gestionepagina.ImageViewer.IIPImage.Pagina;
import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.solr.exception.SolrException;
import mx.randalf.xsd.exception.XsdException;
import mx.teca2015.viewerImage.imageViewer.imager.exception.ImageException;
import mx.teca2015.viewerImage.implement.readBook.ReadBook;
import mx.teca2015.viewerImage.implement.showImage.DatiImmagini;
import mx.teca2015.viewerImage.interfacie.exception.ImageViewerException;

public abstract class ImageViewerTecaDigitaleIIPImage extends ImageViewerTecaDigitaleIIIF {

	/**
	 * Variabile utilizzata per loggare l'appplicazione
	 */
	private Logger log = Logger.getLogger(ImageViewerTecaDigitaleIIPImage.class);

	/**
	 * Indica se l'immagine contiene Carte geografiche gestite dall'interfaccia iipImage
	 */
	protected boolean iipImage=false;

	public ImageViewerTecaDigitaleIIPImage() {
		log.debug("Costruttore");
	}

	protected IIPImage checkIIPImage(String idr) throws ServletException, NumberFormatException, ConfigurationException, SolrException, 
					SolrServerException, ImageViewerException, XsdException, ImageException{
		IIPImage iipImage = null;
		SolrDocumentList solrResponse = null;
		Metadigit mag = null;
		
		try {
			solrResponse = ImageViewerTecaDigitale.findSolr(idr);
			mag = ImageViewerTecaDigitale.readXml(solrResponse);

			if (mag.getImg() != null && 
					mag.getImg().size()>0){
				if (isIIPImage(mag.getImg().get(0))){
					this.iipImage = true;
					iipImage = new IIPImage();
					iipImage.setTitolo(getTitolo(mag.getBib()));
//					iipImage.setIdrPadre(value);
					for (int x=0; x<mag.getImg().size(); x++){
						if (isIIPImage(mag.getImg().get(x))){
							iipImage.getPagina().add(
									genPagina(solrResponse, mag.getImg().get(x), iipImage.getPagina().size(), mag.getGen()));
						}
					}
				}
			}
		} catch (NumberFormatException e) {
			throw e;
		} catch (ConfigurationException e) {
			throw e;
		} catch (SolrException e) {
			throw e;
		} catch (SolrServerException e) {
			throw e;
		} catch (ImageViewerException e) {
			throw e;
		} catch (XsdException e) {
			throw e;
		} catch (ImageException e) {
			throw e;
		}
		return iipImage;
	}

	private Pagina genPagina(SolrDocumentList solrResponse, Img img, int size, Gen gen) throws ImageException, ConfigurationException{
		Pagina pagina = null;
		DatiImmagini datiImmagini = null;
		Altimg altImg = null;
		
		try {
			pagina = new Pagina();

			if (checkUsage(img.getUsage())){
				datiImmagini = new DatiImmagini(solrResponse.get(0), 
						img.getFile().getHref(),
						ReadBook.getFormat(img, gen).getMime().value());
				pagina.setUrlPage(datiImmagini.getURL().toString());

				pagina.setLink((String) Configuration.getValueDefault("bncf.iipImage.link","javascript:changeImg")+"('"+size+"')");
				pagina.setKey("IMG."+size);
				pagina.setKeyPadre("IMG");
				pagina.setValue(img.getNomenclature());
			} else if (img.getAltimg() != null){
				for (int x=0; x<img.getAltimg().size(); x++){
					altImg = img.getAltimg().get(x);
					if (checkUsage(altImg.getUsage())){
						datiImmagini = new DatiImmagini(solrResponse.get(0), 
								altImg.getFile().getHref(),
								ReadBook.getFormat(altImg, gen).getMime().value());
						pagina.setUrlPage(datiImmagini.getURL().toString());

						pagina.setLink((String) Configuration.getValueDefault("bncf.iipImage.link","javascript:changeImg")+"('"+size+"')");
						pagina.setKey("IMG."+size);
						pagina.setKeyPadre("IMG");
						pagina.setValue(img.getNomenclature());
						break;
					}
				}
			}
		} catch (ImageException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return pagina;
	}

	private boolean isIIPImage(Img img){
		boolean isIIPImage= false;
		Altimg altImg = null;

		isIIPImage = checkUsage(img.getUsage());
		if (!isIIPImage && img.getAltimg()!= null){
			for (int x=0; x<img.getAltimg().size(); x++){
				altImg = img.getAltimg().get(x);
				isIIPImage = checkUsage(altImg.getUsage());
				if (isIIPImage){
					break;
				}
			}
		}
		return isIIPImage;
	}

	protected String getTitolo(Bib bib){
		String titolo = null;
		titolo = bib.getIdentifier().get(0).getContent().get(0);
		if (bib.getTitle() != null && bib.getTitle().size()>0){
			titolo = bib.getTitle().get(0).getContent().get(0);
		}
		return titolo;
	}
}
