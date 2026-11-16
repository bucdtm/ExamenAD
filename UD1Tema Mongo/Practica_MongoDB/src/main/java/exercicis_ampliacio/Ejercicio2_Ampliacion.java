package exercicis_ampliacio;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Ejercicio2_Ampliacion {

    public static void main(String[] args) {
        // desactivamos los logs informativos de mongodb
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        // ejercicio 2 de ampliacion: insertar platos desde json a la base de datos cocina coleccion platos

        // creamos la conexion con mongodb
        MongoClient mongoClient = MongoClients.create();

        try {
            // conectamos a la base de datos cocina
            MongoDatabase database = mongoClient.getDatabase("Cocina");

            // obtenemos la coleccion platos
            MongoCollection<Document> colPlatos = database.getCollection("platos");

            // vaciamos la coleccion previa si es necesario para evitar duplicados
            colPlatos.deleteMany(new Document());

            // buscamos el archivo platos.json en las rutas habituales del proyecto
            File archivoJson = new File("datos_json/Platos.json");
            if (!archivoJson.exists()) {
                archivoJson = new File("C:/Users/Admin/Downloads/AD/Practica_MongoDB/datos_json/Platos.json");
            }

            JsonArray arrayPlatos;

            // comprobamos si el archivo existe en el disco
            if (archivoJson.exists()) {
                // leemos el archivo json utilizando gson
                FileReader reader = new FileReader(archivoJson);
                arrayPlatos = JsonParser.parseReader(reader).getAsJsonArray();
                reader.close();
                System.out.println("ejercicio 2 ampliacion - leyendo archivo: " + archivoJson.getAbsolutePath());
            } else {
                // json de respaldo con los mismos datos de la practica si no encuentra la ruta
                String jsonTexto = "[\n" +
                        "  {\n" +
                        "    \"nombre\": \"Gazpacho\",\n" +
                        "    \"tipo\": \"Sopa fría\",\n" +
                        "    \"ingredientes\": [\n" +
                        "      { \"nombre\": \"Tomate\", \"cantidad\": \"1 kg\" },\n" +
                        "      { \"nombre\": \"Pimiento verde\", \"cantidad\": \"100 g\" }\n" +
                        "    ]\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"nombre\": \"Tortilla española\",\n" +
                        "    \"tipo\": \"Primero\",\n" +
                        "    \"ingredientes\": [\n" +
                        "      { \"nombre\": \"Patata\", \"cantidad\": \"500 g\" },\n" +
                        "      { \"nombre\": \"Huevo\", \"cantidad\": \"4 ud\" }\n" +
                        "    ]\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"nombre\": \"Ensalada\",\n" +
                        "    \"tipo\": \"Entrante\",\n" +
                        "    \"ingredientes\": [\n" +
                        "      { \"nombre\": \"Tomate\", \"cantidad\": \"700 kg\" },\n" +
                        "      { \"nombre\": \"lechuga\", \"cantidad\": \"100 g\" }\n" +
                        "    ]\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"nombre\": \"Bizcocho\",\n" +
                        "    \"tipo\": \"postre\",\n" +
                        "    \"ingredientes\": [\n" +
                        "      { \"nombre\": \"Harina\", \"cantidad\": \"1 kg\" },\n" +
                        "      { \"nombre\": \"Huevo\", \"cantidad\": \"5 ud\" }\n" +
                        "    ]\n" +
                        "  }\n" +
                        "]";
                arrayPlatos = JsonParser.parseString(jsonTexto).getAsJsonArray();
                System.out.println("ejercicio 2 ampliacion - usando json integrado de platos");
            }

            // lista de documentos bson para insertar
            List<Document> listaDocs = new ArrayList<Document>();

            // recorremos cada plato del array json de gson
            for (JsonElement element : arrayPlatos) {
                // transformamos el json del plato a documento bson de mongodb
                Document docPlato = Document.parse(element.toString());
                listaDocs.add(docPlato);
            }

            // insertamos todos los documentos de los platos en la base de datos
            if (!listaDocs.isEmpty()) {
                colPlatos.insertMany(listaDocs);
                System.out.println("se han insertado correctamente " + listaDocs.size() + " platos en la coleccion 'platos'");
            }

            // mostramos los platos guardados consultando directamente la base de datos
            System.out.println("\n--- documentos en la base de datos 'Cocina' (coleccion 'platos') ---");
            MongoCursor<Document> cursor = colPlatos.find().iterator();
            while (cursor.hasNext()) {
                Document platoGuardado = cursor.next();
                System.out.println(platoGuardado.toJson());
            }
            cursor.close();

        } catch (Exception e) {
            // gestion basica de errores
            e.printStackTrace();
        } finally {
            // cerramos la conexion con mongodb
            mongoClient.close();
        }
    }
}
