package practica_clase;

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
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

public class Ejercicio2 {

    public static void main(String[] args) {
        // desactivamos los logs informativos de mongodb
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        // ejercicio 2: buscar a maria lopez martinez y mostrar matricula y fecha de venta ordenadas

        // creamos la conexion con mongodb
        MongoClient mongoClient = MongoClients.create();

        try {
            // seleccionamos la base de datos concesionario
            MongoDatabase database = mongoClient.getDatabase("Concesionario");

            // obtenemos las colecciones necesarias
            MongoCollection<Document> colPersonas = database.getCollection("Personas");
            MongoCollection<Document> colVentas = database.getCollection("Ventas");
            MongoCollection<Document> colCoches = database.getCollection("Coches");

            // filtro para buscar por nombre y apellidos
            Bson filtroPersona = Filters.and(
                Filters.eq("nombre", "María"),
                Filters.eq("apellidos", "López Martínez")
            );

            // buscamos a la persona en la coleccion
            Document docPersona = colPersonas.find(filtroPersona).first();

            if (docPersona == null) {
                // si no existe la persona informamos al usuario
                System.out.println("ejercicio 2 - no existe la persona maria lopez martinez en la coleccion");
            } else {
                // obtenemos el id de la persona
                int idPersona = docPersona.getInteger("id_persona");
                System.out.println("ejercicio 2 - persona encontrada: " + docPersona.getString("nombre") + " " + docPersona.getString("apellidos"));

                // buscamos las ventas de esta persona ordenadas por fecha de venta
                MongoCursor<Document> cursorVentas = colVentas.find(Filters.eq("id_persona", idPersona))
                    .sort(Sorts.ascending("fechaVenta"))
                    .iterator();

                if (!cursorVentas.hasNext()) {
                    // la persona no tiene ventas registradas
                    System.out.println("la persona encontrada no tiene ventas de coches registradas");
                } else {
                    System.out.println("--- ventas encontradas ordenadas por fecha ---");
                    // recorremos las ventas de la persona
                    while (cursorVentas.hasNext()) {
                        Document docVenta = cursorVentas.next();
                        Object fechaVenta = docVenta.get("fechaVenta");
                        int idCar = docVenta.getInteger("id_car");

                        // definimos la proyeccion con solo la matricula y sin _id
                        Bson proyeccionMatricula = Projections.fields(
                            Projections.include("Matrícula", "matricula", "Mstrícula"),
                            Projections.excludeId()
                        );

                        // buscamos el coche aplicando la proyeccion
                        Document docCoche = colCoches.find(Filters.eq("id_car", idCar))
                            .projection(proyeccionMatricula)
                            .first();

                        if (docCoche != null) {
                            // mostramos la proyeccion del coche en json y la fecha de venta
                            System.out.println("proyeccion coche: " + docCoche.toJson() + " | fecha de venta: " + fechaVenta);
                        } else {
                            System.out.println("coche id " + idCar + " no encontrado en catalogo | fecha de venta: " + fechaVenta);
                        }
                    }
                }
                // cerramos el cursor
                cursorVentas.close();
            }

        } catch (Exception e) {
            // gestion basica de errores
            e.printStackTrace();
        } finally {
            // cerramos la conexion
            mongoClient.close();
        }
    }
}
