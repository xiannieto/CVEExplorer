package com.xian.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.xian.model.CWE;
import com.xian.repository.CWERepository;

import jakarta.annotation.PostConstruct;

@Service
public class CWEService {
	
	private final static Logger logger = LoggerFactory.getLogger(CWEService.class);

	
	private Map<String, CWE> cwes = new HashMap<>();

	private List<CWE> roots;

	public CWERepository cweRepository;

	@Value("${cwe.xml.path}")
	private String cweXMLPath;

	@PostConstruct
	public void initialize() {
		logger.info("[INFO] Cargando CWES desde: " + cweXMLPath);
		loadFromXML(cweXMLPath);
	}

//	public void loadFromJSON(String jsonPath) {
//		try {
//			cwes.clear();
//			Logger.getLogger(CWEService.class.getName()).log(Level.INFO, "Loading CWEs from " + jsonPath);
//
//			// Load the JSON file as a string
//			String jsonStr = new String(Files.readAllBytes(Paths.get(jsonPath)));
//
//			// Parse JSON string
//			JSONParser parser = new JSONParser(jsonStr);
//
//			JSONObject json = null;
//			try {
//				Object obj = parser.parse();
//				json = (JSONObject) obj;
//			} catch (org.apache.tomcat.util.json.ParseException e) {
//				Logger.getLogger(CWEService.class.getName()).log(Level.SEVERE, "Error parsing CWEs from " + jsonPath);
//				e.printStackTrace();
//			}
//
//			JSONArray weaknesses = json.getJSONObject("Weakness_Catalog").getJSONArray("Weaknesses");
//
//			// Extract relevant fields from each weakness
//			for (Object weaknessObj : weaknesses) {
//				JSONObject weakness = (JSONObject) weaknessObj;
//
//				String id = weakness.getString("ID");
//				String name = weakness.getString("Name");
//				String description = weakness.getString("Description");
//				String extendedDescription = weakness.optString("Extended_Description");
//
//				CWE cwe = new CWE("CWE-" + id, name, description, extendedDescription);
//
//				JSONObject parents = weakness.optJSONObject("Related_Weaknesses");
//				if (parents != null) {
//					JSONArray childOfParents = parents.optJSONArray("ChildOf");
//					if (childOfParents != null) {
//						for (Object parentObj : childOfParents) {
//							JSONObject parent = (JSONObject) parentObj;
//							String parentCWE = parent.getString("CWE_ID");
//							cwe.addParent(parentCWE);
//						}
//					}
//				}
//				cwes.put(cwe.getCweID(), cwe);
//			}
//
//			attachChildren();
//			Logger.getLogger(CWEService.class.getName()).log(Level.INFO, "Loaded " + cwes.size() + " CWEs.");
//
//			roots = extractRoots();
//		} catch (
//
//		IOException ex) {
//			Logger.getLogger(CWEService.class.getName()).log(Level.SEVERE, "Error reading CWEs from " + jsonPath, ex);
//		} catch (JSONException ex) {
//			Logger.getLogger(CWEService.class.getName()).log(Level.SEVERE, "Error parsing CWEs from " + jsonPath, ex);
//		}
//	}
//	public void loadFromXML(String xmlPath) {
//		try {
//			this.cwes = new HashMap<>();
//			Logger.getLogger(CWEService.class.getName()).log(Level.INFO, "Loading CWEs from " + xmlPath);
//
//			// FileSystemResource resource = new FileSystemResource(xmlPath);
//			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			// Document document = builder.parse(resource.getInputStream());
//			Document document = builder.parse(new FileInputStream(xmlPath));
//
//			XPath xPath = XPathFactory.newInstance().newXPath();
//
//			NodeList weaknesses = (NodeList) xPath.evaluate("/Weakness_Catalog/Weaknesses/Weakness", document,
//					XPathConstants.NODESET);
//			int weaknessItems = weaknesses.getLength();
//			for (int i = 0; i < weaknessItems; i++) {
//				Element weakness = (Element) weaknesses.item(i);
//				// Optimizacion, evita carga lenta al final de la lista
//				// (https://stackoverflow.com/questions/3782618/xpath-evaluate-performance-slows-down-absurdly-over-multiple-calls)
//				weakness.getParentNode().removeChild(weakness);
//
//				String id = xPath.evaluate("@ID", weakness);
//				String name = xPath.evaluate("@Name", weakness);
//				String description = xPath.evaluate("Description", weakness);
//				String extendedDescription = xPath.evaluate("Extended_Description", weakness);
//
//				CWE cwe = new CWE("CWE-" + id, name, description, extendedDescription);
//
//				NodeList parents = (NodeList) xPath.evaluate("Related_Weaknesses/Related_Weakness[@Nature='ChildOf']",
//						weakness, XPathConstants.NODESET);
//				if ((parents != null) && parents.getLength() > 0) {
//					for (int k = 0; k < parents.getLength(); k++) {
//						String parentCWE = xPath.evaluate("@CWE_ID", parents.item(k));
//						cwe.addParent(parentCWE);
//					}
//				}
//
//				// TODO: extraer Language, Technology, Operating_System, Architecture de
//				// <Applicable_Platforms>
//				cwes.put(cwe.getCweID(), cwe);
//			}
//
//			attachChildren();
//			Logger.getLogger(CWEService.class.getName()).log(Level.INFO, "Loaded " + this.cwes.size() + " CWEs.");
//
//			this.roots = this.extractRoots();
//		} catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException ex) {
//			ex.printStackTrace();
//			Logger.getLogger(CWEService.class.getName()).log(Level.SEVERE, "Error reading CWEs from " + xmlPath, ex);
//		}
//	}
	
	public void loadFromXML(String xmlPath) {
		try {
			this.cwes = new HashMap<>();

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse(new FileInputStream(xmlPath));

			XPath xPath = XPathFactory.newInstance().newXPath();

			NodeList weaknesses = (NodeList) xPath.evaluate("/Weakness_Catalog/Weaknesses/Weakness", document,
					XPathConstants.NODESET);
			int weaknessItems = weaknesses.getLength();
			
			for (int i = 0; i < weaknessItems; i++) {
				Element weakness = (Element) weaknesses.item(i);
				
				// Optimizacion, evita carga lenta al final de la lista
				// (https://stackoverflow.com/questions/3782618/xpath-evaluate-performance-slows-down-absurdly-over-multiple-calls)
				weakness.getParentNode().removeChild(weakness);


				String id = xPath.evaluate("@ID", weakness);
				String name = xPath.evaluate("@Name", weakness);
				String description = xPath.evaluate("Description", weakness);
				String extendedDescription = xPath.evaluate("Extended_Description", weakness);

				Set<String> languages = new HashSet<>();
				Set<String> technologies = new HashSet<>();
				Set<String> operatingSystems = new HashSet<>();
				Set<String> architectures = new HashSet<>();

				NodeList applicablePlatforms = (NodeList) xPath.evaluate("Applicable_Platforms", weakness,
						XPathConstants.NODESET);
				if ((applicablePlatforms != null) && applicablePlatforms.getLength() > 0) {
					for (int k = 0; k < applicablePlatforms.getLength(); k++) {
						Element applicablePlatform = (Element) applicablePlatforms.item(k);
						NodeList languageNodes = (NodeList) xPath.evaluate("Language", applicablePlatform,
								XPathConstants.NODESET);
						for (int l = 0; l < languageNodes.getLength(); l++) {
							Element languageNode = (Element) languageNodes.item(l);
							languages.add(languageNode.getAttribute("Name"));
						}
						NodeList technologyNodes = (NodeList) xPath.evaluate("Technology", applicablePlatform,
								XPathConstants.NODESET);
						for (int l = 0; l < technologyNodes.getLength(); l++) {
							Element technologyNode = (Element) technologyNodes.item(l);
							technologies.add(technologyNode.getAttribute("Name"));
						}
						NodeList operatingSystemNodes = (NodeList) xPath.evaluate("Operating_System",
								applicablePlatform, XPathConstants.NODESET);
						for (int l = 0; l < operatingSystemNodes.getLength(); l++) {
							Element operatingSystemNode = (Element) operatingSystemNodes.item(l);
							operatingSystems.add(operatingSystemNode.getAttribute("Name"));
						}
						NodeList architectureNodes = (NodeList) xPath.evaluate("Architecture", applicablePlatform,
								XPathConstants.NODESET);
						for (int l = 0; l < architectureNodes.getLength(); l++) {
							Element architectureNode = (Element) architectureNodes.item(l);
							architectures.add(architectureNode.getAttribute("Name"));
						}
					}
				}

				CWE cwe = new CWE("CWE-" + id, name, description, extendedDescription, new HashSet<>(), new HashSet<>(),
						languages, technologies, operatingSystems, architectures);

				NodeList parents = (NodeList) xPath.evaluate("Related_Weaknesses/Related_Weakness[@Nature='ChildOf']",
						weakness, XPathConstants.NODESET);
				if ((parents != null) && parents.getLength() > 0) {
					for (int k = 0; k < parents.getLength(); k++) {
						String parentCWE = xPath.evaluate("@CWE_ID", parents.item(k));
						cwe.addParent(parentCWE);
					}
				}

				cwes.put(cwe.getCweID(), cwe);

			}
			attachChildren();
			logger.info("[INFO] Cargados " + this.cwes.size() + " CWEs.");
			this.roots = this.extractRoots();
		} catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException ex) {
			ex.printStackTrace();
		}
	}

	private void attachChildren() {
		for (CWE cwe : cwes.values()) {
			for (String parentCWE : cwe.getParents()) {
				CWE parent = cwes.get("CWE-"+parentCWE);
				if (parent != null) {
					parent.addChild(cwe.getCweID());
				}
			}
		}
	}
	
	public List<CWE> getAllCWE(){
	    List <CWE> result = null;
	    try {
	        result  = StreamSupport.stream(cweRepository.findAll().spliterator(), false)
	                .collect(Collectors.toList());
	    }
	    catch (IllegalArgumentException e) {
	        System.err.println("Error al recuperar los datos: " + e.getMessage());
	        throw new IllegalArgumentException("Error al recuperar los datos", e);
	    }
	    catch (Exception e) {
	        System.err.println("Error inesperado: " + e.getMessage());
	    }
	    return result;
	}

	public CWE findByCWEID(String cweID) {
		return cwes.get(cweID);
	}

	public CWE findByName(String pattern) {
		Pattern p = Pattern.compile(pattern);
		for (CWE cwe : cwes.values()) {
			Matcher matcher = p.matcher(cwe.getName());
			if (matcher.matches()) {
				return cwe;
			}
		}
		return null;
	}

	public Collection<CWE> findAll() {
		return this.cwes.values();
	}

	public List<CWE> getParents(String cweId) {
		List<String> parentIDs = this.getParentIDs(cweId);
		if ((parentIDs != null) && !parentIDs.isEmpty()) {
			List<CWE> result = new ArrayList<>();
			for (String parentID : parentIDs) {
				CWE parent = this.cwes.get(parentID);
				if (parent != null) {
					result.add(parent);
				}
			}
			return result;
		} else {
			return Collections.emptyList();
		}
	}

	public List<String> getParentIDs(String cweId) {
		CWE cwe = this.cwes.get(cweId);
		if (cwe != null) {
			return new ArrayList<>(cwe.getParents());
		} else {
			return Collections.emptyList();
		}
	}

	public List<String> getChildren(String cweId) {
		List<String> childrenIDs = this.getChildrenIDs(cweId);
		if ((childrenIDs != null) && !childrenIDs.isEmpty()) {
			List<String> result = new ArrayList<>();
			for (String childID : childrenIDs) {
				CWE child = this.cwes.get(childID);
				if (this.cwes.get(childID) != null) {
					result.add(child.getCweID());
				}
			}
			return result;
		} else {
			return Collections.emptyList();
		}
	}

	public List<String> getChildrenIDs(String cweId) {
		CWE cwe = this.cwes.get(cweId);
		if (cwe != null) {
			return new ArrayList<>(cwe.getChildren());
		} else {
			return Collections.emptyList();
		}
	}

	public List<String> findRootIDs() {
		List<String> result = new ArrayList<>();

		for (CWE cwe : this.findRoots()) {
			result.add(cwe.getCweID());
		}

		return result;
	}
	
	public List<CWE> findRoots() {
		if (this.roots == null) {
			this.roots = this.extractRoots();
		}
		return this.roots;
	}

	public List<CWE> findRoots(int pageNumber, int pageSize) {
		int startIndex = pageNumber * pageSize;
	    int endIndex = startIndex + pageSize;
		if (this.roots == null) {
			this.roots = this.extractRoots(pageNumber, pageSize);
		}
		if (this.roots.size() > endIndex) {
	        return this.roots.subList(startIndex, endIndex);
	    } else if (this.roots.size() > startIndex) {
	        return this.roots.subList(startIndex, this.roots.size());
	    } else {
	        return new ArrayList<>();
	    }
	}

	private List<CWE> extractRoots() {
		List<CWE> result = new ArrayList<>();

		for (CWE cwe : this.cwes.values()) {
			Set<String> parents = cwe.getParents();
			if ((parents == null) || parents.isEmpty()) {
				if (!cwe.getName().startsWith("DEPRECATED")) {
					result.add(cwe);
				}
			}else {
				result.add(cwe);
			}
		}
		return result;
	}
	
	private List<CWE> extractRoots(int pageNumber, int pageSize) {
	    List<CWE> result = new ArrayList<>();
	    int startIndex = pageNumber * pageSize;
	    int endIndex = startIndex + pageSize;

	    for (CWE cwe : this.cwes.values()) {
	        Set<String> parents = cwe.getParents();
	        if ((parents == null) || parents.isEmpty()) {
	            if (!cwe.getName().startsWith("DEPRECATED")) {
	                result.add(cwe);
	            }
	        } else {
	            result.add(cwe);
	        }
	    }

	    if (result.size() > endIndex) {
	        return result.subList(startIndex, endIndex);
	    } else if (result.size() > startIndex) {
	        return result.subList(startIndex, result.size());
	    } else {
	        return new ArrayList<>();
	    }
	}

	public List<String> getAncestorIDs(String cweId) {
		CWE cwe = this.cwes.get(cweId);
		if (cwe != null) {
			List<String> result = new ArrayList<>();
			Stack<String> toProcess = new Stack<>();
			toProcess.push(cwe.getCweID());
			// Recorrido "primero en profundidad" invertido
			while (!toProcess.empty()) {
				String currentID = toProcess.pop();
				CWE current = this.cwes.get(currentID);
				if (current != null) {
					if (!current.getParents().isEmpty()) { // Anadir los padres a la Pila
						for (String parentID : current.getParents()) {
							if (!toProcess.contains(parentID) && !result.contains(parentID)) { // Evita repetidos
								toProcess.push(parentID);
							}
							result.add(parentID);
						}
					}
				}
			}
			return result;
		} else {
			return Collections.emptyList();
		}
	}

	public long countAllCVEs() {
		return cwes.size();
	}
	
}
