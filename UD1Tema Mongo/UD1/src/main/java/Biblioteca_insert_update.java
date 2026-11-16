import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;

public class Biblioteca_insert_update {
    @SuppressWarnings("unused")
    public static void main(String[] args) {

        MongoCursor<Document> cursor_libros = null;
        MongoCursor<Document> cursor_prestamos = null;
        MongoCursor<Document> cursor_socios = null;
        MongoCursor<Document> cursor = null;

        Document libro = null;
        Document prestamo = null;
        Document socio = null;
        Document doc = null;

        String cad;

        Bson bsonFilter = null;

        MongoClient mongoC = MongoClients.create();

        MongoDatabase db = mongoC.getDatabase("Biblioteca");
        MongoCollection<Document> col_libros = db.getCollection("Libros");
        MongoCollection<Document> col_prestamos = db.getCollection("Prestamos");
        MongoCollection<Document> col_socio = db.getCollection("Socios");

        try {
            // =========================================================================
            // EJERCICIOS CLASE (1 al 9)
            // =========================================================================

            // Ejercicio 1: Buscar el registro del primer libro (clase)
            System.out.println("--- Ejercicio 1: Primer libro ---");
            libro = col_libros.find().first();
            if (libro != null) {
                System.out.println("El primer libro es " + libro.toJson() + "\n");
                System.out.println("El ISBN del primer libro es " + libro.get("isbn") + "\n");
            }

            // Ejercicio 2: Buscar todos los libros (clase)
            System.out.println("--- Ejercicio 2: Todos los libros ---");
            cursor_libros = col_libros.find().iterator();
            while (cursor_libros.hasNext()) {
                System.out.println("Título: " + cursor_libros.next().get("titulo"));
            }
            System.out.println();

            // Ejercicio 3: Obtener la lista de socios (clase)
            System.out.println("--- Ejercicio 3: Lista de socios ---");
            cursor_socios = col_socio.find().iterator();
            while (cursor_socios.hasNext()) {
                System.out.println(cursor_socios.next().toJson());
            }
            System.out.println();

            // Ejercicio 4: Obtener el nombre de los socios (usar método get()) (clase)
            System.out.println("--- Ejercicio 4: Nombres de los socios ---");
            cursor_socios = col_socio.find().iterator();
            while (cursor_socios.hasNext()) {
                System.out.println("Nombre: " + cursor_socios.next().get("Nombre"));
            }
            System.out.println();

            // Ejercicio 5: Buscar el libro con titulo "El Quijote" (clase)
            System.out.println("--- Ejercicio 5: Buscar el libro 'El Quijote' ---");
            bsonFilter = Filters.eq("titulo", "El Quijote");
            cursor_libros = col_libros.find(bsonFilter).iterator();
            while (cursor_libros.hasNext()) {
                System.out.println(cursor_libros.next().toJson());
            }
            System.out.println();

            // Ejercicio 6: Buscar el préstamo con dos libros (clase)
            System.out.println("--- Ejercicio 6: Préstamo con 2 libros ---");
            bsonFilter = Filters.size("libro", 2);
            cursor_prestamos = col_prestamos.find(bsonFilter).iterator();
            while (cursor_prestamos.hasNext()) {
                System.out.println(cursor_prestamos.next().toJson());
            }
            System.out.println();

            // Ejercicio 7: Buscar título de libros de autor que se apellida Follet (clase)
            System.out.println("--- Ejercicio 7: Libros del autor Follet ---");
            bsonFilter = Filters.eq("autor.apellido", "Follet");
            cursor_libros = col_libros.find(bsonFilter).iterator();
            while (cursor_libros.hasNext()) {
                System.out.println("Título: " + cursor_libros.next().get("titulo"));
            }
            System.out.println();

            // Ejercicio 8: En prestamos buscar el documentos del préstamos del libro con ISBN 446854 (clase)
            System.out.println("--- Ejercicio 8: Préstamo del libro con ISBN 446854 ---");
            bsonFilter = Filters.eq("libro.isbn", "446854");
            cursor_prestamos = col_prestamos.find(bsonFilter).iterator();
            while (cursor_prestamos.hasNext()) {
                System.out.println(cursor_prestamos.next().toJson());
            }
            System.out.println();

            // Ejercicio 9: Buscar el nombre de los socios que han tenido prestado 456779 (clase)
            System.out.println("--- Ejercicio 9: Socios que tuvieron prestado 456779 ---");
            bsonFilter = Filters.eq("libro.isbn", "456779");
            cursor_prestamos = col_prestamos.find(bsonFilter).iterator();
            while (cursor_prestamos.hasNext()) {
                socio = (Document) cursor_prestamos.next().get("socio");
                cad = (String) socio.get("num_socio");
                Integer numero = Integer.parseInt(cad);

                Bson filtroSocio = Filters.eq("Num_socio", numero);
                Document socioDoc = col_socio.find(filtroSocio).first();
                if (socioDoc != null) {
                    System.out.println("Nombre: " + socioDoc.get("Nombre"));
                }
            }
            System.out.println();

            // =========================================================================
            // EJERCICIOS 10 AL 22
            // =========================================================================

            // Ejercicio 10: Buscar libros con año mayor que 2000 Y de más de 500 páginas
            System.out.println("--- Ejercicio 10: Libros > 2000 y > 500 páginas ---");
            bsonFilter = Filters.and(Filters.gt("año_publicacion", 2000), Filters.gt("paginas", 500));
            cursor_libros = col_libros.find(bsonFilter).iterator();
            while (cursor_libros.hasNext()) {
                System.out.println(cursor_libros.next().toJson());
            }
            System.out.println();

            // Ejercicio 11: Buscar libros editados entre 2010 y 2020
            System.out.println("--- Ejercicio 11: Libros entre 2010 y 2020 ---");
            bsonFilter = Filters.and(Filters.gte("año_publicacion", 2010), Filters.lte("año_publicacion", 2020));
            cursor_libros = col_libros.find(bsonFilter).iterator();
            while (cursor_libros.hasNext()) {
                System.out.println(cursor_libros.next().toJson());
            }
            System.out.println();

            // Ejercicio 12: Buscar los libros de la editorial «Debolsillo»
            System.out.println("--- Ejercicio 12: Libros de editorial 'Debolsillo' ---");
            bsonFilter = Filters.eq("editorial", "Debolsillo");
            cursor_libros = col_libros.find(bsonFilter).iterator();
            while (cursor_libros.hasNext()) {
                System.out.println(cursor_libros.next().toJson());
            }
            System.out.println();

            // Ejercicio 13: Mostrar el nombre del socio 2
            System.out.println("--- Ejercicio 13: Nombre del socio 2 ---");
            bsonFilter = Filters.eq("Num_socio", 2);
            socio = col_socio.find(bsonFilter).first();
            if (socio != null) {
                System.out.println("Nombre del socio 2: " + socio.get("Nombre"));
            }
            System.out.println();

            // Ejercicio 14: Buscar los libros (título, nombre autor) que sean de autores de nacionalidad americana o de la editorial planeta
            System.out.println("--- Ejercicio 14: Libros autor americano o editorial Planeta ---");
            bsonFilter = Filters.or(
                    Filters.eq("autor.nacionalidad", "americana"),
                    Filters.eq("editorial", "planeta")
            );
            cursor_libros = col_libros.find(bsonFilter)
                    .projection(Projections.fields(
                            Projections.include("titulo", "autor.nombre"),
                            Projections.excludeId()
                    ))
                    .iterator();
            while (cursor_libros.hasNext()) {
                System.out.println(cursor_libros.next().toJson());
            }
            System.out.println();

            // Ejercicio 15: Mostrar la fecha de préstamo del libro prestado al socio num_socio 1
            // Nota: En el Ejercicio 9 se observa que socio.num_socio en Prestamos está guardado como String ("1")
            System.out.println("--- Ejercicio 15: Fecha préstamo socio 1 ---");
            bsonFilter = Filters.eq("socio.num_socio", "1");
            cursor_prestamos = col_prestamos.find(bsonFilter).iterator();
            while (cursor_prestamos.hasNext()) {
                prestamo = cursor_prestamos.next();
                System.out.println("Fecha de préstamo: " + prestamo.get("fecha_prestamo"));
            }
            System.out.println();

            // Ejercicio 16: Buscar libros editados entre 1990 y 2000
            System.out.println("--- Ejercicio 16: Libros editados entre 1990 y 2000 ---");
            bsonFilter = Filters.and(
                    Filters.gte("año_publicacion", 1990),
                    Filters.lte("año_publicacion", 2000)
            );
            cursor_libros = col_libros.find(bsonFilter).iterator();
            while (cursor_libros.hasNext()) {
                System.out.println(cursor_libros.next().toJson());
            }
            System.out.println();

            // Ejercicio 17: Mostrar el título de los libros escritos por Isaac Asimov
            System.out.println("--- Ejercicio 17: Libros de Isaac Asimov ---");
            bsonFilter = Filters.eq("autor.apellido", "Asimov");
            cursor_libros = col_libros.find(bsonFilter).sort(Sorts.descending("titulo")).iterator();
            while (cursor_libros.hasNext()) {
                System.out.println(cursor_libros.next().toJson());
            }
            System.out.println();

            // Ejercicio 18: Mostrar el nombre de los socios que se ha llevado prestado el libro de título «El Quijote»
            System.out.println("--- Ejercicio 18: Socios que pidieron prestado 'El Quijote' ---");
            bsonFilter = Filters.eq("titulo", "El Quijote");
            cursor_libros = col_libros.find(bsonFilter).iterator();
            while (cursor_libros.hasNext()) {
                cad = (String) cursor_libros.next().get("isbn");
                bsonFilter = Filters.eq("libro.isbn", cad);
                cursor_prestamos = col_prestamos.find(bsonFilter).iterator();

                while (cursor_prestamos.hasNext()) {
                    socio = (Document) cursor_prestamos.next().get("socio");
                    cad = (String) socio.get("num_socio");
                    Integer numero = Integer.parseInt(cad);

                    bsonFilter = Filters.eq("Num_socio", numero);
                    socio = col_socio.find(bsonFilter).first();
                    if (socio != null) {
                        System.out.println("Nombre socio: " + socio.get("Nombre"));
                    }
                }
            }
            System.out.println();

            // Ejercicio 19: Mostrar el título de los libros que tiene prestados el socio con nombre «Javier García»
            System.out.println("--- Ejercicio 19: Libros prestados al socio 'Javier García' ---");
            bsonFilter = Filters.eq("Nombre", "Javier García");
            socio = col_socio.find(bsonFilter).first();
            if (socio != null) {
                String numSocioStr = String.valueOf(socio.get("Num_socio"));
                bsonFilter = Filters.eq("socio.num_socio", numSocioStr);
                cursor_prestamos = col_prestamos.find(bsonFilter).iterator();

                while (cursor_prestamos.hasNext()) {
                    prestamo = cursor_prestamos.next();
                    Object campoLibro = prestamo.get("libro");

                    if (campoLibro instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<Document> listaLibros = (List<Document>) campoLibro;
                        for (Document libDoc : listaLibros) {
                            String isbn = (String) libDoc.get("isbn");
                            libro = col_libros.find(Filters.eq("isbn", isbn)).first();
                            if (libro != null) {
                                System.out.println("Título: " + libro.get("titulo"));
                            }
                        }
                    } else if (campoLibro instanceof Document) {
                        String isbn = (String) ((Document) campoLibro).get("isbn");
                        libro = col_libros.find(Filters.eq("isbn", isbn)).first();
                        if (libro != null) {
                            System.out.println("Título: " + libro.get("titulo"));
                        }
                    }
                }
            }
            System.out.println();

            // Ejercicio 20: Mostrar el título y el autor de los libros de la editorial «planeta» ordenados por año de edición
            System.out.println("--- Ejercicio 20: Libros editorial 'planeta' ordenados por año ---");
            bsonFilter = Filters.eq("editorial", "planeta");
            cursor_libros = col_libros.find(bsonFilter)
                    .sort(Sorts.ascending("año_publicacion"))
                    .iterator();
            while (cursor_libros.hasNext()) {
                libro = cursor_libros.next();
                System.out.println("Título: " + libro.get("titulo") + " | Autor: " + libro.get("autor"));
            }
            System.out.println();

            // Ejercicio 21: Mostrar el título y la editorial (proyección) de los libros ordenados por número de páginas.
            System.out.println("--- Ejercicio 21: Proyección título y editorial por número de páginas ---");
            cursor_libros = col_libros.find()
                    .projection(Projections.fields(
                            Projections.include("titulo", "editorial"),
                            Projections.excludeId()
                    ))
                    .sort(Sorts.ascending("paginas"))
                    .iterator();
            while (cursor_libros.hasNext()) {
                System.out.println(cursor_libros.next().toJson());
            }
            System.out.println();

            // Ejercicio 22: Mostrar el título de los libros que tienen algún préstamo
            System.out.println("--- Ejercicio 22: Libros que tienen algún préstamo ---");
            cursor = col_prestamos.find().iterator();
            while (cursor.hasNext()) {
                ArrayList lib = (ArrayList) cursor.next().get("libro");
                for (Object lib_1 : lib) {
                    doc = (Document) lib_1;
                    cad = (String) doc.get("isbn");
                    System.out.println("isbn: " + cad);
                    bsonFilter = Filters.eq("isbn", cad);
                    doc = (Document) col_libros.find(bsonFilter).first();
                    if (doc != null) {
                        System.out.println("Ejer 22: " + doc.toJson());
                    }
                }
            }

            System.out.println();
            bsonFilter = Filters.eq("libro.isbn", "12345");
            col_libros.updateOne(bsonFilter,
                    new Document("$set", new Document("páginas", 1200)));

            bsonFilter = Filters.eq("libro.isbn", "12345");
            col_libros.updateMany(bsonFilter,
                    new Document("$set", new Document("páginas", 1200)));

            DeleteResult res = col_libros.deleteMany(bsonFilter);
            System.out.println(res);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor_libros != null) cursor_libros.close();
            if (cursor_prestamos != null) cursor_prestamos.close();
            if (cursor_socios != null) cursor_socios.close();
            if (cursor != null) cursor.close();
            mongoC.close();
        }
    }
}