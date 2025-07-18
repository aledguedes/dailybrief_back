package com.dailybrief.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class LocaleConfig {

    private static final Logger logger = LoggerFactory.getLogger(LocaleConfig.class);

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        List<Locale> supportedLocales = Arrays.asList(
                Locale.forLanguageTag("pt"),
                Locale.forLanguageTag("en"),
                Locale.forLanguageTag("es")
        );
        resolver.setSupportedLocales(supportedLocales);
        resolver.setDefaultLocale(Locale.forLanguageTag("pt"));
        logger.info("LocaleResolver configurado com idiomas suportados: {} e padrão: pt", supportedLocales);
        return resolver;
    }
}