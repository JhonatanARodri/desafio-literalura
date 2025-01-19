package com.aluracursos.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Entity
@Table(name="libro")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String titulo;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List <Autor> autor;

    private List<String> idiomas;

    private Double numeroDescargas;

    public Libro(){}

    public Libro(DatosLibro datosLibro){
        this.titulo = datosLibro.titulo();
        this.autor = new ArrayList<>();
        datosLibro.autores().forEach(datosAutor -> this.autor.add(new Autor(datosAutor)));
        this.idiomas = new ArrayList<>();
        this.idiomas.addAll(datosLibro.idiomas());
        this.numeroDescargas = datosLibro.numeroDescargas();
    }

    @Override
    public String toString() {
        String autoresStr = autor.stream()
                .map(Autor::getNombre)
                .collect(Collectors.joining("; "));

        String idiomasStr = idiomas.stream()
                .collect(Collectors.joining("; "));

        String separador = "*".repeat(20+titulo.length()) + "\n";

        return  separador +
                "LIBRO" + "\n" +
                "Titulo: " + titulo + "\n" +
                "Autores: " + autoresStr + "\n" +
                "Idiomas: " + idiomasStr + "\n" +
                "Número de descargas: " + numeroDescargas + "\n" +
                separador;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutor() {
        return autor;
    }

    public void setAutor(List<Autor> autor) {
        this.autor = autor;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Double numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }
}
