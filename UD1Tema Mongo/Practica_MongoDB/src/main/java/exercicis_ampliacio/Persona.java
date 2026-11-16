package exercicis_ampliacio;

import com.google.gson.annotations.SerializedName;

public class Persona {

    // ejercicio ampliacion 1: clase pojo persona con atributos propios sin incluir coches

    @SerializedName("id_persona")
    private int idPersona;

    private String nombre;
    private String apellidos;

    @SerializedName(value = "City", alternate = {"city", "ciudad"})
    private String city;

    private int edad;
    private String dni;
    private String telefono;
    private String email;

    // constructor vacio necesario para la libreria gson
    public Persona() {
    }

    // constructor con parametros para inicializar los datos de la persona
    public Persona(int idPersona, String nombre, String apellidos, String city, int edad, String dni, String telefono, String email) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.city = city;
        this.edad = edad;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
    }

    // metodos getters y setters
    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // metodo tostring para mostrar la informacion de la persona por consola
    @Override
    public String toString() {
        return "persona {" +
                "id=" + idPersona +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", ciudad='" + city + '\'' +
                ", edad=" + edad +
                ", dni='" + dni + '\'' +
                '}';
    }
}
