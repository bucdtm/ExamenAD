package practica_clase;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Ejercicio1 {

    public static void main(String[] args) {
        // desactivamos los logs informativos de mongodb
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        // ejercicio 1: buscar el propietario y el anio de un coche por matricula

        Scanner sc = new Scanner(System.in);
        // pedimos al usuario la matricula por teclado
        System.out.print("ejercicio 1 - introduce la matricula del coche: ");
        String matriculaEntrada = sc.nextLine().trim();

        // creamos la conexion con mongodb
        MongoClient mongoClient = MongoClients.create();

        try {
            // seleccionamos la base de datos concesionario
            MongoDatabase database = mongoClient.getDatabase("Concesionario");

            // obtenemos las colecciones
            MongoCollection<Document> colCoches = database.getCollection("Coches");
            MongoCollection<Document> colPersonas = database.getCollection("Personas");
            MongoCollection<Document> colVentas = database.getCollection("Ventas");

            // filtro para buscar el coche por matricula
            Bson filtroCoche = Filters.or(
                Filters.eq("Matrícula", matriculaEntrada),
                Filters.eq("matricula", matriculaEntrada),
                Filters.eq("Mstrícula", matriculaEntrada)
            );

            // buscamos el coche en la coleccion
            Document docCoche = colCoches.find(filtroCoche).first();

            if (docCoche == null) {
                // el coche no existe en la base de datos
                System.out.println("no se ha encontrado ningun coche con la matricula: " + matriculaEntrada);
            } else {
                // extraemos el anio y el id del coche
                Object anio = docCoche.get("Year");
                Integer idCar = docCoche.getInteger("id_car");

                Document docPersona = null;

                // buscamos primero en la coleccion ventas para ver quien lo compro
                if (idCar != null) {
                    Document docVenta = colVentas.find(Filters.eq("id_car", idCar)).first();
                    if (docVenta != null && docVenta.get("id_persona") != null) {
                        int idPersona = docVenta.getInteger("id_persona");
                        docPersona = colPersonas.find(Filters.eq("id_persona", idPersona)).first();
                    }
                }

                // si no se encontro en ventas buscamos directamente en personas por el array de coches
                if (docPersona == null && idCar != null) {
                    docPersona = colPersonas.find(Filters.eq("coches", idCar)).first();
                }

                // mostramos los resultados
                System.out.println("--- resultado ejercicio 1 ---");
                System.out.println("año del coche: " + anio);

                if (docPersona != null) {
                    String nombre = docPersona.getString("nombre");
                    String apellidos = docPersona.getString("apellidos");
                    if (apellidos == null) {
                        apellidos = docPersona.getString("surname");
                    }
                    System.out.println("nombre y apellidos del propietario: " + nombre + " " + apellidos);
                } else {
                    // en caso de que el coche no tenga propietario registrado
                    System.out.println("este coche actualmente no tiene un propietario asignado");
                }
            }

        } catch (Exception e) {
            // gestion basica de excepciones
            e.printStackTrace();
        } finally {
            // cerramos el escaner y la conexion
            sc.close();
            mongoClient.close();
        }
    }
}
