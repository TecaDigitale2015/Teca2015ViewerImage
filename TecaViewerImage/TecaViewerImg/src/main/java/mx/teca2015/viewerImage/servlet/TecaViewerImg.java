/**
 * 
 */
package mx.teca2015.viewerImage.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;

import org.apache.log4j.Logger;

import mx.imageviewer.schema.ImageViewerXsd;
import mx.imageviewer.schema.OperaXsd;
import mx.imageviewer.schema.ReadBookXsd;
import mx.imageviewer.schema.gestionelibro.ReadBook;
import mx.imageviewer.schema.gestioneopera.Opera;
import mx.imageviewer.schema.gestionepagina.ImageViewer;
import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.converter.xsl.ConverterXsl;
import mx.randalf.converter.xsl.exception.ConvertXslException;
import mx.randalf.xsd.exception.XsdException;
import mx.teca2015.viewerImage.interfacie.IImageViewer;
import mx.teca2015.viewerImage.interfacie.manifestPrefix.ImageViewerNamespacePrefixMapper;

/**
 * @author massi
 *
 */
public class TecaViewerImg extends HttpServlet implements Servlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6141407133171278629L;

	/**
	 * Variabile utilizzata per loggare l'applicazione
	 */
	private Logger log = Logger.getLogger(TecaViewerImg.class);

	/**
	 * 
	 */
	public TecaViewerImg() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException {
		Vector<String> paths = null;
		String[] st = null;
		File f = null;

		try {
			log.debug("init");

			paths = new Vector<String>();
			if (this.getServletContext().getInitParameter("nomeCatalogo") == null){
				paths.add(calcPath()+"conf/teca_digitale");
			} else {
				st = this.getServletContext().getInitParameter("nomeCatalogo").split("\\|");
				for (int x=0; x<st.length; x++){
					if (st[x].startsWith("file:")){
						if (st[x].startsWith("file:////")){
							paths.add(st[x].replace("file:///", ""));
						} else if (st[x].startsWith("file:///")){
							paths.add(st[x].replace("file://", ""));
						}
					} else {
						paths.add(calcPath()+st[x]);
					}
				}
			}
			
			for (int x=0; x<paths.size(); x++){
				f = new File(paths.get(x));
				if (f.exists()){
					log.debug("pathProperties: " + f.getAbsolutePath());

					Configuration.init(f.getAbsolutePath());
					break;
				}
			}
			if (!Configuration.isInizialize()){
				throw new ServletException("Non risulta essere presente i files di configurazione");
			}

		} catch (ConfigurationException e) {
			throw new ServletException(e.getMessage(), e);
		}
	}

	private String calcPath(){
		String pathProperties = null;
		if (System.getProperty("catalina.base") != null)
			pathProperties = System.getProperty("catalina.base")
					+ File.separator;
		else
			pathProperties = System.getProperty("jboss.server.home.dir")
					+ File.separator;
		return pathProperties;
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.debug("doGet");
		esegui(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.debug("doPost");
		esegui(request, response);
	}

	/**
	 * Questo metodo viene utilizzato per eseguire le operazioni relative
	 * all'applicativo
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void esegui(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String metodo = "";
		String azione = "";
		String className = "";

		try {
			log.debug("esegui");
			if (request.getParameter("metodo") != null)
				metodo = request.getParameter("metodo");
			else
				metodo = (String) Configuration.
						getValue("imageViewer.default");
			log.debug("metodo: " + metodo);

			if (request.getParameter("azione") != null)
				azione = request.getParameter("azione");
			else
				azione = "home";
			log.debug("azione: " + azione);

			if (Configuration.getValue("imageViewer."
					+ metodo + ".class") != null) {
				className = (String) Configuration.getValue(
						"imageViewer." + metodo + ".class");
				log.debug("className: " + className);

//				myClass = Class.forName(className);
//				imageViewer = (IImageViewer) myClass.newInstance();

				if (azione.equals("home")) {
					showHome(request,response, className);
				} else if (azione.equals("readBook")) {
					bookReader(request, response, className);
//				} else if (azione.equals("readCatalogo")) {
//					readCatalogo(request, response, className);
//				} else if (azione.equals("showCatalogo")) {
//					showCatalogo(request, response, className);
				} else if (azione.equals("readStru")) {
					readStru(request, response, className);
				} else if (azione.equals("showImg")) {
					showImg(request, response, className);
				} else if (azione.equals("json")) {
					jSon(request, response, className);
				} else
					throw new ServletException(
							"L'azione richiesta non \u00E8 prevista da questa applicazione");
			} else
				throw new ServletException(
						"Il metodo richiesto non \u00E8 previsto da questa applicazione");
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (InstantiationException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (PropertyException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (ConvertXslException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (JAXBException e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new ServletException(e.getMessage());
		}
	}


	@SuppressWarnings("rawtypes")
	private void jSon(HttpServletRequest request, HttpServletResponse response, String className) throws ServletException {
		IImageViewer imageViewer = null;
		Class myClass = null;
		String json = null;
		PrintWriter output = null;

		try {
			myClass = Class.forName(className);
			imageViewer = (IImageViewer) myClass.newInstance();

			json = imageViewer.jSon(request, response);
			if (json != null){
				response.setContentType("application/json; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				output = response.getWriter();
				output.println(json);
			} else {
				throw new ServletException(
						"Non risultano le informazioni per il tracciato Json");
			}
		} catch (ClassNotFoundException e) {
			throw new ServletException(e.getMessage(),e);
		} catch (InstantiationException e) {
			throw new ServletException(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			throw new ServletException(e.getMessage(),e);
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw new ServletException(e.getMessage(),e);
		} finally {
			if (output != null){
				output.flush();
				output.close();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void showHome(HttpServletRequest request, HttpServletResponse response, String className) 
			throws 
			PropertyException, 
			ClassNotFoundException, InstantiationException, IllegalAccessException,
					ServletException, IOException, 
					ConvertXslException, JAXBException, 
					Exception{
		IImageViewer imageViewer = null;
		Class myClass = null;
		ImageViewer imgViewer = null;
		String fileXsl = "";
		ImageViewerXsd imageViewerXsd = null;

		try {
			myClass = Class.forName(className);
			imageViewer = (IImageViewer) myClass.newInstance();

			imgViewer = imageViewer.initPage(request, response, request.getServerName());

			if (imageViewer.isShowOpere()){
				showCatalogo(request, response, className);
			} else if (imgViewer != null) {
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				fileXsl = imageViewer.getFoglioXsl(request.getServerName());
				imageViewerXsd = new ImageViewerXsd();
//				ByteArrayOutputStream baos = null;
				
//				baos = (ByteArrayOutputStream) imageViewerXsd.writeOutputStream(imgViewer, new ImageViewerNamespacePrefixMapper());
//				System.out.println(baos.toString());

				
				ConverterXsl
						.convertXsl(
								fileXsl,
								imageViewerXsd.writeInputStream(imgViewer, new ImageViewerNamespacePrefixMapper(), false),
								response.getOutputStream());
			} else{
				throw new ServletException(
						"Non risulta essere presente le informazioni necessarie per la creazione della pagina principale");
			}
		} catch (PropertyException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (ConvertXslException e) {
			throw e;
		} catch (JAXBException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	private void showCatalogo(HttpServletRequest request, HttpServletResponse response, String className)
			throws 
			PropertyException, 
			ClassNotFoundException, InstantiationException, IllegalAccessException,
			ServletException, IOException 
			, XsdException, ConvertXslException, JAXBException, Exception
	{
		IImageViewer imageViewer = null;
		Class myClass = null;
		ImageViewer imgViewer = null;
		String fileXsl = "";
		ImageViewerXsd imageViewerXsd = null;

		try {
			myClass = Class.forName(className);
			imageViewer = (IImageViewer) myClass.newInstance();
			imgViewer = imageViewer.showCatalogo(request, response);

			if (imgViewer != null) {
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				imageViewerXsd = new ImageViewerXsd();
				log.debug(imageViewerXsd.write(imgViewer, null, null, null, null));
				fileXsl = imageViewer.getFoglioXsl(request.getServerName());
				ConverterXsl
						.convertXsl(
								fileXsl,
								imageViewerXsd.writeInputStream(imgViewer, 
										new ImageViewerNamespacePrefixMapper(), false),
								response.getOutputStream());
			} else{
				throw new ServletException(
						"Non risulta essere presente le informazioni del libro");
			}
		} catch (PropertyException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (XsdException e) {
			throw e;
		} catch (ConvertXslException e) {
			throw e;
		} catch (JAXBException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	private void readStru(HttpServletRequest request, HttpServletResponse response, String className)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			ServletException, IOException, XsdException{
		IImageViewer imageViewer = null;
		Class myClass = null;
		Opera opera= null;
		OperaXsd operaXsd = null;

		try {
			myClass = Class.forName(className);
			imageViewer = (IImageViewer) myClass.newInstance();
			opera = imageViewer.readStru(request, response);

			if (opera != null) {
				response.setContentType("text/xml; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				operaXsd = new OperaXsd();
				operaXsd.write(opera, response.getOutputStream(), null, null, null, null, false);
			} else{
				throw new ServletException(
						"Non risulta essere presente le informazioni del libro");
			}
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (XsdException e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	private void bookReader(HttpServletRequest request, HttpServletResponse response, String className)
			throws  ClassNotFoundException, InstantiationException, IllegalAccessException,
			ServletException, IOException, XsdException, IllegalArgumentException, 
			InvocationTargetException, NoSuchMethodException,SecurityException {
		IImageViewer imageViewer = null;
		Class myClass = null;
		ReadBook readBook = null;
		ReadBookXsd readBookXsd = null;

		try {
			myClass = Class.forName(className);
			imageViewer = (IImageViewer) myClass.getDeclaredConstructor().newInstance();

			imageViewer.initPage(request, response, request.getServerName());

			readBook = imageViewer.readBook(request, response);

			if (readBook != null) {
				response.setContentType("text/xml; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				readBookXsd = new ReadBookXsd();
				readBookXsd.write(readBook, response.getOutputStream(), 
						new ImageViewerNamespacePrefixMapper(), null, 
						null, null, false);
			} else{
				throw new ServletException(
						"Non risulta essere presente le informazioni del libro");
			}
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (XsdException e) {
			throw e;
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (InvocationTargetException e) {
			throw e;
		} catch (NoSuchMethodException e) {
			throw e;
		} catch (SecurityException e) {
			throw e;
		}

	}

	@SuppressWarnings("rawtypes")
	private void showImg(HttpServletRequest request, HttpServletResponse response, String className)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			ServletException, IOException{
		IImageViewer imageViewer = null;
		Class myClass = null;

		try {
			myClass = Class.forName(className);
			imageViewer = (IImageViewer) myClass.newInstance();
			imageViewer.showImage(request, response);
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (InstantiationException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (ServletException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		
	}
}
