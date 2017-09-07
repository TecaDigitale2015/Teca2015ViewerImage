package mx.teca2015.viewerImage.implement.showImage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import mx.teca2015.tecaUtility.solr.item.ItemTeca;
import mx.teca2015.viewerImage.imageViewer.IVConfiguration;
import mx.teca2015.viewerImage.imageViewer.imager.ImageManager;
import mx.teca2015.viewerImage.imageViewer.imager.exception.ImageException;

import org.apache.solr.common.SolrDocument;

/**
 * Classe utilizzata per la gestione delle informazioni relative hai dati dell'immagine
 * 
 * @author massi
 *
 */
public class DatiImmagini {

	/**
	 * Protocollo per il collegamento con lo storage FTP/NTFS
	 */
	String hostProt=null;

	/**
	 * Path relativa allo storage
	 */
	String hostServerPath=null;

	/**
	 * Definizione del disco relativo allo storage
	 */
	String hostPathDisco=null;

	/**
	 * Path della singola immagine
	 */
	String imgPathName=null;

	/**
	 * Indirizzo IP dello storage
	 */
	String hostIp=null;

	/**
	 * Porta dello storage relativo al protocollo FTP
	 */
	int hostPorta=-1;

	/**
	 * Utente per il collegamento allo storage
	 */
	String hostLogin= null;

	/**
	 * Password per il collegamento allo storage
	 */
	String hostPsw=null;

	/**
	 * Formato dell'oggetto digitale
	 */
	String mimeDes=null;

	/**
	 * 
	 * @param rs Record relativo al  risultato della ricerca
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public DatiImmagini(SolrDocument solrResponse, String imgPathName, String mimeDes){
		String originaFile = "";
		File fOri = null;
		File fFolder = null;
		ArrayList<String> nomeFile = null;
		
		nomeFile = ((ArrayList<String>) solrResponse.getFieldValue(ItemTeca.ORIGINALFILENAME+"_show"));
		originaFile = (String) nomeFile.get(0);
		if (originaFile.startsWith("/")){
			this.hostProt=IVConfiguration.NFS_SCHEME;
			this.hostIp="127.0.0.1";
			this.hostPorta=0;
			this.hostLogin="";
			this.hostPsw="";
		} else if (originaFile.startsWith("file://")){
			this.hostProt=IVConfiguration.NFS_SCHEME;
			this.hostIp="127.0.0.1";
			this.hostPorta=0;
		} else {
			this.hostProt=IVConfiguration.NFS_SCHEME;
			this.hostIp="127.0.0.1";
			this.hostPorta=0;
		}

		fOri = new File(originaFile);
		fFolder = new File(fOri.getParentFile().getAbsolutePath());
		this.hostServerPath=fFolder.getParentFile().getAbsolutePath();
		this.hostPathDisco="/"+fFolder.getName();
		this.imgPathName=imgPathName;
		this.mimeDes=mimeDes;
	}

	public String getHostProt() {
		return hostProt;
	}

	public String getHostServerPath() {
		return hostServerPath;
	}

	public String getHostPathDisco() {
		return hostPathDisco;
	}

	public String getImgPathName() {
		return imgPathName;
	}

	public String getHostIp() {
		return hostIp;
	}

	public int getHostPorta() {
		return hostPorta;
	}

	public String getHostLogin() {
		return hostLogin;
	}

	public String getHostPsw() {
		return hostPsw;
	}

	public String getMimeDes() {
		return mimeDes;
	}

	public URL getURL() throws ImageException{
		return ImageManager.createURL(hostProt, hostServerPath, hostPathDisco, imgPathName, hostIp, hostPorta, hostLogin, hostPsw, 
				mimeDes, true);		
	}
}
