package practica_clase;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Ejercicio3 {

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

        // ejercicio 3: obtener en formato json los coches vendidos por un precio superior al introducido

        Scanner sc = new Scanner(System.in);
        // pedimos al usuario el precio minimo para filtrar
        System.out.print("ejercicio 3 - introduce el precio minimo de venta: ");
        double precioMinimo = sc.nextDouble();

        // creamos la conexion con mongodb
        MongoClient mongoClient = MongoClients.create();

        try {
            // seleccionamos la base de datos concesionario
            MongoDatabase database = mongoClient.getDatabase("Concesionario");

            // obtenemos las colecciones de ventas y coches
            MongoCollection<Document> colVentas = database.getCollection("Ventas");
            MongoCollection<Document> colCoches = database.getCollection("Coches");

            // cursor para recorrer las ventas
            MongoCursor<Document> cursorVentas = colVentas.find().iterator();

            int coincidencias = 0;
            // conjunto para evitar mostrar coches duplicados si se vendieron mas de una vez
            Set<Integer> cochesMostrados = new HashSet<Integer>();

            System.out.println("--- coches vendidos con precio mayor a " + precioMinimo + " (en json) ---");

            // recorremos cada venta
            while (cursorVentas.hasNext()) {
                Document docVenta = cursorVentas.next();
                int idCar = docVenta.getInteger("id_car");

                // determinamos el precio de venta (desde el campo de la venta o desde el coche)
                double precioVentaReal = 0;
                if (docVenta.containsKey("precioVenta")) {
                    precioVentaReal = extraerNumero(docVenta, "precioVenta");
                } else if (docVenta.containsKey("precio")) {
                    precioVentaReal = extraerNumero(docVenta, "precio");
                } else {
                    // si la venta no tiene precio propio buscamos el valor del coche
                    Document cocheBase = colCoches.find(Filters.eq("id_car", idCar)).first();
                    if (cocheBase != null) {
                        if (cocheBase.containsKey("Value")) {
                            precioVentaReal = extraerNumero(cocheBase, "Value");
                        } else if (cocheBase.containsKey("Vallue")) {
                            precioVentaReal = extraerNumero(cocheBase, "Vallue");
                        }
                    }
                }

                // comprobamos si el precio supera el valor pedido
                if (precioVentaReal > precioMinimo && !cochesMostrados.contains(idCar)) {
                    cochesMostrados.add(idCar);

                    // buscamos el documento completo del coche
                    Document docCoche = colCoches.find(Filters.eq("id_car", idCar)).first();
                    if (docCoche != null) {
                        coincidencias++;
                        // mostramos toda la informacion del coche en formato json
                        System.out.println(docCoche.toJson());
                    }
                }
            }

            // cerramos el cursor de ventas
            cursorVentas.close();

            if (coincidencias == 0) {
                System.out.println("no se encontraron coches vendidos por encima de ese precio");
            }

        } catch (Exception e) {
            // gestion basica de errores
            e.printStackTrace();
        } finally {
            // cerramos el escaner y la conexion
            sc.close();
            mongoClient.close();
        }
    }
}
