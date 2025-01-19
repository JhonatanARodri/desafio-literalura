package com.aluracursos.literalura.service;

import com.deepl.api.*;

public class ConsultaTraduccion {

    public static String obtenerTraduccion(String texto) {

        Translator translator;

        String authKey  = System.getenv("DEEPL_APIKEY");

        translator = new Translator(authKey);

        TextResult result =
                null;
        try {
            result = translator.translateText(texto, null, "ES");
        } catch (DeepLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return result.getText();

    }

}