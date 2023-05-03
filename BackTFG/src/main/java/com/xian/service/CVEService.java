package com.xian.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.xian.model.CVE;
import com.xian.model.CVE.Configuration;
import com.xian.model.CVE.Reference;
import com.xian.repository.CVERepository;

import jakarta.annotation.PostConstruct;

@Service
public class CVEService {

	private final static Logger logger = LoggerFactory.getLogger(CVEService.class);

	@Autowired
	CVERepository cveRepository;

	@Autowired
	CWEService cweService;

	@Value("${cve.json.path}")
	Resource resourceFile;

	@Value("file:C:/Users/xian.jimenez/Documents/GitHub/TFG/BackTFG/cve-resources/nvdcve-1.1-2022.json")
	Resource resourceFile2022;

	@Value("file:../../../../cve-resources/nvdcve-1.1-2023.json")
	Resource resourceFile2023;

	@PostConstruct
	public void initialize() {
		System.out.println("Cargando CVEs... ");
		loadFromJSON(resourceFile2022);
	}

	public void loadFromJSON(Resource resourceFile) {
		try {
//	        File file = new File(resourceFile2022);
//	        JsonNode root = readJsonFile(file);
			File file = resourceFile.getFile();
			JsonNode root = readJsonFile(file);
			processJsonElements(root);
		} catch (IOException ex) {
			logger.error("[ERROR] Error loading CVEs from " + resourceFile2022, ex);
		}
	}

	private JsonNode readJsonFile(File file) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readTree(file);
	}

	private void processJsonElements(JsonNode root) {
		Integer cveCount = 0;
		Optional<CVE> cveCheck = Optional.of(new CVE());
		ArrayNode cveItems = (ArrayNode) root.get("CVE_Items");
		List<CVE> cvesToSave = new ArrayList<>();
		for (JsonNode entry : cveItems) {
			JsonNode cveElement = entry.get("cve");
			CVE cve = loadCVE(cveElement);
			cveCheck = cveRepository.findById(cve.getCveID());
			if (cveCheck != null && cveCheck.isEmpty()) {
				JsonNode configurationsElement = entry.get("configurations");
				List<CVE.Configuration> configurations = loadConfigurations(configurationsElement);
				cve.setConfigurations(configurations);

				for (CVE.Configuration configuration : configurations) {
					cve.getCpes().add(configuration.cpe23);
				}
				cve.setVendorProductPairs(extractVendorProductPairs(configurations));
				JsonNode impactElement = entry.get("impact");
				cve.setImpact(loadImpact(impactElement));
				try {
					cvesToSave.add(cve);
					cveCount++;
				} catch (IllegalArgumentException e) {
					logger.error("[ERROR] El ID del CVE es null. ", e);
				} catch (Exception e) {
					logger.error("[ERROR] No se ha podido guardar el CVE. ", e);
				}
			}
		}
		if (!cvesToSave.isEmpty()) {
			cveRepository.saveAll(cvesToSave);
		}
		System.out.println("Cargados " + cveCount + " CVEs.");
	}

	private List<String> extractVendorProductPairs(List<Configuration> configurations) {
		Set<String> result = new HashSet<>();
		for (CVE.Configuration configuration : configurations) {
			if (!result.contains(configuration.vendor + "/*")) {
				result.add(configuration.vendor + "/*");
			}
			result.add(configuration.vendor + "/" + configuration.product);
		}
		return new ArrayList<>(result);
	}

	private CVE loadCVE(JsonNode cveElement) {
		String id = cveElement.at("/CVE_data_meta/ID").asText();
		String assigner = cveElement.at("/CVE_data_meta/ASSIGNER").asText();
		String description = cveElement.at("/description/description_data").get(0).get("value").asText();
		CVE result = new CVE(id, assigner, description);
		JsonNode referencesJson = cveElement.at("/references/reference_data");
		if (referencesJson.isArray()) {
			result.setReferences(loadReferences((ArrayNode) referencesJson));
		}
		JsonNode cwesJson = cveElement.at("/problemtype/problemtype_data");
		if (cwesJson.isArray()) {
			List<String> cwes = loadCWEs((ArrayNode) cwesJson);
			result.setCwes(cwes);
			result.setCwesWithAncestors(expandCWEWithAncestors(cwes));
		}
		return result;
	}

	private List<String> expandCWEWithAncestors(List<String> cwes) {
		List<String> result = new ArrayList<>();
		for (String cwe : cwes) {
			result.addAll(cweService.getAncestorIDs(cwe));
		}
		return result;
	}

	private List<CVE.Reference> loadReferences(ArrayNode arrayNode) {
		List<CVE.Reference> result = new ArrayList<>();
		for (int i = 0; i < arrayNode.size(); i++) {
			JsonNode referenceElement = arrayNode.get(i);
			String url = referenceElement.get("url").asText();
			String source = referenceElement.get("refsource").asText();
			CVE.Reference reference = new Reference(url, source);

			JsonNode tagsNode = referenceElement.get("tags");
			if (tagsNode.isArray()) {
				reference.tags = new ArrayList<String>();
				ArrayNode tagsArray = (ArrayNode) tagsNode;
				for (int j = 0; j < tagsArray.size(); j++) {
					reference.addTag(tagsArray.get(j).asText());
				}
			}
			result.add(reference);
		}
		return result;
	}

	private List<String> loadCWEs(ArrayNode arrayNode) {
		List<String> result = new ArrayList<>();
		for (int i = 0; i < arrayNode.size(); i++) {
			JsonNode descriptionElement = arrayNode.get(i).get("description");
			if (descriptionElement.isArray()) {
				ArrayNode descriptionArray = (ArrayNode) descriptionElement;

				for (int j = 0; j < descriptionArray.size(); j++) {
					String lang = descriptionArray.get(j).get("lang").asText();
					String value = descriptionArray.get(j).get("value").asText();
					if (lang.equals("en")) {
						result.add(value);
					}
				}
			}
		}
		return result;
	}

	private List<CVE.Configuration> loadConfigurations(JsonNode configurationsElement) {
		List<CVE.Configuration> result = new ArrayList<>();
		JsonNode nodesElement = configurationsElement.get("nodes");

		if (nodesElement.isArray()) {
			ArrayNode arrayNode = (ArrayNode) nodesElement;
			for (int i = 0; i < arrayNode.size(); i++) {
				result.addAll(loadCPEMatches(arrayNode.get(i)));
			}
		}
		return result;
	}

	private List<CVE.Configuration> loadCPEMatches(JsonNode nodeElement) {
		List<CVE.Configuration> result = new ArrayList<>();
		ArrayNode cpeMatchArray = (ArrayNode) nodeElement.get("cpe_match");
		for (int i = 0; i < cpeMatchArray.size(); i++) {
			CVE.Configuration configuration = new CVE.Configuration(cpeMatchArray.get(i).get("cpe23Uri").asText());
			result.add(configuration);
		}
		ArrayNode childrenArray = (ArrayNode) nodeElement.get("children");
		for (int i = 0; i < childrenArray.size(); i++) {
			result.addAll(loadCPEMatches(childrenArray.get(i)));
		}

		return result;
	}

	private CVE.CVSS3Impact loadImpact(JsonNode impactElement) {
		CVE.CVSS3Impact impact = new CVE.CVSS3Impact();
		JsonNode baseMetricV3Element = impactElement.get("baseMetricV3");
		if (baseMetricV3Element != null) {
			impact.exploitabilityScore = baseMetricV3Element.get("exploitabilityScore").asDouble(0.0);
			impact.impactScore = baseMetricV3Element.get("impactScore").asDouble(0.0);
			impact.attackVector = baseMetricV3Element.at("/cvssV3/attackVector").asText("UNKNOWN");
			impact.attackComplexity = baseMetricV3Element.at("/cvssV3/attackComplexity").asText("UNKNOWN");
			impact.privilegesRequired = baseMetricV3Element.at("/cvssV3/privilegesRequired").asText("UNKNOWN");
			impact.userInteraction = baseMetricV3Element.at("/cvssV3/userInteraction").asText("UNKNOWN");
			impact.scope = baseMetricV3Element.at("/cvssV3/scope").asText("UNKNOWN");
			impact.confidentialityImpact = baseMetricV3Element.at("/cvssV3/confidentialityImpact").asText("UNKNOWN");
			impact.integrityImpact = baseMetricV3Element.at("/cvssV3/integrityImpact").asText("UNKNOWN");
			impact.availabilityImpact = baseMetricV3Element.at("/cvssV3/availabilityImpact").asText("UNKNOWN");
			impact.baseScore = baseMetricV3Element.at("/cvssV3/baseScore").asDouble(0.0);
			impact.baseSeverity = baseMetricV3Element.at("/cvssV3/baseSeverity").asText("UNKNOWN");

		}
		return impact;
	}

	public List<CVE> getAllCVE(){
	    List <CVE> result = null;
	    try {
	        result  = StreamSupport.stream(cveRepository.findAll().spliterator(), false)
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

	public CVE getCVEById(String id) {
		try {
			Optional<CVE> list = cveRepository.findById(id);
			if (list.isEmpty()) {
				logger.info("[INFO] No hay ningun CVE actualmente. ");
				return null;
			}
			return list.get();
		} catch (Exception e) {
			logger.error("[ERROR] No se ha podido recuperar los CVE: ", e);
			return null;
		}
	}

	public void saveCVE(CVE c) {
		try {
			cveRepository.save(c);
			logger.info("[INFO] Se ha guardado el CVE:{} ", c);

		} catch (Exception e) {
			logger.error("[ERROR] No se ha podido guardar el CVE:{} ", c, e);
		}
	}

}
