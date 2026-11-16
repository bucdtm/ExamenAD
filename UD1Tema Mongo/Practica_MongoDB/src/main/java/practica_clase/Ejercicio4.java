package practica_clase;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Ejercicio4 {

    public static void main(String[] args) {
        // desactivamos los logs informativos de mongodb
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        // ejercicio 4: mostrar revisiones y comprador de coches modelo rolls

        // creamos la conexion con mongodb
        MongoClient mongoClient = MongoClients.create();

        try {
            // seleccionamos la base de datos concesionario
            MongoDatabase database = mongoClient.getDatabase("Concesionario");

            // obtenemos las colecciones necesarias
            MongoCollection<Document> colCoches = database.getCollection("Coches");
            MongoCollection<Document> colVentas = database.getCollection("Ventas");
            MongoCollection<Document> colPersonas = database.getCollection("Personas");

            // filtro para buscar los coches del modelo rolls
            Bson filtroRolls = Filters.regex("Model", "^Rolls$", "i");
            MongoCursor<Document> cursorRolls = colCoches.find(filtroRolls).iterator();

            System.out.println("--- ejercicio 4: listado de coches rolls ---");

            // recorremos cada coche rolls
            while (cursorRolls.hasNext()) {
                Document docCoche = cursorRolls.next();
                int idCar = docCoche.getInteger("id_car");
                Object matricula = docCoche.get("Matrícula");
                if (matricula == null) {
                    matricula = docCoche.get("matricula");
                }
                if (matricula == null) {
                    matricula = docCoche.get("Mstrícula");
                }

                System.out.println("\n-------------------------------------------");
                System.out.println("coche id: " + idCar + " | modelo: " + docCoche.getString("Model") + " | matricula: " + matricula);

                // mostramos las fechas de las revisiones que han pasado
                if (docCoche.containsKey("revisiones")) {
                    List<?> revisiones = docCoche.get("revisiones", List.class);
                    if (revisiones != null && !revisiones.isEmpty()) {
                        System.out.println("fechas de revisiones pasadas: " + revisiones);
                    } else {
                        System.out.println("fechas de revisiones pasadas: sin revisiones registradas");
                    }
                } else {
                    System.out.println("fechas de revisiones pasadas: no constan revisiones en el documento");
                }

                // comprobamos si el coche ha sido vendido buscando en la coleccion ventas
                Document docVenta = colVentas.find(Filters.eq("id_car", idCar)).first();

                if (docVenta != null && docVenta.get("id_persona") != null) {
                    int idPersona = docVenta.getInteger("id_persona");
                    // buscamos los datos de la persona compradora
                    Document docPersona = colPersonas.find(Filters.eq("id_persona", idPersona)).first();
                    if (docPersona != null) {
                        String nombre = docPersona.getString("nombre");
                        String apellidos = docPersona.getString("apellidos");
                        if (apellidos == null) {
                            apellidos = docPersona.getString("surname");
                        }
                        System.out.println("estado de venta: vendido a " + nombre + " " + apellidos);
                    } else {
                        System.out.println("estado de venta: vendido a persona con id " + idPersona);
                    }
                } else {
                    // comprobamos tambien si esta vinculado directamente en personas
                    Document docPersonaDirecta = colPersonas.find(Filters.eq("coches", idCar)).first();
                    if (docPersonaDirecta != null) {
                        String nombre = docPersonaDirecta.getString("nombre");
                        String apellidos = docPersonaDirecta.getString("apellidos");
                        if (apellidos == null) {
                            apellidos = docPersonaDirecta.getString("surname");
                        }
                        System.out.println("estado de venta: vendido a " + nombre + " " + apellidos);
                    } else {
                        System.out.println("estado de venta: este coche no ha sido vendido");
                    }
                }
            }

            // cerramos el cursor de coches rolls
            cursorRolls.close();

        } catch (Exception e) {
            // gestion basica de errores
            e.printStackTrace();
        } finally {
            // cerramos la conexion
            mongoClient.close();
        }
    }
}
