package exercicis_ampliacio;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Ejercicio1_Ampliacion {

    public static void main(String[] args) {
        // desactivamos los logs informativos de mongodb
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        // ejercicio 1 de ampliacion: obtener propietarios de london y mapear a arraylist de pojo persona con gson

        // creamos la conexion con mongodb
        MongoClient mongoClient = MongoClients.create();

        try {
            // seleccionamos la base de datos concesionario
            MongoDatabase database = mongoClient.getDatabase("Concesionario");

            // obtenemos la coleccion personas
            MongoCollection<Document> colPersonas = database.getCollection("Personas");

            // filtro para obtener las personas de la ciudad london (ignorando mayusculas y minusculas)
            Bson filtroLondon = Filters.regex("City", "^London$", "i");

            // instanciamos el arraylist de tipo persona y el objeto gson
            List<Persona> listaPersonas = new ArrayList<Persona>();
            Gson gson = new Gson();

            // obtenemos el cursor para recorrer los documentos encontrados
            MongoCursor<Document> cursor = colPersonas.find(filtroLondon).iterator();

            // recorremos cada documento de la base de datos
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                // convertimos el documento de mongodb a formato json
                String jsonPersona = doc.toJson();

                // convertimos el json a objeto java de la clase pojo persona mediante gson
                Persona p = gson.fromJson(jsonPersona, Persona.class);

                // agregamos la persona a la lista de objetos
                listaPersonas.add(p);
            }

            // cerramos el cursor de la consulta
            cursor.close();

            // mostramos el resultado recorriendo el arraylist
            System.out.println("--- ejercicio 1 ampliacion: personas de london encontradas (" + listaPersonas.size() + ") ---");
            for (Persona persona : listaPersonas) {
                // mostramos los datos de cada persona por consola
                System.out.println(persona);
            }

        } catch (Exception e) {
            // gestion basica de errores
            e.printStackTrace();
        } finally {
            // cerramos la conexion con mongodb
            mongoClient.close();
        }
    }
}
