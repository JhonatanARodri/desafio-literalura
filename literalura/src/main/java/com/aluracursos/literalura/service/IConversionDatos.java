package com.aluracursos.literalura.service;

public interface IConversionDatos {

    <T> T obtenerDatos(String json, Class<T> clase);

}
