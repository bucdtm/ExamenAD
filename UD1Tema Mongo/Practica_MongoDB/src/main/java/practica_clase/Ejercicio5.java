package practica_clase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class Ejercicio5 {

    // metodo auxiliar para obtener el valor numerico de forma segura
    private static double extraerNumero(Document doc, String clave) {
        Object valor = doc.get(clave);
        if (valor instanceof Number) {
            return ((Number) valor).doubleValue();
        }
        return 0;
    }

    public static void main(String[] args) {
        // desactivamos los logs informativos de mongodb
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        // ejercicio 5: persona que mas dinero ha gastado mostrando proyeccion con nombre y apellidos

        // creamos la conexion con mongodb
        MongoClient mongoClient = MongoClients.create();

        try {
            // seleccionamos la base de datos concesionario
            MongoDatabase database = mongoClient.getDatabase("Concesionario");

            // obtenemos las colecciones necesarias
            MongoCollection<Document> colVentas = database.getCollection("Ventas");
            MongoCollection<Document> colCoches = database.getCollection("Coches");
            MongoCollection<Document> colPersonas = database.getCollection("Personas");

            // mapa para acumular el total gastado por cada id de persona
            Map<Integer, Double> gastoPorPersona = new HashMap<Integer, Double>();

            // cursor para recorrer todas las ventas
            MongoCursor<Document> cursorVentas = colVentas.find().iterator();

            while (cursorVentas.hasNext()) {
                Document docVenta = cursorVentas.next();
                if (!docVenta.containsKey("id_persona")) {
                    continue;
                }

                int idPersona = docVenta.getInteger("id_persona");
                int idCar = docVenta.getInteger("id_car");

                // obtenemos el precio del vehiculo
                double precio = 0;
                if (docVenta.containsKey("precioVenta")) {
                    precio = extraerNumero(docVenta, "precioVenta");
                } else if (docVenta.containsKey("precio")) {
                    precio = extraerNumero(docVenta, "precio");
                } else {
                    Document docCoche = colCoches.find(Filters.eq("id_car", idCar)).first();
                    if (docCoche != null) {
                        if (docCoche.containsKey("Value")) {
                            precio = extraerNumero(docCoche, "Value");
                        } else if (docCoche.containsKey("Vallue")) {
                            precio = extraerNumero(docCoche, "Vallue");
                        }
                    }
                }

                // sumamos al acumulado de la persona
                double acumulado = gastoPorPersona.containsKey(idPersona) ? gastoPorPersona.get(idPersona) : 0;
                gastoPorPersona.put(idPersona, acumulado + precio);
            }

            // cerramos el cursor de ventas
            cursorVentas.close();

            if (gastoPorPersona.isEmpty()) {
                System.out.println("ejercicio 5 - no hay ventas registradas en la base de datos");
            } else {
                // calculamos el importe maximo gastado
                double maxGasto = 0;
                for (double total : gastoPorPersona.values()) {
                    if (total > maxGasto) {
                        maxGasto = total;
                    }
                }

                // recopilamos los id de quienes hayan gastado esa cantidad maxima (por si hay empate)
                List<Integer> maxCompradores = new ArrayList<Integer>();
                for (Map.Entry<Integer, Double> entrada : gastoPorPersona.entrySet()) {
                    if (entrada.getValue() == maxGasto) {
                        maxCompradores.add(entrada.getKey());
                    }
                }

                System.out.println("--- ejercicio 5: persona(s) que mas dinero ha gastado ---");
                System.out.println("gasto total maximo: " + maxGasto + " €");

                // proyeccion que incluye nombre y apellidos y excluye _id
                Bson proyeccionPersona = Projections.fields(
                    Projections.include("nombre", "apellidos", "surname"),
                    Projections.excludeId()
                );

                // buscamos y mostramos la proyeccion de cada persona con el maximo gasto
                for (int idPersona : maxCompradores) {
                    Document docPersona = colPersonas.find(Filters.eq("id_persona", idPersona))
                        .projection(proyeccionPersona)
                        .first();

                    if (docPersona != null) {
                        System.out.println("proyeccion persona: " + docPersona.toJson());
                    } else {
                        System.out.println("persona id " + idPersona + " no encontrada en coleccion personas");
                    }
                }
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
