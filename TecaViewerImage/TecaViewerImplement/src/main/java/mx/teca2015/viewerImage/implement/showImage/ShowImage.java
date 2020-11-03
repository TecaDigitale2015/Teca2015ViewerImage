/**
 * 
 */
package mx.teca2015.viewerImage.implement.showImage;

import it.sbn.iccu.metaag1.Img;
import it.sbn.iccu.metaag1.Metadigit;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.solr.exception.SolrException;
import mx.randalf.xsd.exception.XsdException;
import mx.teca2015.viewerImage.imageViewer.imager.ImageManager;
import mx.teca2015.viewerImage.imageViewer.imager.exception.ImageException;
import mx.teca2015.viewerImage.implement.ImageViewerTecaDigitale;
import mx.teca2015.viewerImage.implement.readBook.ReadBook;
import mx.teca2015.viewerImage.interfacie.exception.ImageViewerException;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;


/**
 * @author massi
 *
 */
public class ShowImage
{

	/**
	 * Variabile utilizzata per loggare l'appplicazione
	 */
	private static Logger log = Logger.getLogger(ShowImage.class);

	/**
	 * 
	 */
	public ShowImage()
	{
	}

	public static void showImage(String risIdr, HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		ImageManager imageManager = null;
//		ViewImages view = null;
//		ResultSet rs = null;
		String usage = "";
		boolean absolutePath = false;
//		boolean forceUsage = false;
		DatiImmagini datiImmagini = null;
		Metadigit mag = null;
		SolrDocumentList solrResponse = null;
		Img img = null;
		int sequence = 0;
		
		try
		{
//			log.debug("risIdr: "+risIdr);
//			view = new ViewImages(Configuration.getPool("teca"));
//
			if (request.getParameter("usage") != null){
//				forceUsage = true;
				usage = request.getParameter("usage");
//				view.setCampoValue("imgUsage", request.getParameter("usage"));
			}
			else{
				usage = (String)Configuration.getValueDefault("imageViewer."+request.getServerName()+".usageImageDefault", 
						Configuration.getValueDefault("imageViewer.ALL.usageImageDefault", "3"));
			}
			if (checkIp(usage, request)){
				if (request.getParameter("idr") != null){
					solrResponse = ImageViewerTecaDigitale.findSolr(risIdr);
					mag = ImageViewerTecaDigitale.readXml(solrResponse);
//			view.setCampoValue("risIdr", risIdr);
////			System.out.println("risIdr: "+risIdr);
//			rs = view.startSelect();
//			while(rs.next()){
//				if (checkIp(rs.getString("imgUsage"), request) ||
//					rs.getString("imgUsage").equals(usage) ||
//					forceUsage){

					sequence = Integer.valueOf(request.getParameter("sequence"));
					if (mag.getImg()!= null &&
							mag.getImg().size()>=sequence){
						img = mag.getImg().get(sequence-1);
						
						if (ReadBook.checkUsage(img.getUsage(), usage)){
							datiImmagini = new DatiImmagini(solrResponse.get(0), 
									img.getFile().getHref(),
									ReadBook.getFormat(img, mag.getGen()).getMime().value());
						} else {
							if (img.getAltimg()!= null &&
									img.getAltimg().size()>0){
								for (int x=0;x<img.getAltimg().size(); x++){
									if (ReadBook.checkUsage(img.getAltimg().get(x).getUsage(), usage)){
										datiImmagini = new DatiImmagini(solrResponse.get(0), 
												img.getAltimg().get(x).getFile().getHref(),
												ReadBook.getFormat(img.getAltimg().get(x), mag.getGen()).getMime().value());
										break;
									}
								}
							}
						}
						
//					break;
//				}
//			}
//
						if (datiImmagini != null)
						{
							absolutePath = ((String)
									Configuration.getValueDefault("imageViewer."+request.getServerName()+".ftp.absolutePath", 
									              Configuration.getValueDefault("imageViewer.ALL.ftp.absolutePath", "true"))).
									              equalsIgnoreCase("true");
							imageManager = new ImageManager();
							imageManager.initialize(
									ImageManager.createURL(datiImmagini.getHostProt(), datiImmagini.getHostServerPath(), 
											datiImmagini.getHostPathDisco(), datiImmagini.getImgPathName(), datiImmagini.getHostIp(), 
											datiImmagini.getHostPorta(), datiImmagini.getHostLogin(), datiImmagini.getHostPsw(), 
											datiImmagini.getMimeDes(), absolutePath), false);
							response.setContentType("image/jpeg");
							imageManager.sendImage(response.getOutputStream());
						}
						else
						{
							log.info("Non risulta presente l'immagine ["+risIdr+"]");
							errorImage(request,response);
//							throw new ServletException("Non risulta presente l'immagine ["+risIdr+"]");
						}
					}
					else
					{
						log.info("Non risulta presente l'immagine ["+risIdr+"]");
						errorImage(request,response);
//						throw new ServletException("Non risulta presente l'immagine ["+risIdr+"]");
					}
				}
				else
				{
					log.info("Non risulta presente l'immagine ["+risIdr+"]");
					errorImage(request,response);
//					throw new ServletException("Non risulta presente l'immagine ["+risIdr+"]");
				}
			}
			else
			{
				log.info("Non risulta presente l'immagine ["+risIdr+"]");
				errorImage(request,response);
//				throw new ServletException("Non risulta presente l'immagine ["+risIdr+"]");
			}
		}
		catch (ConfigurationException e)
		{
			log.error(e);
			errorImage(request,response);
			throw new ServletException(e);
		}
		catch (ServletException e)
		{
			errorImage(request,response);
			throw e;
		}
		catch (ImageException e)
		{
			log.error(e);
			errorImage(request,response);
//			throw new ServletException(e);
		}
		catch (IOException e)
		{
			log.error(e);
			errorImage(request,response);
			throw new ServletException(e);
		}
		catch (RuntimeException e)
		{
			errorImage(request,response);
//			throw new ServletException(e);
		} catch (SolrException e) {
			log.error(e);
			errorImage(request,response);
			throw new ServletException(e);
		} catch (SolrServerException e) {
			log.error(e);
			errorImage(request,response);
			throw new ServletException(e);
		} catch (ImageViewerException e) {
			log.error(e);
			errorImage(request,response);
			throw new ServletException(e);
		} catch (XsdException e) {
			log.error(e);
			errorImage(request,response);
			throw new ServletException(e);
		}
		finally
		{
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
		}
	}

	private static void errorImage(HttpServletRequest request, HttpServletResponse response) throws ServletException{
		String errorImg = "";
		File f = null;
		ImageManager imageManager = null;
		
		try {
			errorImg = ((String)
					Configuration.getValueDefault("imageViewer."+request.getServerName()+".imageNotFound", 
				              Configuration.getValueDefault("imageViewer.ALL.imageNotFound", "")));
			if (errorImg != null &&
					!errorImg.trim().equals("")){
				f = new File(errorImg);
				if (f.exists()){
					imageManager = new ImageManager();
					imageManager.initialize(
							ImageManager.createURL("file://"+f.getAbsolutePath()), false);
					response.setContentType("image/jpeg");
					imageManager.sendImage(response.getOutputStream());
				}
			}
		} catch (ImageException e) {
			log.error(e);
			throw new ServletException(e);
		} catch (IOException e) {
			log.error(e);
			throw new ServletException(e);
		} catch (ConfigurationException e) {
			log.error(e);
			throw new ServletException(e);
		}
	}

	private static  boolean checkIp(String usage, HttpServletRequest request) throws ConfigurationException{
		String ipCheck = null;
		String[] st = null;
		String[] st2 = null;
		String[] remoteAddr = null;
		boolean result = false;

		ipCheck = (String)Configuration.getValueDefault("imageViewer."+request.getServerName()+".usage."+usage, 
				Configuration.getValueDefault("imageViewer.ALL.usage."+usage, ""));

		if (ipCheck != null && !ipCheck.equals("")){
			remoteAddr = request.getRemoteAddr().split("\\.");
			st = ipCheck.split(",");
			for (int x=0; x<st.length; x++){
				st2 = st[x].split("\\.");
				if ((st2[0].equals("*") ||st2[0].equals(remoteAddr[0])) && 
					(st2[1].equals("*") ||st2[1].equals(remoteAddr[1])) && 
					(st2[2].equals("*") ||st2[2].equals(remoteAddr[2])) && 
					(st2[3].equals("*") ||st2[3].equals(remoteAddr[3]))){
					result = true;
				}
			}
		}
		return result;
	}
}
