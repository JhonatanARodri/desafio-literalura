package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository  extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.nombre=:nombreAutor")
    Autor autorPorNombre(String nombreAutor);

    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <=:fecha AND a.fechaFallecimiento >=:fecha")
    List<Autor> autorPorFecha(Integer fecha);

}
