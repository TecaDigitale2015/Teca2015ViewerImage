/**
 * 
 */
package mx.teca2015.viewerImage.implement;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.json.JSONObject;
import org.purl.dc.elements._1.SimpleLiteral;

import it.sbn.iccu.metaag1.Bib.Holdings;
import it.sbn.iccu.metaag1.Bib.Holdings.Shelfmark;
import it.sbn.iccu.metaag1.Bib.Piece;
import it.sbn.iccu.metaag1.BibliographicLevel;
import it.sbn.iccu.metaag1.Img;
import it.sbn.iccu.metaag1.Img.Altimg;
import it.sbn.iccu.metaag1.Metadigit;
import mx.randalf.configuration.Configuration;
import mx.randalf.configuration.exception.ConfigurationException;
import mx.randalf.solr.exception.SolrException;
import mx.randalf.xsd.exception.XsdException;
import mx.teca2015.tecaUtility.solr.item.ItemTeca;
import mx.teca2015.viewerImage.interfacie.IImageViewer;
import mx.teca2015.viewerImage.interfacie.exception.ImageViewerException;

/**
 * @author massi
 *
 */
public abstract class ImageViewerTecaDigitaleIIIF extends IImageViewer {

	/**
	 * 
	 */
	public ImageViewerTecaDigitaleIIIF() {
	}

	
/*
{
  "sequences": [
    {
      "@context": "http://iiif.io/api/image/2/context.json",
      "@id": "http://192.168.7.78/iiif/f55bd4c8-8d9b-4117-9be7-6773376cb5bb/canvas/default",
      "@type": "sc:Sequence",
      "canvases": [
        {
          "@id": "http://192.168.7.78/iiif/ArchivioLucchini/012/19175/IIIF/0001.tif/canvas",
          "@type": "sc:Canvas",
          "height": 2904,
          "images": [
            {
              "@context": "http://iiif.io/api/image/2/context.json",
              "@id": "http://192.168.7.78/iiif/ArchivioLucchini/012/19175/IIIF/0001.tif/annotation",
              "@type": "oa:Annotation",
              "motivation": "sc:painting",
              "resource": {
                "@id": "http://192.168.7.78/iiif/ArchivioLucchini/012/19175/IIIF/0001.tif/full/full/0/native.jpg",
                "@type": "dctypes:Image",
                "format": "image/jpeg",
                "height": 2904,
                "service": {
                  "@context": "http://iiif.io/api/image/2/context.json",
                  "@id": "http://192.168.7.78/iiif/ArchivioLucchini/012/19175/IIIF/0001.tif",
                  "profile": "http://iiif.io/api/image/2/profiles/level2.json"
                },
                "width": 2220
              }
            }
          ],
          "label": "Carta Fronte",
          "width": 2220
        },
        {
          "@id": "http://192.168.7.78/iiif/f55bd4c8-8d9b-4117-9be7-6773376cb5bb$2/canvas",
          "@type": "sc:Canvas",
          "height": 2868,
          "images": [
            {
              "@context": "http://iiif.io/api/image/2/context.json",
              "@id": "http://192.168.7.78/iiif/ArchivioLucchini/012/19175/IIIF/0002.tif/annotation",
              "@type": "oa:Annotation",
              "motivation": "sc:painting",
              "resource": {
                "@id": "http://192.168.7.78/iiif/ArchivioLucchini/012/19175/IIIF/0002.tif/full/full/1/native.jpg",
                "@type": "dctypes:Image",
                "format": "image/jpeg",
                "height": 2868,
                "service": {
                  "@context": "http://iiif.io/api/image/2/context.json",
                  "@id": "http://192.168.7.78/iiif/ArchivioLucchini/012/19175/IIIF/0002.tif",
                  "profile": "http://iiif.io/api/image/2/profiles/level2.json"
                },
                "width": 2196
              }
            }
          ],
          "label": "Carta Retro",
          "width": 2196
        }
      ],
    }
  ],
}
 */
	
	/**
	 * @see mx.teca2015.viewerImage.interfacie.IImageViewer#jSon(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String jSon(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SolrDocumentList solrResponse = null;
		Metadigit mag = null;
		JSONObject obj  = null;
		String result = null;
		SolrDocument solrDocument = null;
		File fMag = null;
		
		try {
			solrResponse = ImageViewerTecaDigitale.findSolr(request.getParameter("id"));
			
			solrDocument = solrResponse.get(0);
			fMag = new File(((ArrayList<String>) solrDocument.get(ItemTeca.ORIGINALFILENAME+"_show")).get(0));
			mag = ImageViewerTecaDigitale.readXml(solrResponse);

			if (mag.getImg() != null && 
					mag.getImg().size()>0){
				obj = new JSONObject();

				obj.put("@context","http://iiif.io/api/image/2/context.json");
				obj.put("@id", getURL(request));
				obj.put("@type", "sc:Manifest");
				obj.put("attribution", "");
				obj.put("description", "");
				obj.put("label", mag.getBib().getTitle().get(0).getContent().get(0));
				obj.put("logo", "");
				
				obj.put("metadata", addMetadata(mag));
				
				obj.put("related", Configuration.getValue("web.URLShowImg")+request.getParameter("id"));
//				obj.put("seeAlso", "http://archive.org/metadata/platowithenglish04platuoft");
				obj.put("sequence", addSequence(request, mag.getImg(), fMag));
//				web.URLIIF
				obj.put("thumbnail", addThumbnail(mag.getImg().get(0), fMag));
				obj.put("viewingHint","paged");
				result = obj.toString();
			}
		} catch (NumberFormatException e) {
			throw new ServletException(e.getMessage(),e);
		} catch (ConfigurationException e) {
			throw new ServletException(e.getMessage(),e);
		} catch (SolrException e) {
			throw new ServletException(e.getMessage(),e);
		} catch (SolrServerException e) {
			throw new ServletException(e.getMessage(),e);
		} catch (ImageViewerException e) {
			throw new ServletException(e.getMessage(),e);
		} catch (XsdException e) {
			throw new ServletException(e.getMessage(),e);
		}
		return result;
	}

	private Map<String,Object> addSequence(HttpServletRequest request, List<Img> imgs, File fMag) throws ConfigurationException {
		Map<String, Object> jsonObjects = null;

		try {
			jsonObjects = new HashMap<String, Object>();

			jsonObjects.put("@context", "http://iiif.io/api/image/2/context.json");
			jsonObjects.put("@id", Configuration.getValue("web.URLIIF")+"/"+
					request.getParameter("id")+"/canvas/default");
			jsonObjects.put("@type", "sc:Sequence");
			jsonObjects.put("canvases", addCanvases(imgs, fMag));
			jsonObjects.put("label", "default");
		} catch (ConfigurationException e) {
			throw e;
		}
		return jsonObjects;
	}


	private Collection<Map<String,Object>> addCanvases(List<Img> imgs, File fMag) throws ConfigurationException {
		Collection<Map<String,Object>> jsonObjects = null;

		try {
			jsonObjects = new ArrayList<Map<String,Object>>();
			for(Img img: imgs)
			{
				jsonObjects.add(addCanvases(img, fMag));
			  }
		} catch (ConfigurationException e) {
			throw e;
		}

		return jsonObjects;
	}


	private Map<String,Object> addCanvases(Img img, File fMag) throws ConfigurationException {
		Map<String, Object> jsonObjects = null;

		try {
			jsonObjects = new HashMap<String, Object>();

			jsonObjects.put("@type", "sc:Canvas");
			jsonObjects.put("height", img.getImageDimensions().getImagelength());
			jsonObjects.put("label", img.getNomenclature());
			jsonObjects.put("width", img.getImageDimensions().getImagewidth());
			if (checkUsage(img.getUsage())){
				jsonObjects.put("@id", Configuration.getValue("web.URLIIF")+
						genFileIIIF(img.getFile().getHref(), fMag)+"/canvas");
				jsonObjects.put("images", addImages(img.getFile().getHref(), fMag, img.getImageDimensions().getImagewidth(), img.getImageDimensions().getImagelength()));
			} else if (img.getAltimg() != null){
				for (Altimg altimg: img.getAltimg()){
					if (checkUsage(altimg.getUsage())){
						jsonObjects.put("@id", Configuration.getValue("web.URLIIF")+
								genFileIIIF(altimg.getFile().getHref(), fMag)+"/canvas");
						jsonObjects.put("images", addImages(altimg.getFile().getHref(), fMag, img.getImageDimensions().getImagewidth(), img.getImageDimensions().getImagelength()));
					}
				}
			}
		} catch (ConfigurationException e) {
			throw e;
		}
		return jsonObjects;
	}


	private Collection<Map<String,Object>> addImages(String iiif, File fMag, BigInteger width, BigInteger height) throws ConfigurationException {
		Collection<Map<String,Object>> jsonObjects = null;

		try {
			jsonObjects = new ArrayList<Map<String,Object>>();
			jsonObjects.add(addImage(iiif, fMag, width, height));
		} catch (ConfigurationException e) {
			throw e;
		}
		return jsonObjects;
	}


	private Map<String, Object> addImage(String iiif, File fMag, BigInteger width, BigInteger height) throws ConfigurationException {
		Map<String, Object> jsonObjects = null;

		try {
			jsonObjects = new HashMap<String, Object>();

			jsonObjects.put("@context", "http://iiif.io/api/image/2/context.json");
			jsonObjects.put("@id", Configuration.getValue("web.URLIIF")+
					genFileIIIF(iiif, fMag)+"/annotation");
			jsonObjects.put("@type", "oa:Annotation");
			jsonObjects.put("motivation", "sc:painting");
			jsonObjects.put("resource", addResource(iiif, fMag, width, height));
		} catch (ConfigurationException e) {
			throw e;
		}
		return jsonObjects;
	}


	private Map<String, Object> addResource(String iiif, File fMag,  BigInteger width, BigInteger height) throws ConfigurationException {
		Map<String, Object> jsonObjects = null;

		try {
			jsonObjects = new HashMap<String, Object>();
			jsonObjects.put("@id", Configuration.getValue("web.URLIIF")+
					genFileIIIF(iiif, fMag)+"/full/full/0/native.jpg");
			jsonObjects.put("@type", "dctypes:Image");
			jsonObjects.put("format", "image/jpeg");
			jsonObjects.put("height", height);
			jsonObjects.put("service", addService( iiif,  fMag));
			jsonObjects.put("width", width);
		} catch (ConfigurationException e) {
			throw e;
		}
		return jsonObjects;
	}


	private Map<String, Object> addService(String iiif, File fMag) throws ConfigurationException {
		Map<String, Object> jsonObjects = null;

		try {
			jsonObjects = new HashMap<String, Object>();
			jsonObjects.put("@context", "http://iiif.io/api/image/2/context.json");
			jsonObjects.put("@id", Configuration.getValue("web.URLIIF")+
					genFileIIIF(iiif, fMag));
			jsonObjects.put("profile", "http://iiif.io/api/image/2/profiles/level2.json");
		} catch (ConfigurationException e) {
			throw e;
		}

		return jsonObjects;
	}


	private JSONObject addThumbnail(Img img, File fMag) throws ConfigurationException {
		JSONObject jsonObject = null;
		
		try {
			if (checkUsage(img.getUsage())){
				jsonObject = addMetadataRow("@id", Configuration.getValue("web.URLIIF")+
						genFileIIIF(img.getFile().getHref(), fMag));
			} else if (img.getAltimg() != null){
				for (Altimg altimg: img.getAltimg()){
					if (checkUsage(altimg.getUsage())){
						jsonObject = addMetadataRow("@id", Configuration.getValue("web.URLIIF")+
								genFileIIIF(altimg.getFile().getHref(), fMag));
					}
				}
			}
		} catch (ConfigurationException e) {
			throw e;
		}
		return jsonObject;
	}

	private String genFileIIIF(String href, File fMag) {
		File fIIIF = null;
		
		if (href.startsWith("./")){
			fIIIF = new File(fMag.getParentFile().getAbsolutePath()+href.substring(1));
		} else if (href.startsWith("/")){
			fIIIF = new File(fMag.getParentFile().getAbsolutePath()+href);
		} else {
			fIIIF = new File(fMag.getParentFile().getAbsolutePath()+File.separator+href);
		}
		return fIIIF.getAbsolutePath();
	}

	private Collection<JSONObject> addMetadata(Metadigit mag) {
		Collection<JSONObject> metadata = null;
		
		metadata = new ArrayList<JSONObject>();
		addMetadataRow(metadata, "contributor", mag.getBib().getContributor());
		addMetadataRow(metadata, "coverage", mag.getBib().getCoverage());
		addMetadataRow(metadata, "creator", mag.getBib().getCreator());
		addMetadataRow(metadata, "date", mag.getBib().getDate());
		addMetadataRow(metadata, "description", mag.getBib().getDescription());
		addMetadataRow(metadata, "format", mag.getBib().getFormat());
		addMetadataRow(metadata, "identifier", mag.getBib().getIdentifier());
		addMetadataRow(metadata, "language", mag.getBib().getLanguage());
		addMetadataRow(metadata, "publisher", mag.getBib().getPublisher());
		addMetadataRow(metadata, "relation", mag.getBib().getRelation());
		addMetadataRow(metadata, "rights", mag.getBib().getRights());
		addMetadataRow(metadata, "source", mag.getBib().getSource());
		addMetadataRow(metadata, "subject", mag.getBib().getSubject());
		addMetadataRow(metadata, "title", mag.getBib().getTitle());
		addMetadataRow(metadata, "type", mag.getBib().getType());
		addMetadataRow(metadata, mag.getBib().getHoldings());
		addMetadataRow(metadata, "level", mag.getBib().getLevel());
		addMetadataRow(metadata, mag.getBib().getPiece());

		return metadata;
	}

	private void addMetadataRow(Collection<JSONObject> metadata, Piece piece) {
		if (piece != null){
			if (piece.getYear() != null){
				metadata.add(addMetadataRow("year", piece.getYear()));
			}
			if (piece.getIssue() != null){
				metadata.add(addMetadataRow("issue", piece.getIssue()));
			}
			if (piece.getStpiecePer() != null){
				metadata.add(addMetadataRow("stpiece_per", piece.getStpiecePer()));
			}
			if (piece.getPartNumber() != null){
				metadata.add(addMetadataRow("part_number", piece.getPartNumber().toString()));
			}
			if (piece.getPartName() != null){
				metadata.add(addMetadataRow("part_name", piece.getPartName()));
			}
			if (piece.getStpieceVol() != null){
				metadata.add(addMetadataRow("stpiece_vol", piece.getStpieceVol()));
			}
		}
	}

	private void addMetadataRow(Collection<JSONObject> metadata, String key, BibliographicLevel level) {
		if (level != null){
			if (level.equals(BibliographicLevel.A)){
				metadata.add(addMetadataRow("level", "analitico"));
			} else if (level.equals(BibliographicLevel.C)){
				metadata.add(addMetadataRow("level", "raccolta"));
			} else if (level.equals(BibliographicLevel.M)){
				metadata.add(addMetadataRow("level", "monografia"));
			} else if (level.equals(BibliographicLevel.S)){
				metadata.add(addMetadataRow("level", "pubblicazione in serie"));
			}
		}
	}

	private void addMetadataRow(Collection<JSONObject> metadata, List<Holdings> holdingses) {
		if (holdingses != null){
			for (Holdings holdings : holdingses){
				if (holdings.getInventoryNumber()!= null){
					metadata.add(addMetadataRow("inventory", holdings.getInventoryNumber()));
				}
				if (holdings.getLibrary() != null){
					metadata.add(addMetadataRow("library", holdings.getLibrary()));
				}
				addMetadataRowShelfmark(metadata, holdings.getShelfmark());
			}
		}
	}

	private void addMetadataRowShelfmark(Collection<JSONObject> metadata, List<Shelfmark> shelfmarks) {
		if (shelfmarks != null){
			for (Shelfmark shelfmark : shelfmarks){
				if (shelfmark.getContent() != null){
					metadata.add(addMetadataRow("shelfmark", shelfmark.getContent()));
				}
			}
		}
	}

	private void addMetadataRow(Collection<JSONObject> metadata, String key, List<SimpleLiteral> values) {
		if (values != null){
			for (SimpleLiteral value: values){
				for (String val: value.getContent()){
					metadata.add(addMetadataRow(key, val));
				}
			}
		}
	}

	private JSONObject addMetadataRow(String key, String value) {
		JSONObject row = null;
		
		row = new JSONObject();
		row.put(key, value);
		return row;
	}

	private String getURL(HttpServletRequest request) {
	    StringBuffer url = new StringBuffer();
	    int port = request.getServerPort();
	    if (port < 0) {
	        port = 80; // Work around java.net.URL bug
	    }
	    String scheme = request.getScheme();
	    url.append(scheme);
	    url.append("://");
	    url.append(request.getServerName());
	    if (("http".equals(scheme) && (port != 80)) || ("https".equals(scheme) && (port != 443))) {
	        url.append(':');
	        url.append(port);
	    }
	    url.append(request.getContextPath());
	    if (request.getPathInfo() != null){
		    url.append(request.getPathInfo());
	    }
	    if (request.getQueryString() != null){
		    url.append("?"+request.getQueryString());
	    }
	    return url.toString();
	}

	protected boolean checkUsage(List<String> usage){
		boolean isUsage = false;
		for (int x=0; x<usage.size(); x++){
			if (usage.get(x).equals("6") || usage.get(x).equals("7")){
				isUsage=true;
				break;
			}
		}
		return isUsage;
	}

}
