package practica1;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Practica1 {

	public static void main(String[] args) {

		JSONParser parser = new JSONParser();
		JSONArray platosArray, ingredientesArray = null;
		JSONObject platos, ingrediente;

		int totalHuevosExistentes = 0, maxHuevosPorPlato = 0, platosCHuevo = 0, platosObjetivo, huevosNecesarios;

		try {
			platosArray = (JSONArray) parser.parse(new FileReader("Ficheros/Platos.json"));

			// 1. Obtener los ingredientes del gazpacho

			for (Object platosObj : platosArray) {
				platos = (JSONObject) platosObj;

				if ("Gazpacho".equals(platos.get("nombre"))) {
					System.out.println("Plato: " + platos.get("nombre"));

					ingredientesArray = (JSONArray) platos.get("ingredientes");

					for (Object ingredienteObj : ingredientesArray) {

						ingrediente = (JSONObject) ingredienteObj;
						System.out.println("Nombre Ingrediente: " + ingrediente.get("nombre"));

					}
					break;
				}
			}

			// 2. Obtener todos los platos (nombre) que tiene el ingrediente tomate

			for (Object platosObj : platosArray) {

				platos = (JSONObject) platosObj;
				ingredientesArray = (JSONArray) platos.get("ingredientes");

				for (Object ingredienteObj : ingredientesArray) {

					ingrediente = (JSONObject) ingredienteObj;

					if (ingrediente.get("nombre").equals("Tomate")) {
						System.out.println("\nPlato: " + platos.get("nombre"));

					}
				}
			}

			// 3. Obtener que platos se ofrecen como primero y que ingredientes tienen

			for (Object platosObj : platosArray) {
				platos = (JSONObject) platosObj;

				if ("Primero".equals(platos.get("tipo"))) {
					System.out.println("\nPlato: " + platos.get("nombre"));

					ingredientesArray = (JSONArray) platos.get("ingredientes");

					for (Object ingredienteObj : ingredientesArray) {

						ingrediente = (JSONObject) ingredienteObj;
						System.out.println("Nombre Ingrediente: " + ingrediente.get("nombre"));

					}
					break;
				}
			}

			// 4. Obtener que cantidad de huevos necesito tener para poder ofrecer al menos
			// 3 platos de los que necesitan huevo, ¿ Puedes extraer esta información, cual
			// es el problema, lo podrías solucionar?

			for (Object platosObj : platosArray) {
				
				platos = (JSONObject) platosObj;
				ingredientesArray = (JSONArray) platos.get("ingredientes");
				
				for (Object ingredienteObj : ingredientesArray) {

					ingrediente = (JSONObject) ingredienteObj;
					
					if("Huevo".equals(ingrediente.get("nombre"))) {
						String cantidadHuevosString = (String) ingrediente.get("cantidad");
						int cantidadHuevosInt = Integer.parseInt(cantidadHuevosString.split(" ")[0]);
						
						totalHuevosExistentes += cantidadHuevosInt;
						platosCHuevo++;
						
						if (cantidadHuevosInt > maxHuevosPorPlato) {
							maxHuevosPorPlato = cantidadHuevosInt;
						}
						break;
					}
				}
			}
			
			huevosNecesarios = totalHuevosExistentes;
			
			System.out.println("\nHuevos minimos: "+ huevosNecesarios);
			
			

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

	}
}
