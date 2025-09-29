package com.yourpkg.service.systemconfigurations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubs.hni.data.jpa.repositories.FeatureConfigurationRepository;
import com.ubs.hni.data.jpa.services.FeatureConfigurationJpaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class MandatorConfigurationService {

    private final SimpleSecurityContextProvider simpleSecurityContextProvider;
    private final FeatureConfigurationJpaService featureConfigurationJpaService;
    private final MandatorConfigurationFilterUtil mandatorConfigurationFilterUtil;
    private final ObjectMapper objectMapper;

    /** Yalnızca orchestrator: sistemi okur, get... metotlarını çağırır, entries’i döner. */
    public SystemConfiguration.MandatorConfiguration loadMandatorConfiguration(StopWatches watches) {
        final String mandator = simpleSecurityContextProvider.getMandator();
        final String location = simpleSecurityContextProvider.getCurrentUserLocation();
        LOGGER.info("Get mandator configuration for mandator : {} and location : {}", mandator, location);

        var cfg = new SystemConfiguration.MandatorConfiguration();
        cfg.getEntries().add(getLanguagesForLocation(location, watches));            // LANGUAGE
        cfg.getEntries().add(getDocumentOrganizationForLocation(location, watches)); // ORGANIZATION
        return cfg;
    }

    /* ======================= LANGUAGE ======================= */
    private SystemConfiguration.MandatorConfiguration.Entry getLanguagesForLocation(String location, StopWatches watches) {
        JsonNode full = loadConfigValue(FeatureConfigurationRepository.ConfigurationKey.LOCATION_LANGUAGES, watches);
        // DEFAULT fallback’lı seçim (util)
        JsonNode selected = mandatorConfigurationFilterUtil
                .getLocationSpecificConfigurationOrDefault(full, safeLocation(location));
        return buildEntry(FeatureConfigurationRepository.ConfigurationKey.LOCATION_LANGUAGES.name(), selected);
    }

    /* =================== DOCUMENT ORGANIZATION =================== */
    private SystemConfiguration.MandatorConfiguration.Entry getDocumentOrganizationForLocation(String location, StopWatches watches) {
        JsonNode full = loadConfigValue(FeatureConfigurationRepository.ConfigurationKey.DOCUMENT_ORGANIZATION, watches);
        // DEFAULT yok → servis içinde strict seçim (eşleşme yoksa exception)
        JsonNode selected = selectOrganizationStrict(full, safeLocation(location));
        return buildEntry(FeatureConfigurationRepository.ConfigurationKey.DOCUMENT_ORGANIZATION.name(), selected);
    }

    /* =================== ORTAK YARDIMCILAR =================== */

    /** DB’den jsonb config’i çeker; yoksa açıklayıcı exception fırlatır. */
    private JsonNode loadConfigValue(FeatureConfigurationRepository.ConfigurationKey key, StopWatches watches) {
        Optional<JsonNode> opt = featureConfigurationJpaService.getValue(key, watches.getDbStopWatch());
        return opt.orElseThrow(() ->
                new BackendException("No record found in FEATURE_CONFIGURATION with key " + key.name()));
    }

    /** Entry oluşturur ve payload’ı JSON-string olarak set eder (UI geri uyumluluğu). */
    private SystemConfiguration.MandatorConfiguration.Entry buildEntry(String key, JsonNode payload) {
        var entry = new SystemConfiguration.MandatorConfiguration.Entry();
        entry.setKey(key);
        try {
            entry.setValue(objectMapper.writeValueAsString(payload)); // String olarak döndürme
        } catch (JsonProcessingException e) {
            throw new BackendException("JSON serialize error for key " + key, e);
        }
        return entry;
    }

    /** Organization için DEFAULT’suz (strict) seçim: sadece location eşleşirse döner. */
    private JsonNode selectOrganizationStrict(JsonNode json, String location) {
        if (json == null || !json.isArray() || json.isEmpty()) {
            throw new BackendException("Invalid/empty DOCUMENT_ORGANIZATION config");
        }
        for (JsonNode node : json) {
            JsonNode locs = node.get("Location");
            if (locs == null || !locs.isArray()) continue;
            for (JsonNode l : locs) {
                if (location.equalsIgnoreCase(l.asText())) {
                    return node;
                }
            }
        }
        throw new BackendException("No DOCUMENT_ORGANIZATION config for location: " + location);
    }

    /** Null/boş lokasyon için güvenli normalizasyon. */
    private static String safeLocation(String loc) {
        return (loc == null || loc.isBlank()) ? "DEFAULT" : loc.trim();
    }
}
