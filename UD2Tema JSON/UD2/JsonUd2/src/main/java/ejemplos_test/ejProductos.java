package ejemplos_test;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ejProductos {
	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		JSONArray prodArray, categoria = null;
		JSONObject prod, ubicacion;
		
		
		String almacen, pasillo;
		double precio;
		long stock;
		
		try {
			
			prodArray = (JSONArray) parser.parse(new FileReader("ficheros/productos.json"));
			
			System.out.println("\n1.\n");
			for (Object prodObj : prodArray) {
				prod = (JSONObject) prodObj;
					System.out.println("Nombre: "+prod.get("nombre_producto"));
			}
			System.out.println("\n2.\n");
			for (Object prodObj : prodArray) {
				prod = (JSONObject) prodObj;
				if(prod.get("tipo_producto").equals("Ropa")) {
					System.out.println("Nombre: "+prod.get("nombre_producto"));
					System.out.println("Precio Producto: "+ prod.get("precio"));
				}
			}
			System.out.println("\n3.\n");
			for (Object prodObj : prodArray) {
				prod = (JSONObject) prodObj;
					if(prod.get("nombre_producto").equals("Pantalones Levi's 501")) {
						System.out.println(prod.toJSONString());
					}
			}
			System.out.println("\n4.\n");
			for (Object prodObj : prodArray) {
				prod = (JSONObject) prodObj;
				ubicacion = (JSONObject) prod.get("ubicacion");
				almacen = (String) ubicacion.get("almacen");
				precio = (double) prod.get("precio");
				
				if(almacen.equals("Almacén 1") && precio<150.0) {
					System.out.println("Nombre Producto: "+prod.get("nombre_producto")+"\nStock: "+ prod.get("stock"));
					
				}
			}
			System.out.println("\n5.\n");
			for (Object prodObj : prodArray) {
				prod = (JSONObject) prodObj;
				ubicacion = (JSONObject) prod.get("ubicacion");
				stock = (Long) prod.get("stock");	
				
				if(stock<50) {
					System.out.println("Nombre Producto: "+ prod.get("nombre_producto") + "\n Ubicacion: "+ ubicacion.toJSONString());
				}
			}
			System.out.println("\n6.\n");
			for (Object prodObj : prodArray) {
				prod = (JSONObject) prodObj;
				ubicacion = (JSONObject) prod.get("ubicacion");
				almacen = (String) ubicacion.get("almacen");
				pasillo = (String) ubicacion.get("pasillo");
				
				
				if(almacen.equals("Almacén 3") && pasillo.equals("Pasillo 3")) {
					System.out.println("Nombre Producto: "+ prod.get("nombre_producto") + "\n Ubicacion: "+ ubicacion.toJSONString());
				}
			}
			System.out.println("\n7.\n");
			for (Object prodObj : prodArray) {
				prod = (JSONObject) prodObj;
				categoria = (JSONArray) prod.get("categorias");
				for (int i = 0; i < categoria.size(); i++) {
					if(categoria.get(i).equals("Deportes")) {
						System.out.println("Nombre Producto: "+prod.get("nombre_producto"));
					}
				}
			}

			

			
			
		} catch (IOException | ParseException e) {
			
			e.printStackTrace();
			
		}
	}
	
}
