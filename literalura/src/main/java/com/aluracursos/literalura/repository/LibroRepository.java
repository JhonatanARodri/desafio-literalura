package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Query("SELECT l FROM Libro l ORDER BY l.numeroDescargas DESC LIMIT 10")
    List<Libro> top10LibrosPorDescarga();

    @Query("SELECT l.idiomas FROM Libro l")
    List<String> listaIdiomas();

    @Query("SELECT COUNT(i) FROM Libro l JOIN l.idiomas i WHERE i=:idioma")
    List<String> librosPorIdioma(List<String> idioma);



}
