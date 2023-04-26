package com.xian.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.xian.model.CPE;

// OJO: la carga de CPEs es muy lenta, mejor no usarlos
@Service
public class CPEService {

	private Map<String, CPE> cpes = new HashMap<>();

	private Map<String, Map<String, List<CPE>>> vendorProductMap = new HashMap<>();

	public void loadFromJSON(String cpeJSONPath) {
		try {
			this.cpes = new HashMap<>();

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(new File(cpeJSONPath));
			ArrayNode cpeItems = (ArrayNode) rootNode.get("cpe-list").get("cpe-item");

			for (JsonNode cpeItem : cpeItems) {
				String title = cpeItem.get("title").asText();
				String cpe23 = cpeItem.get("cpe-23").get("cpe23-item").get("name").asText();

				if ((title != null) && (cpe23 != null)) {
					CPE cpe = new CPE(cpe23, title);
					System.out.println(">>>> " + cpe);
					addCPE(cpe);
				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			Logger.getLogger(CWEService.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void loadFromXML(String cpeXMLPath) {
		try {
			this.cpes = new HashMap<>();

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse(new File(cpeXMLPath));

			XPath xPath = XPathFactory.newInstance().newXPath();

			NodeList cpeItems = (NodeList) xPath.evaluate("/cpe-list/cpe-item", document, XPathConstants.NODESET);
			for (int i = 0; i < cpeItems.getLength(); i++) {
				Element cpeItem = (Element) cpeItems.item(i);
				NodeList children = cpeItem.getChildNodes();
				String title = null;
				String cpe23 = null;
				for (int j = 0; j < children.getLength(); j++) {
					Node n = children.item(j);
					if (n.getNodeName().equals("title")) {
						title = n.getTextContent();
					}
					if (n.getNodeName().equals("cpe-23:cpe23-item")) {
						cpe23 = xPath.evaluate("@name", n);
					}
				}
				if ((title != null) && (cpe23 != null)) {
					CPE cpe = new CPE(cpe23, title);
					System.out.println(">>>> " + cpe);
					addCPE(cpe);
				}
			}

		} catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException ex) {
			ex.printStackTrace();
			Logger.getLogger(CWEService.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void addCPE(CPE cpe) {
		this.cpes.put(cpe.getCpe23(), cpe);

		String vendor = cpe.getVendor();
		String product = cpe.getProduct();

		Map<String, List<CPE>> vendorMap = this.vendorProductMap.get(vendor);
		if (vendorMap == null) {
			vendorMap = new HashMap<>();
			this.vendorProductMap.put(vendor, vendorMap);
		}
		List<CPE> productCPEs = vendorMap.get(product);
		if (productCPEs == null) {
			productCPEs = new ArrayList<>();
			vendorMap.put(product, productCPEs);
		}
		productCPEs.add(cpe);
	}

	public final static void main(String[] args) {
		CPEService service = new CPEService();
		service.loadFromXML("/tmp/CVE/official-cpe-dictionary_v2.3.xml");

	}

}
