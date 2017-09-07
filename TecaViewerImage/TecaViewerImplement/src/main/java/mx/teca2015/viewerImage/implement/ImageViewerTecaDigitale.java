/**
 * 
 */
package mx.teca2015.viewerImage.implement;

import it.sbn.iccu.metaag1.Metadigit;
import it.sbn.iccu.metaag1.Stru;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.imageviewer.schema.gestionelibro.ReadBook;
import mx.imageviewer.schema.gestioneopera.Libro;
import mx.imageviewer.schema.gestioneopera.Opera;
import mx.imageviewer.schema.gestioneopera.Volume;
import mx.imageviewer.schema.gestionepagina.ImageViewer;
import mx.imageviewer.schema.gestionepagina.ImageViewer.IIPImage;
import mx.imageviewer.schema.gestionepagina.ImageViewer.ShowNavigatore;
import mx.imageviewer.schema.gestionepagina.ImageViewer.ShowStru;
import mx.imageviewer.schema.gestionepagina.ImageViewer.Xlimage;
import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.mag.MagXsd;
import mx.randalf.solr.FindDocument;
import mx.randalf.solr.exception.SolrException;
import mx.randalf.xsd.exception.XsdException;
import mx.teca2015.tecaUtility.solr.item.ItemTeca;
import mx.teca2015.viewerImage.implement.showImage.ShowImage;
import mx.teca2015.viewerImage.interfacie.exception.ImageViewerException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;


/**
 * Questa classe viene utilizzata per gestire l'interfaccia verso di 
 * Database Teca per la visualizzazione delle immagini
 * 
 * @author Massimiliano Randazzo
 *
 */
public class ImageViewerTecaDigitale extends ImageViewerTecaDigitaleIIPImage
{

	/**
	 * Variabile utilizzata per loggare l'appplicazione
	 */
	private static  Logger log = Logger.getLogger(ImageViewerTecaDigitale.class);

	/**
	 * Indica se l'immagine contiene Carte geografiche gestire dall'interfaccia xlimage
	 */
	private boolean xlimage=false;

	/**
	 * Costruttore
	 */
	public ImageViewerTecaDigitale()
	{
		log.debug("Costruttore");
	}

	private static QueryResponse findSolr(String query, int start, int rows) throws 
				NumberFormatException,ConfigurationException, SolrException, 
				SolrServerException{
		FindDocument find = null;
		QueryResponse response = null;

		try {
			find = new FindDocument(Configuration.getValue("solr.URL"),
					Boolean.parseBoolean(Configuration
							.getValue("solr.Cloud")),
					Configuration
							.getValue("solr.collection"),
					Integer.parseInt(Configuration
							.getValue("solr.connectionTimeOut")),
					Integer.parseInt(Configuration
							.getValue("solr.clientTimeOut")));
			if (query ==  null){
				query = "*:*";
			}
			response = find.find(query, 
					start, 
					rows);
		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (SolrException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (SolrServerException e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				if (find != null){
					find.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				throw new SolrServerException(e.getMessage(),e);
			}
		} 
		return response;
	}
	
	/**
	 * @see mx.imageviewer.servlet.interfacie.IImageViewer#initPage(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public ImageViewer initPage(HttpServletRequest request, HttpServletResponse response, String serverName) throws ServletException, IOException
	{
		ImageViewer imageViewer = null;
		String idr = "";
		Metadigit mag = null;
		SolrDocumentList solrResponse = null;
		IIPImage iipImage = null;
		Xlimage xlimage = null;
		try
		{
			log.debug("initPage");
			imageViewer = new ImageViewer();
			
			if (request.getParameter("id") != null){
				solrResponse = findSolr(request.getParameter("id"));
				mag = readXml(solrResponse);
//						objectIdentifier = (String) solrResponse.get(0).get(ItemTeca.ID);
				imageViewer.setTitolo("Visualizzatore Immagini TecaDigitale ver. 5.0");
//						isCollezione(request.getParameter("idr"), imageViewer, serverName);
				isCollezione(solrResponse, imageViewer, serverName);
//						isStru(request.getParameter("idr"), imageViewer);
				isStru(mag, solrResponse, imageViewer);

				if (request.getParameter("id")!= null){
//						if (request.getParameter("idr")!= null){
//							idr = getBookIdr(request.getParameter("idr"));
					idr = getBookIdr(solrResponse);
					imageViewer.setIdr(idr);
					iipImage = checkIIPImage(idr);
					xlimage = checkXlimage(idr);
					if (iipImage!= null){
						imageViewer.setIIPImage(iipImage);
					} else if (xlimage != null){
						imageViewer.setXlimage(xlimage);
					}
				}
			} else {
				throw new ImageViewerException("Non e' stato indicato un filtro di ricerca valido");
			}
		} catch (ImageViewerException e) {
			imageViewer.setMsgError(e.getMessage());
			log.error(e.getMessage(), e);
		} catch (NumberFormatException e) {
			imageViewer.setMsgError(e.getMessage());
			log.error(e.getMessage(), e);
		} catch (ConfigurationException e) {
			imageViewer.setMsgError(e.getMessage());
			log.error(e.getMessage(), e);
		} catch (SolrException e) {
			imageViewer.setMsgError(e.getMessage());
			log.error(e.getMessage(), e);
		} catch (SolrServerException e) {
			imageViewer.setMsgError(e.getMessage());
			log.error(e.getMessage(), e);
		} catch (XsdException e) {
			imageViewer.setMsgError(e.getMessage());
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			imageViewer.setMsgError(e.getMessage());
			log.error(e.getMessage(), e);
		}
		return imageViewer;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws NumberFormatException
	 * @throws ConfigurationException
	 * @throws SolrException
	 * @throws SolrServerException
	 * @throws ImageViewerException
	 */
	public static SolrDocumentList findSolr(String id) throws NumberFormatException, ConfigurationException, SolrException, 
				SolrServerException, ImageViewerException{
		QueryResponse qr = null;
		SolrDocumentList solrResponse = null;

		try {
			qr = findSolr(ItemTeca.ID+":"+id, 0, 0);
			if (qr.getResponse() != null &&
					qr.getResponse().get("response")!=null){
				solrResponse = (SolrDocumentList) qr.getResponse().get("response");
			} else {
				throw new ImageViewerException("La ricerca non ha portato ad alcun risultato");
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
		}
		return solrResponse;
	}

	/**
	 * 
	 * @param solrResponse
	 * @return
	 * @throws XsdException
	 * @throws ImageViewerException
	 */
	@SuppressWarnings("unchecked")
	public static Metadigit readXml(SolrDocumentList solrResponse) throws XsdException, ImageViewerException{
		MagXsd magXsd = null;
		Metadigit mag = null;
		ByteArrayInputStream bais = null;

		try {
			if (solrResponse.getNumFound()>0){
				
				magXsd = new MagXsd();
				bais = new ByteArrayInputStream(((ArrayList<String>) solrResponse.get(0).get(ItemTeca.XML)).get(0).getBytes());
				mag = magXsd.read(bais);

			} else {
				throw new ImageViewerException("La ricerca non ha portato ad alcun risultato");
			}
		} catch (XsdException e) {
			throw e;
		} catch (ImageViewerException e) {
			throw e;
		}
		return mag;
	}

	private Xlimage checkXlimage(String idr) throws ServletException{
		Xlimage xlimage = null;
		
		// TODO: da implementare per la bncf
//		Pagina pagina = null;
//		Tblris table = null;
//		ResultSet rs2 = null;
//		ViewListaImg view = null;
//		ResultSet rs = null;
//
//		try {
//			table = new Tblris(Configuration.getPool("teca"));
//			table.setCampoValue("risidr", idr);
//			rs2 = table.startSelect();
//			view = new ViewListaImg(Configuration.getPool("teca"));
//			view.setCampoValue("relrisidr", idr);
//			view.setCampoValue("ImgUsage", "5");
//			view.getCampo("seq").setOrderBy(Column.ORDERBY_CRES, 1);
//			rs = view.startSelect();
//			while(rs.next()){
//				this.xlimage=true;
//				if (xlimage == null){
//					xlimage = new Xlimage();
//					if (rs2.next()){
//						xlimage.setTitolo(rs2.getString("risNotaPub"));
//					}
//				}
//				pagina = new Pagina();
//
//				pagina.setUrlPage((String)Configuration.get("bncf.xlimage.urlPage", "http://opac.bncf.firenze.sbn.it/php/xlimage/XLImageRV.php?&amp;xsize=500&amp;image=")+
//						rs.getString("imgPathName"));
//				pagina.setLink((String) Configuration.get("bncf.xlimage.link","javascript:changeImg")+"('"+xlimage.getPagina().size()+"')");
//				pagina.setKey("IMG."+xlimage.getPagina().size());
//				pagina.setKeyPadre("IMG");
//				pagina.setValue(rs.getString("nota"));
//				xlimage.getPagina().add(pagina);
//			}
//		} catch (SQLException e) {
//			log.error(e);
//			throw new ServletException(e);
//		} finally {
//			try {
//				if (rs != null){
//					rs.close();
//				}
//				if (rs2 != null){
//					rs2.close();
//				}
//				if (view != null){
//					view.stopSelect();
//				}
//				if (table != null){
//					table.stopSelect();
//				}
//			} catch (SQLException e) {
//				log.error(e);
//				throw new ServletException(e);
//			}
//		}
		return xlimage;
	}

	/**
	 * Metodo utilizzato per ricavare l'identificativo del Libro
	 * @param risIdr
	 * @return
	 * @throws ImageViewerException 
	 */
	private String getBookIdr(SolrDocumentList solrResponse) throws ImageViewerException{
		String result = null;
		
		// TODO: da implementare tutte le casistiche in cui si visualizzano i dati nelle pagine interne
		result = (String)solrResponse.get(0).get(ItemTeca.ID);
		
//		ConnectionPool cp = null;
//		MsSqlPool msp = null;
//		ResultSet rs = null;
//		String newRisIdr = null;
//		boolean trovato = false;
//		boolean lavorato = false;
//
//		try {
//			cp = Configuration.getPool("teca");
//			msp = cp.getConn();
//			rs = msp.StartSelect("Select TBLRELRIS.tiporelid, " +
//					                    "TBLRIS.* " +
//					               "from TBLRELRIS, " +
//					                    "TBLRIS " +
//					              "where TBLRELRIS.relrisidrarrivo='"+risIdr+"' and " +
//					              		"TBLRIS.risidr=TBLRELRIS.relrisidrpartenza " +
//					           "order by TBLRELRIS.relrissequenza desc");
//			while (rs.next()){
//				lavorato=true;
//				if (rs.getString("rislivello").equals("P")){
//					trovato = true;
//					result = risIdr;
//					break;
//				}else if (newRisIdr == null){
//					newRisIdr = rs.getString("risIdr");
//				}
//			}
//			
//			if (!lavorato){
//				throw new ImageViewerException("Non risultano presenti collegamenti con le immagini");
//			}
//		} catch (SQLException e) {
//			log.error(e);
//			throw new ImageViewerException(e.getMessage(), e);
//		} catch (ImageViewerException e) {
//			throw e;
//		} catch (Exception e) {
//			log.error(e);
//			throw new ImageViewerException(e.getMessage(), e);
//		}finally{
//			try {
//				if (rs != null){
//					rs.close();
//				}
//				if (msp != null){
//					msp.StopSelect();
//					cp.releaseConn(msp);
//				}
//			} catch (SQLException e) {
//				log.error(e);
//				throw new ImageViewerException(e.getMessage(), e);
//			}finally{
//				if (!trovato){
//					result = getBookIdr(newRisIdr);
//				}
//			}
//		}
		return result;
	}

	private void isStru(Metadigit mag,SolrDocumentList solrResponse, ImageViewer imageViewer){
		
//		ConnectionPool cp = null;
//		TblRelRis tblRelRis = null;
//		ResultSet rs = null;
		ShowStru show =null;
		boolean costola = false;
//
//		try {
//			costola = isCostola(risIdr);
			costola = isCostola(mag);
//			cp = Configuration.getPool("teca");
//			tblRelRis = new TblRelRis(cp);
//			tblRelRis.setCampoValue("relRisidrArrivo", risIdr);
//			tblRelRis.setCampoValue("tipoRelId", 3);
//			rs = tblRelRis.startSelect();
//			if (rs.next()){
		if (mag.getStru()!= null && mag.getStru().size()>0){
				show = new ShowStru();
				show.setValue(true);
				show.setIdr((String)solrResponse.get(0).get(ItemTeca.ID));
				show.setCostola(costola);
				imageViewer.setShowStru(show);
		}
//			}
//		} catch (SQLException e) {
//			log.error(e);
//		} finally {
//			try {
//				if (rs != null){
//					rs.close();
//				}
//				if (tblRelRis != null){
//					tblRelRis.stopSelect();
//				}
//			} catch (SQLException e) {
//				log.error(e);
//			}
//		}
	}

	private boolean isCostola(Metadigit mag){
//		ViewListaImg view = null;
//		ResultSet rs = null;
		boolean result = false;

		if (mag.getImg()!=null && mag.getImg().size()>0){
			if (mag.getImg().get(0).getNomenclature().toLowerCase().contains("costola") ||
					mag.getImg().get(0).getNomenclature().toLowerCase().contains("dorso")){
				result=true;
			}
		}
//		try
//		{
//			view = new ViewListaImg(Configuration.getPool("teca"));
//			view.setCampoValue("relrisidr", risIdr);
//			view.addWhere(" AND (LOWER(TBLRIS.RISNOTAPUB) like '%costola%' OR LOWER(TBLRIS.RISNOTAPUB) like '%dorso%')");
//			rs = view.startSelect();
//			result = rs.next();
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
		return result;
	}

	/**
	 * Metodo utilizzato per ricavare se l'opera in oggetto è corrispondente ad una collezione
	 * 
	 * @param ris
	 * @return
	 * @throws ImageViewerException 
	 */
	@SuppressWarnings("unchecked")
	private void isCollezione(SolrDocumentList solrResponse, ImageViewer imageViewer, String serverName) throws ImageViewerException{
		
			
//		ConnectionPool cp = null;
//		Tblris tblRis = null;
//		TblRelRis tblRelRis = null;
//		ResultSet rs = null;
//		ResultSet rs2 = null;
		ShowNavigatore show = null;
		
//		
		try {
//			cp = Configuration.getPool("teca");
//			tblRis = new Tblris(cp);
//			tblRis.setCampoValue("risidr", risIdr);
//			rs = tblRis.startSelect();
//			if (rs.next()){
//				if (rs.getString("rislivello").equals("C")){
			if (((ArrayList<String>)solrResponse.get(0).get(ItemTeca.TIPOOGGETTO+"_show")).get(0).equals(ItemTeca.TIPOOGGETTO_COLLANA)){
				if (((String) Configuration.getValueDefault("imageViewer." + serverName + ".externalVolumi",
						Configuration.getValueDefault("imageViewer.ALL.externalVolumi", "false"))).equalsIgnoreCase("flase")){
					show = new ShowNavigatore();
					show.setValue(true);
					show.setIdr((String)solrResponse.get(0).get(ItemTeca.ID));
					imageViewer.setShowNavigatore(show);
				} else {
					showOpere = true;
				}
			}
//				}else if (rs.getString("rislivello").equals("S")){
//					if (((String) Configuration.get("imageViewer." + serverName + ".externalVolumi",
//						     Configuration.get("imageViewer.ALL.externalVolumi", "false"))).equalsIgnoreCase("flase")){
//						tblRelRis = new TblRelRis(cp);
//						tblRelRis.setCampoValue("relRisidrPartenza", risIdr);
//						tblRelRis.setCampoValue("tipoRelId", 2);
//						rs2 = tblRelRis.startSelect();
//						if (rs2.next()){
//							show = new ShowNavigatore();
//							show.setValue(true);
//							show.setIdr(rs2.getString("relRisidrArrivo"));
//							imageViewer.setShowNavigatore(show);
//						}
//					}
//				}
//			}else{
//				throw new ImageViewerException("Non risulta presente l'oggetto in base dati");
//			}
		} catch (ConfigurationException e) {
			throw new ImageViewerException(e.getMessage(), e);
		}
//		} catch (SQLException e) {
//			log.error(e);
//		} catch (ImageViewerException e) {
//			throw e;
//		} finally {
//			try {
//				if (rs != null){
//					rs.close();
//				}
//				if (rs2 != null){
//					rs2.close();
//				}
//				if (tblRelRis != null){
//					tblRelRis.stopSelect();
//				}
//				if (tblRis != null){
//					tblRis.stopSelect();
//				}
//			} catch (SQLException e) {
//				log.error(e);
//			}
//		}
	}

	/**
	 * 
	 * @see mx.imageviewer.servlet.interfacie.IImageViewer#showImage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void showImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
//		String key = "";
//		String[] values = null;
//
//		log.debug("showImage");
//		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();)
//		{
//			key = e.nextElement();
//			values = request.getParameterValues(key);
//			for (int x=0; x<values.length; x++)
//				log.debug(key+": "+values[x]);
//		}
		ShowImage.showImage(request.getParameter("idr"), request, response);
	}

//	public void showImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//	{
//		String key = "";
//		String[] values = null;
//		String fileImg = "";
//		File f = null;
//		FileImageInputStream fiis = null;
//		int len = 0;
//		byte[] bbuf = new byte[1000000]; 
//
//		try
//		{
//			log.debug("showImage");
//			for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();)
//			{
//				key = e.nextElement();
//				values = request.getParameterValues(key);
//				for (int x=0; x<values.length; x++)
//					log.debug(key+": "+values[x]);
//			}
//			response.setCharacterEncoding("UTF-8");
//			response.setContentType("image/jpeg");
//			
//			fileImg= "/Volumes/HDDSORGENTI/Backup/discoc/Progetti/Tivoli/Tivoli DVD/Immagini/P000001_SIGI_1691_D1_VIII_12/200";
//			fileImg += File.separator;
//			fileImg += "TIV_"+ConvertText.mettiZeri(request.getParameter("sequence"), 4)+".jpg";
//
//			log.debug("fileImg: "+fileImg);
//			f = new File(fileImg);
//			fiis = new FileImageInputStream(f);
//			while ((len = fiis.read(bbuf))>-1)
//			{
//				response.getOutputStream().write(bbuf,0,len);
//			}
//		}
//		catch (IOException e)
//		{
//			log.error(e);
//			throw e;
//		}
//		finally
//		{
//			if (fiis != null)
//				fiis.close();
//		}
//	}

	/**
	 * 
	 * @see mx.imageviewer.servlet.interfacie.IImageViewer#readBook(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ReadBook readBook(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		ReadBook myReadBook = null;
		mx.teca2015.viewerImage.implement.readBook.ReadBook readBook = null;
		String usage = "";
		Integer numPage = null;
		SolrDocumentList solrResponse = null;
		Metadigit mag = null;

//		readBook = new mx.imageViewer.implement.readBook.ReadBook(request);
		try {
			readBook = new mx.teca2015.viewerImage.implement.readBook.ReadBook(request);

			if (request.getParameter("usage") != null)
				usage = request.getParameter("usage");
			else
				usage = (String)Configuration.getValueDefault("imageViewer."+request.getServerName()+".usageImageDefault", 
						Configuration.getValueDefault("imageViewer.ALL.usageImageDefault", "3"));

			if (request.getParameter("numPage")!= null){
				numPage = new Integer(request.getParameter("numPage"));
			}
			if (request.getParameter("idr") != null){
				solrResponse = findSolr(request.getParameter("idr"));
				mag = readXml(solrResponse);
				myReadBook = readBook.esegui(request.getParameter("idr"), mag, usage, numPage);
			}
		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		} catch (SolrException e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		} catch (SolrServerException e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		} catch (ImageViewerException e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		} catch (XsdException e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		}
		return myReadBook;
	}

	@Override
	public String getFoglioXsl(String serverName) {
		String foglioXsl = null;
		
		try {
			foglioXsl = super.getFoglioXsl(serverName);
			if (xlimage){
				foglioXsl = (String) Configuration.getValueDefault("bncf.xlimage.xsl", (xlimage?"true":"false"));
			}
			if (iipImage){
				foglioXsl = (String) Configuration.getValueDefault("bncf.iipImage.xsl", (iipImage?"true":"false"));
			}
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		}
		return foglioXsl;
	}

	@Override
	public Opera readCatalogo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		return readCatalogo(request, response, true);
	}

	
	public Opera readCatalogo(HttpServletRequest request,
			HttpServletResponse response, boolean initBook) throws ServletException, IOException {
		Opera opera = null;
//		ConnectionPool cp = null;
//		MsSqlPool msp = null;
//		ResultSet rs = null;
//		Libro libro = null;
//		Libro gruppo = null;
//		Volume dettaglio = null;
//		String titolo = "";
//
//		try {
//			cp = Configuration.getPool("teca");
//			msp = cp.getConn();
//			rs = msp.StartSelect("SELECT TBLLEGNOT.tmpautore, " +
//					                    "TBLLEGNOT.tmptitolo, " +
//					                    "TBLLEGNOTRIS.risidr, " +
//					                    "TBLLEGNOTRIS.piecegr, " +
//					                    "TBLLEGNOTRIS.piecedt, " +
//					                    "TBLLEGNOTRIS.piecein " +
//                                   "FROM TBLRELRIS, " +
//                                        "TBLLEGNOTRIS, " +
//                                        "TBLLEGNOT " +
//                                  "WHERE TBLRELRIS.relrisidrarrivo = '"+request.getParameter("idr")+"' AND " +
//                                  		"TBLRELRIS.relrisidrpartenza = TBLLEGNOTRIS.risidr AND " +
//                                  		"TBLLEGNOTRIS.id_tbllegnot = TBLLEGNOT.id_tbllegnot " +
//                               "ORDER BY TBLLEGNOTRIS.piecein;");
//			while (rs.next()){
//				if (opera==null){
//					opera = new Opera();
//				}
//				if (rs.getString("tmptitolo")== null){
//					titolo = "Titolo";
//				}else{
//					titolo = rs.getString("tmptitolo");
//				}
//				if (libro == null){
//					libro = new Libro();
//
//					if (rs.getString("tmpautore") != null){
//						libro.setAutore(rs.getString("tmpautore"));
//					}
//					libro.setTitolo(titolo);
//				} else if ((libro.getTitolo() != null && !libro.getTitolo().equals(titolo)) ||
//						   (libro.getAutore() != null && !libro.getAutore().equals(rs.getString("tmpautore")))){
//
//					if (gruppo != null) {
//						libro.getLibro().add(gruppo);
//					}
//
//					opera.getLibro().add(libro);
//					libro = new Libro();
//
//					if (rs.getString("tmpautore") != null){
//						libro.setAutore(rs.getString("tmpautore"));
//					}
//					libro.setTitolo(titolo);
//					gruppo = null;
//				}
//				
//				if (gruppo == null){
//					gruppo = new Libro();
//					gruppo.setTitolo(rs.getString("piecegr"));
//				} else if (!gruppo.getTitolo().equals(rs.getString("piecegr"))) {
//					libro.getLibro().add(gruppo);
//					gruppo = new Libro();
//					gruppo.setTitolo(rs.getString("piecegr"));
//				}
//				
//				dettaglio = new Volume();
//				dettaglio.setValue(rs.getString("piecedt"));
//				if (initBook){
//					dettaglio.setHref("javascript:parent.initBook('"+rs.getString("risidr")+"');");
//				}else{
//					dettaglio.setHref(rs.getString("risidr"));
//				}
//				gruppo.getVolume().add(dettaglio);
//			}
//			if (libro != null){
//				if (gruppo != null) {
//					libro.getLibro().add(gruppo);
//				}
//				opera.getLibro().add(libro);
//			}
//		} catch (SQLException e) {
//			log.error(e);
//			throw new ServletException(e.getMessage(), e);
//		} catch (ServletException e) {
//			throw e;
//		} catch (Exception e) {
//			log.error(e);
//			throw new ServletException(e.getMessage(), e);
//		}finally{
//			try {
//				if (rs != null){
//					rs.close();
//				}
//				if (msp != null){
//					msp.StopSelect();
//					cp.releaseConn(msp);
//				}
//			} catch (SQLException e) {
//				log.error(e);
//				throw new ServletException(e.getMessage(), e);
//			}finally{
////				if (opera == null){
////					result = getBookIdr(newRisIdr);
////				}
//			}
//		}
		return opera;
	}

	@Override
	public ImageViewer showCatalogo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ImageViewer imageViewer = null;
//		Opera opera = null;
//		String idr = "";
//
//		try
//		{
//			showOpere=true;
//			log.debug("initPage");
//			imageViewer = new ImageViewer();
//			imageViewer.setTitolo("Visualizzatore Immagini TecaDigitale ver. 4.1.0");
//
//			if (request.getParameter("idr")!= null){
//				idr = getBookIdr(request.getParameter("idr"));
//				imageViewer.setIdr(idr);
//			}
//			opera = readCatalogo(request, response, false);
//			for (Libro libro : opera.getLibro()){
//				imageViewer.getLibro().add(libro);
//			}
//		} catch (ImageViewerException e) {
//			imageViewer.setMsgError(e.getMessage());
//		}
		return imageViewer;
	}

	/**
	 * 
	 */
	@Override
	public Opera readStru(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Opera opera = null;
		Metadigit mag = null;
		boolean costola = false;
		SolrDocumentList solrResponse = null;
		Libro  libro = null;

		try {
			//		Vector<String[]> stru = null;
//
//		stru = readStru(request.getParameter("idr"), "3");
//		if (stru != null){
			if (request.getParameter("idr") != null){
				solrResponse = findSolr(request.getParameter("idr"));
				mag = readXml(solrResponse);
				opera = new Opera();
//			costola = isCostola(request.getParameter("idr"));
				costola = isCostola(mag);
//			for (int x=0; x<stru.size(); x++){
				for (int x=0; x<mag.getStru().size(); x++){
					libro = new Libro();
					libro.setTitolo(mag.getStru().get(x).getNomenclature());
					checkStru(libro, mag.getStru().get(x), costola);
					opera.getLibro().add(libro);
				}
			}
		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		} catch (SolrException e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		} catch (SolrServerException e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		} catch (ImageViewerException e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		} catch (XsdException e) {
			log.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		}
		return opera;
	}

	private void checkStru(Libro libro, Stru stru, boolean costola) throws ServletException{
		Libro  subLibro = null;
		Volume volume = null;
		int idPagina = 0;
		int start = 0;
		int stop = 0;
		
		if (stru.getStru() != null &&
				stru.getStru().size()>0){
			for (int x=0; x<stru.getStru().size(); x++){
				subLibro = new Libro();
				subLibro.setTitolo(stru.getStru().get(x).getNomenclature());
				checkStru(subLibro, stru.getStru().get(x),  costola);
				libro.getLibro().add(subLibro);
			}
		} else {
			if (stru.getStart() != null){
				for (int x=stru.getStart().intValue(); x<=(stru.getStop()==null?stru.getStart().intValue():stru.getStop().intValue()); x++){
					volume = new Volume();
					volume.setValue(stru.getStru().get(x).getNomenclature());
	
					idPagina = x;
					if (costola){
						idPagina--;
					}
					volume.setHref("javascript:parent.changePage("+idPagina+");");
					libro.getVolume().add(volume);
				}
			} else {
				if (stru.getElement() != null && 
						stru.getElement().size()>0){
					for (int y=0; y<stru.getElement().size(); y++){
						if (stru.getElement().get(y).getStart() != null &&
								stru.getElement().get(y).getStart().getSequenceNumber() != null){
							start = stru.getElement().get(y).getStart().getSequenceNumber().intValue();
							if (stru.getElement().get(y).getStop() != null &&
									stru.getElement().get(y).getStop().getSequenceNumber() != null){
								stop = stru.getElement().get(y).getStop().getSequenceNumber().intValue();
							} else {
								stop = start;
							}
						for (int x=start; x<=stop; x++){
							volume = new Volume();
							volume.setValue("P."+x);
			
							idPagina = x;
							if (costola){
								idPagina--;
							}
							volume.setHref("javascript:parent.changePage("+idPagina+");");
							libro.getVolume().add(volume);
						}
						}
					}
				}
			}
		}
	}
//	private void checkStru(Libro libro, String idr, String idrPadre, boolean costola) throws ServletException{
//		Vector<String[]> stru = null;
//		Libro  subLibro = null;
//		Volume volume = null;
//		int idPagina = 0;
//
//		try {
//			stru = readStru(idr, "3");
//			if (stru != null){
//				for (int x=0; x<stru.size(); x++){
//					subLibro = new Libro();
//					subLibro.setTitolo(stru.get(x)[1]);
//					checkStru(subLibro, stru.get(x)[0], idrPadre, costola);
//					libro.getLibro().add(subLibro);
//				}
//			}else{
//				stru = readStru(idr, "1");
//				if (stru != null){
//					for (int x=0; x<stru.size(); x++){
//						volume = new Volume();
//						volume.setValue(stru.get(x)[1]);
//						idPagina = getIdPagina(idrPadre, stru.get(x)[0]);
//						if (costola){
//							idPagina--;
//						}
//						volume.setHref("javascript:parent.changePage("+idPagina+");");
//						libro.getVolume().add(volume);
//					}
//				}
//			}
//		} catch (ServletException e) {
//			throw e;
//		}
//		
//	}

//	private int getIdPagina(String idrPadre, String idr) throws ServletException{
//		int result = 0;
//		TblRelRis table = null;
//		ResultSet rs = null;
//
//		try {
//			table = new TblRelRis(Configuration.getPool("teca"));
//			table.setCampoValue("relRisidrPartenza", idr);
//			table.setCampoValue("relRisidrArrivo", idrPadre);
//			rs = table.startSelect();
//			if (rs.next()){
//				result = rs.getInt("relRisSequenza");
//			}
//		} catch (SQLException e) {
//			log.error(e);
//			throw new ServletException(e.getMessage(), e);
//		} finally {
//			try {
//				if (rs != null){
//					rs.close();
//				}
//				if (table != null){
//					table.stopSelect();
//				}
//			} catch (SQLException e) {
//				log.error(e);
//				throw new ServletException(e.getMessage(), e);
//			}
//		}
//		return result;
//	}

//	private Vector<String[]> readStru(String idr, String tipoRelId) throws ServletException{
//		Vector<String[]> result =null;
//		ViewTblrisFigli viewTblRisFigli = null;
//		ResultSet rs = null;
//		String[] dati = null;
//
//		try{
//			viewTblRisFigli = new ViewTblrisFigli(Configuration.getPool("teca"));
//			viewTblRisFigli.setCampoValue("idrp", idr);
//			viewTblRisFigli.setCampoValue("tipoRelId", tipoRelId);
//			rs = viewTblRisFigli.startSelect();
//			while (rs.next()){
//				if (result == null){
//					result= new Vector<String[]>();
//				}
//				dati = new String[2];
//				dati[0]=rs.getString("idr");
//				dati[1]=rs.getString("nota");
//				result.add(dati);
//			}
//		} catch (SQLException e) {
//			log.error(e);
//			throw new ServletException(e.getMessage(), e);
//		} finally {
//			try {
//				if (rs != null){
//					rs.close();
//				}
//				if (viewTblRisFigli != null){
//					viewTblRisFigli.stopSelect();
//				}
//			} catch (SQLException e) {
//				log.error(e);
//				throw new ServletException(e.getMessage(), e);
//			}
//		}
//		return result;
//	}
}
