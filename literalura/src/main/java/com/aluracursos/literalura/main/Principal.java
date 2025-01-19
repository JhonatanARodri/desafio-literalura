package com.aluracursos.literalura.main;

import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Datos;

import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoApi;
import com.aluracursos.literalura.service.ConversionDatos;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private static final String URL_BUSQUEDA = "?search=";

    private ConsumoApi consumoApi = new ConsumoApi();

    private ConversionDatos conversionDatos = new ConversionDatos();

    private Scanner teclado = new Scanner(System.in);

    private List<Libro> librosConsultados;

    private List<Autor> autoresConsultados;

    private List<String> idiomasConsultados;

    private LibroRepository libroRepository;

    private AutorRepository autorRepository;


    public Principal(LibroRepository libroRepository, AutorRepository autorRepository){
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraMenu (){
        var opcion = -1;
        while (opcion != 0){

            var decoracion = "*".repeat(20) +"\n";

            var menu= decoracion +
                    """
                    Bienvenido al desafio LiterAlura
                    1. Buscar libro por titulo
                    2. Listar libros almacenados
                    3. Listar autores almacenados
                    4. Listar autores vivos en un año
                    5. Listar libros por idioma
                    6. Listar TOP 10 libros más descargados
                    7. Encontar autor por nombre
                    8. Encnotar autor por año de nacimiento
                    9. Encontar autor por año de fallecimiento
                    0. Salir de la aplicación
                    """ +
                    decoracion;

            var despedida = "Cerrando la aplciación";
            var errorMenu = "Opción inválida";

            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    encontarLibroPorTitulo();
                    break;
                case 2:
                    historialLibros();
                    break;
                case 3:
                    historialAutores();
                    break;
                case 4:
                    autoresPorFecha();
                    break;
                case 5:
                    idiomasConsultados();
                    break;
                case 0:
                    System.out.println(despedida);
                    break;
                default:
                    System.out.println(errorMenu);
            }
        }
    }

    private Datos buscarDatosLibro(String nombreTitulo) {
        String urlBusqueda = URL_BASE + URL_BUSQUEDA + nombreTitulo.replace(" ","%20");
        var json = consumoApi.obtenerDatos(urlBusqueda);
        Datos datos = conversionDatos.obtenerDatos(json, Datos.class);
        return datos;
    }

    private void encontarLibroPorTitulo() {
        System.out.println("Escriba el titulo que desea buscar");
        var nombreTitulo = teclado.nextLine();
        Datos datos = buscarDatosLibro(nombreTitulo);
        librosConsultados = libroRepository.findAll();

        if (datos.libros().isEmpty()){
            System.out.println("No se encontro ningún libro");
        }else if (datos.conteo() != 0){
            Libro libro = new Libro(datos.libros().get(0));
            Autor autor = new Autor(datos.libros().get(0).autores().get(0));
            libro.setTitulo(datos.libros().get(0).titulo().length() > 240 ?
                            datos.libros().get(0).titulo().substring(0,240) :
                            datos.libros().get(0).titulo()
                    );

            if (librosConsultados.stream()
                    .anyMatch(l -> l.getTitulo().equalsIgnoreCase(libro.getTitulo()))){
                System.out.println("El libro ya se encuentra en la base de datos");
                mensajeEspera();
            } else {
                libroRepository.save(libro);
            }
            System.out.println(libro);
            mensajeEspera();
        }
    }

    private void historialLibros() {
        try {
            librosConsultados = libroRepository.findAll();
            if(librosConsultados.isEmpty()){
                System.out.println("No hay libros registrados");

            } else {
                librosConsultados.forEach(System.out::println);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mensajeEspera();

    }

    private void historialAutores(){
        try {
            autoresConsultados = autorRepository.findAll();
            if (autoresConsultados.isEmpty()){
                System.out.println("No hay autores resgistrados");
            } else {
                autoresConsultados.forEach(System.out::println);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mensajeEspera();
    }

    private void autoresPorFecha() {
        System.out.println("Ingrese el año de consulta");
        var fecha = teclado.nextInt();
        autoresConsultados = autorRepository.autorPorFecha(fecha);
        if(autoresConsultados.isEmpty()){
            System.out.println("No se encontraros autores para la fecha solicitada");
        } else {
            autoresConsultados.forEach(System.out::println);
        }
        mensajeEspera();
    }

    private void idiomasConsultados(){
        idiomasConsultados = libroRepository.listaIdiomas().stream()
                .distinct()
                .collect(Collectors.toList());

        List<String> idiomasNumerados = IntStream.range(0,idiomasConsultados.size())
                        .mapToObj(i -> (i+1) + "." + idiomasConsultados.get(i))
                        .toList();

        System.out.println("Seleciona el idioma que desea consultar");
        idiomasNumerados.forEach(System.out::println);
        var idioma = teclado.nextInt();
        String idiomaConsulta = idiomasConsultados.get(idioma-1);
        List<String> cuentaLibrosPorIdioma = libroRepository.librosPorIdioma(idiomaConsulta);

        System.out.println("Se encuentran" + cuentaLibrosPorIdioma + " libros con el idioma selecionado");

    }

    private void mensajeEspera(){
        System.out.println("Presione enter para continuar");
        teclado.nextLine();
    }


}
