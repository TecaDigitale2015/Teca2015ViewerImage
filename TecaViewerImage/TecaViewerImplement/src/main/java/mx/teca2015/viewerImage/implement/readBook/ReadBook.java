/**
 * 
 */
package mx.teca2015.viewerImage.implement.readBook;

import it.sbn.iccu.metaag1.Metadigit;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import mx.imageviewer.schema.gestionelibro.DatiBibliografici;
import mx.imageviewer.schema.gestionelibro.Immagini;
import mx.imageviewer.schema.gestionelibro.Immagini.Immagine;
import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;

import org.apache.log4j.Logger;
import org.purl.dc.elements._1.SimpleLiteral;

//import mx.configuration.Configuration;
//import mx.database.MsSqlException;
//import mx.database.table.Column;
//import mx.imageViewer.imager.ImageManager;
//import mx.imageViewer.imager.exception.ImageException;
//import mx.imageViewer.schema.gestionelibro.DatiBibliografici;
//import mx.imageViewer.schema.gestionelibro.Immagini;
//import mx.imageViewer.schema.gestionelibro.Immagini.Immagine;
//import mx.teca.archivi.arsbni.TblImg;
//import mx.teca.archivi.arsbni.view.ViewListaImg;
//import mx.teca.archivi.arsbni.view.ViewTbllegnotTblris;

/**
 * @author massi
 *
 */
public class ReadBook
{

	/**
	 * Variabile utilizzata per loggare l'appplicazione
	 */
	private Logger log = Logger.getLogger(ReadBook.class);

	/**
	 * Questa variabile viene utilizzata per gestire il colloqui da parte del client
	 */
	private HttpServletRequest request = null;

	/**
	 * Costruttore 
	 */
	public ReadBook(HttpServletRequest request)
	{
		this.request = request;
	}

//	public mx.imageviewer.schema.gestionelibro.ReadBook esegui(String risIdr, String usage)
	public mx.imageviewer.schema.gestionelibro.ReadBook esegui(String id, Metadigit mag, String usage, Integer numPage)
	{
		mx.imageviewer.schema.gestionelibro.ReadBook readBook = null;
		Immagini immagini = null;
//		ViewListaImg view = null;
//		ResultSet rs = null;
		String nomenclatura = null;
		int imgLength = 0;
		int imgWidth = 0;
		boolean isCostola = false;
//		ImageManager imageManager = null;
//		
		try {
//		{
//			view = new ViewListaImg(Configuration.getPool("teca"));
//			view.setCampoValue("relrisidr", risIdr);
//			view.setCampoValue("ImgUsage", usage);
//			view.getCampo("seq").setOrderBy(Column.ORDERBY_CRES, 1);
//			rs = view.startSelect();
//
			readBook = new mx.imageviewer.schema.gestionelibro.ReadBook();
			if (mag.getImg()!= null &&
					mag.getImg().size()>0){
//			if (view.getRecTot()>0)
//			{
				if (numPage!= null){
					if (readBook.getDatiBibliografici() == null)
					{
						readBook.setDatiBibliografici(initDatiBibliografici(mag));
					}
					if (immagini == null)
					{
						immagini = new Immagini();
						immagini.setNumImg(1);
						immagini.setIsCostola(false);
					}
					nomenclatura = null;
					imgLength = 0;
					imgWidth = 0;

					if (checkUsage(mag.getImg().get(numPage).getUsage(), usage)){
						nomenclatura = mag.getImg().get(numPage).getNomenclature();
						if (mag.getImg().get(numPage).getImageDimensions() != null){
							if (mag.getImg().get(numPage).getImageDimensions().getImagelength() != null &&
									mag.getImg().get(numPage).getImageDimensions().getImagewidth() != null){
								imgLength = mag.getImg().get(numPage).getImageDimensions().getImagelength().intValue();
								imgWidth = mag.getImg().get(numPage).getImageDimensions().getImagewidth().intValue();
							} else {
								imgLength = 800;
								imgWidth = 600;
							}
						} else {
							imgLength = 800;
							imgWidth = 600;
						}
					} else if (mag.getImg().get(numPage).getAltimg() != null){
						for (int y=0; y<mag.getImg().get(numPage).getAltimg().size(); y++){
							if (checkUsage(mag.getImg().get(numPage).getAltimg().get(y).getUsage(), usage)){
								nomenclatura = mag.getImg().get(numPage).getNomenclature();
								if (mag.getImg().get(numPage).getAltimg().get(y).getImageDimensions() != null){
									if (mag.getImg().get(numPage).getAltimg().get(y).getImageDimensions().getImagelength() != null &&
											mag.getImg().get(numPage).getAltimg().get(y).getImageDimensions().getImagewidth() != null){
										imgLength = mag.getImg().get(numPage).getAltimg().get(y).getImageDimensions().getImagelength().intValue();
										imgWidth = mag.getImg().get(numPage).getAltimg().get(y).getImageDimensions().getImagewidth().intValue();
									} else {
										imgLength = 800;
										imgWidth = 600;
									}
								} else {
									imgLength = 800;
									imgWidth = 600;
								}
							}
						}
					}

					if (nomenclatura != null){
						immagini.getImmagine().add(
	  							addImmagine( id,
	  									numPage, 
	  									isCostola, 
	  									nomenclatura, 
	  									imgLength, 
	  									imgWidth, 
	  									usage));
					}
				} else {
					for (int x=0; x<mag.getImg().size(); x++){
						if (readBook.getDatiBibliografici() == null)
						{
							readBook.setDatiBibliografici(initDatiBibliografici(mag));
						}
						if (immagini == null)
						{
							immagini = new Immagini();
							immagini.setNumImg(mag.getImg().size());
							immagini.setIsCostola(false);
						}
						isCostola = false;
						if (mag.getImg().get(x).getNomenclature().toLowerCase().contains("costola") ||
								mag.getImg().get(x).getNomenclature().toLowerCase().contains("dorso")){
							isCostola = true;
							immagini.setIsCostola(isCostola);
						}
						nomenclatura = null;
						imgLength = 0;
						imgWidth = 0;
	
						if (checkUsage(mag.getImg().get(x).getUsage(), usage)){
							nomenclatura = mag.getImg().get(x).getNomenclature();
							if (mag.getImg().get(x).getImageDimensions() != null){
								if (mag.getImg().get(x).getImageDimensions().getImagelength() != null &&
										mag.getImg().get(x).getImageDimensions().getImagewidth() != null){
									imgLength = mag.getImg().get(x).getImageDimensions().getImagelength().intValue();
									imgWidth = mag.getImg().get(x).getImageDimensions().getImagewidth().intValue();
								} else {
									imgLength = 800;
									imgWidth = 600;
								}
							} else {
								imgLength = 800;
								imgWidth = 600;
							}
						} else if (mag.getImg().get(x).getAltimg() != null){
							for (int y=0; y<mag.getImg().get(x).getAltimg().size(); y++){
								if (checkUsage(mag.getImg().get(x).getAltimg().get(y).getUsage(), usage)){
									nomenclatura = mag.getImg().get(x).getNomenclature();
									if (mag.getImg().get(x).getAltimg().get(y).getImageDimensions() != null){
										if (mag.getImg().get(x).getAltimg().get(y).getImageDimensions().getImagelength() != null &&
												mag.getImg().get(x).getAltimg().get(y).getImageDimensions().getImagewidth() != null){
											imgLength = mag.getImg().get(x).getAltimg().get(y).getImageDimensions().getImagelength().intValue();
											imgWidth = mag.getImg().get(x).getAltimg().get(y).getImageDimensions().getImagewidth().intValue();
										} else {
											imgLength = 800;
											imgWidth = 600;
										}
									} else {
										imgLength = 800;
										imgWidth = 600;
									}
								}
							}
						}
	
						if (nomenclatura != null){
							immagini.getImmagine().add(
		  							addImmagine( id,
		  									x, 
		  									isCostola, 
		  									nomenclatura, 
		  									imgLength, 
		  									imgWidth, 
		  									usage));
						}
					}
				}
				if (immagini != null){
					readBook.setImmagini(immagini);
				}
//			}else{
//				try {
//					int iUsage =0;
//					iUsage = Integer.valueOf(usage).intValue();
//					if (iUsage>1){
//						iUsage--;
//						usage = Integer.toString(iUsage);
//						readBook = esegui(risIdr, usage);
//					}
//				} catch (NumberFormatException e) {
//				}
			}
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		}
//		}
//		catch (MsSqlException e)
//		{
//			log.error(e);
//		}
//		catch (SQLException e)
//		{
//			log.error(e);
//		}
//		finally
//		{
//			try
//			{
//				if (rs != null)
//					rs.close();
//				if (view != null)
//					view.stopSelect();
//			}
//			catch (SQLException e)
//			{
//				log.error(e);
//			}
//		}
		return readBook;
	}

	public static boolean checkUsage(List<String>usages, String usage){
		boolean result = false;
		for (int x=0; x<usages.size(); x++){
			if (usages.get(x).equalsIgnoreCase(usage)){
				result = true;
				break;
			}
		}
		return result;
	}
	
//	private void updImgConv(int imgLengthConv, int imgWidthConv, String idTblImg) throws MsSqlException{
//		TblImg tblimg = null;
//		
//		tblimg = new TblImg(Configuration.getPool("teca"));
//		tblimg.setCampoValue("imgLengthConv", imgLengthConv);
//		tblimg.setCampoValue("imgWidthConv", imgWidthConv);
//		tblimg.setCampoValue("idTblImg", idTblImg);
//		tblimg.update();
//	}

	/**
	 * Questo metodo viene utilizzato per la definizione del nodo Immagine
	 * 
	 * @param id Identificativo dell'immagine
	 * @param sequenza Numero di sequenza dell'immagine
	 * @param nomenclatura Nomenclatura dell'imamgine
	 * @param altezza Altezza dell'imamgine
	 * @param larghezzaLarghezza dell'immgine
	 * @return
	 */
	private Immagine addImmagine(String id, int sequenza, Boolean visPaginaDoppia, String nomenclatura, 
			int altezza, int larghezza, String usage)
	{
		Immagine immagine = null;

		immagine = new Immagine();
		immagine.setID(id);
		immagine.setSequenza(sequenza);
		immagine.setVisPaginaDoppia(visPaginaDoppia);
		immagine.setNomenclatura(nomenclatura);
		immagine.setAltezza(altezza);
		immagine.setLarghezza(larghezza);
		immagine.setUsage(usage);
		return immagine;
	}

	/**
	 * Funzione utilizzata per reperire le informazioni Bibliografiche dell'opera
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
//	private DatiBibliografici initDatiBibliografici(String risidr) throws SQLException
	private DatiBibliografici initDatiBibliografici(Metadigit mag) throws ConfigurationException
	{
		DatiBibliografici datiBibliografici = null;
		String dati = "";
		String urlDatiBibliografici = null;
//		int pos = 0;
//		String key = "";
//		ResultSet rs = null;
//		ViewTbllegnotTblris view = null;
//
		try
		{
//			view = new ViewTbllegnotTblris(Configuration.getPool("teca"));
//			view.setCampoValue("risidr", risidr);
//			rs = view.startSelect();
			datiBibliografici = new DatiBibliografici();
//
		if (mag.getBib() != null){
//			if (rs.next()){

			dati = read(mag.getBib().getCreator());
			if (!dati.equals("")){
				datiBibliografici.setAutore(dati);
			}
//			if (rs.getString("autore") != null && 
//					!rs.getString("autore").trim().equals(""))
//				datiBibliografici.setAutore(rs.getString("autore").trim());

			dati = read(mag.getBib().getTitle());
			if (!dati.equals("")){
				datiBibliografici.setAutore(dati);
			}
//			if (rs.getString("titolo") != null && 
//					!rs.getString("titolo").trim().equals(""))
//				datiBibliografici.setTitolo(rs.getString("titolo").trim());

			dati = read(mag.getBib().getPublisher());
			if (!dati.equals("")){
				datiBibliografici.setAutore(dati);
			}
//			if (rs.getString("notePubblicazione") != null && 
//					!rs.getString("notePubblicazione").trim().equals(""))
//				datiBibliografici.setPubblicazione(rs.getString("notePubblicazione").trim());
//

			if (mag.getBib().getPiece() != null){
				if (mag.getBib().getPiece().getYear() != null &&
						mag.getBib().getPiece().getIssue() != null){
					datiBibliografici.setUnitaFisica(mag.getBib().getPiece().getYear().trim()+
							" "+
							mag.getBib().getPiece().getIssue().trim());
				} else if (mag.getBib().getPiece().getPartNumber() != null &&
						mag.getBib().getPiece().getPartName() != null){
					datiBibliografici.setUnitaFisica(mag.getBib().getPiece().getPartNumber().intValue()+
							" "+
							mag.getBib().getPiece().getPartName().trim());
				}
			}
//			if ((rs.getString("pieceGr") != null && 
//					!rs.getString("pieceGr").trim().equals("")) &&
//					(rs.getString("PieceDt") != null && 
//							!rs.getString("PieceDt").trim().equals("")))
//				datiBibliografici.setUnitaFisica(rs.getString("pieceGr").trim()+" "+rs.getString("PieceDt").trim());
//
				urlDatiBibliografici = (String)Configuration.getValueDefault("imageViewer."+request.getServerName()+".urlDatiBibliografici", 
						Configuration.getValueDefault("imageViewer.ALL.urlDatiBibliografici", ""));

			if (urlDatiBibliografici != null &&
					!urlDatiBibliografici.trim().equals(""))
			{
				urlDatiBibliografici = urlDatiBibliografici.replace("<serverName>", request.getServerName());
//
//				pos = urlDatiBibliografici.indexOf("<");
//				while(pos>-1)
//				{
//					key = urlDatiBibliografici.substring(pos+1);
//					pos = key.indexOf(">");
//					if (pos>-1)
//						key = key.substring(0,pos);
//					try
//					{
//						rs.findColumn(key);
//						urlDatiBibliografici = urlDatiBibliografici.replace("<"+key+">", rs.getString(key));
//					}
//					catch (SQLException e)
//					{
//						urlDatiBibliografici = urlDatiBibliografici.replace("<"+key+">", "");
//					}
//					pos = urlDatiBibliografici.indexOf("<");
//				}
				datiBibliografici.setUrlDatiBibliografici(urlDatiBibliografici);
			}
		}
//		}
		} catch (ConfigurationException e) {
			log.error(e.getMessage(),e);
			throw e;
		}
//		catch (SQLException e)
//		{
//			log.error(e);
//			throw e;
//		}finally{
//			if (rs != null){
//				rs.close();
//			}
//			if (view != null){
//				view.stopSelect();
//			}
//		}
//
		return datiBibliografici;
	}

	private String read(List<SimpleLiteral> value){
		String result = "";
		if (value != null &&
				value.size()>0){
			for (int x=0; x<value.size(); x++){
				if (value.get(x).getContent() != null &&
						value.get(x).getContent().size()>0){
					for (int y=0; y<value.get(x).getContent().size(); y++){
						if (value.get(x).getContent().get(y) != null &&
								! value.get(x).getContent().get(y).trim().equals("")){
							result += (result.equals("")?"":"<br/>");
							result += value.get(x).getContent().get(y).trim();
						}
					}
				}
			}
		}
		return result;
	}

}
