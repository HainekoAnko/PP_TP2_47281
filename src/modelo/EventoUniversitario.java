package modelo;

import actividades.Actividad;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EventoUniversitario implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;
    private static int cantidadEventos = 0;

    private Sala sala;
    private List<Actividad> actividades;

    public EventoUniversitario(String id, String titulo, double costoBase, boolean gratuito) {
        this.id = id;
        this.titulo = titulo;
        this.costoBase = costoBase;
        this.gratuito = gratuito;
        this.actividades = new ArrayList<>();
        cantidadEventos++;
    }

    // Constructor de copia
    public EventoUniversitario(EventoUniversitario otro) {
        this(otro.id, otro.titulo, otro.costoBase, otro.gratuito);
        this.sala = otro.sala;
        this.actividades = new ArrayList<>(otro.actividades);
    }

    public void asignarSala(Sala sala) {
        this.sala = sala;
    }

    public double calcularCostoEstimado() {
        if (gratuito) return 0;
        double totalMateriales = 0;
        for (Actividad a : actividades) {
            totalMateriales += a.calcularCostoMateriales();
        }
        return costoBase + totalMateriales;
    }

    public void agregarActividad(Actividad actividad) {
        this.actividades.add(actividad);
    }

    // Método con genéricos acotados (Ejercicio 3)
    public  List filtrarActividadesPorTipo(Class tipo) {
        List filtradas = new ArrayList<>();
        for (Actividad act : actividades) {
            if (tipo.isInstance(act)) {
                filtradas.add(tipo.cast(act));
            }
        }
        return filtradas;
    }

    // Método con comodines/wildcards (Ejercicio 3)
    public double calcularCostoMateriales(List <Actividad> listaActividades) {
        double total = 0;
        for (Actividad act : listaActividades) {
            total += act.calcularCostoMateriales();
        }
        return total;
    }

    public void mostrarDatos() {
        System.out.println("==================================================");
        System.out.println("EVENTO: " + titulo + " (ID: " + id + ")");
        System.out.println("Gratuito: " + (gratuito ? "Sí" : "No") + " | Costo Base: $" + costoBase);
        System.out.println("Sala: " + (sala != null ? sala.getNombre() : "Sin asignar"));
        System.out.println("Actividades vinculadas: " + actividades.size());
        for (Actividad a : actividades) {
            a.mostrarIdentificacion();
            a.mostrarInscripciones();
        }
        System.out.println("==================================================");
    }

    // Persistencia mediante Serialización
    public boolean persistirEvento() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("evento_" + id + ".dat"))) {
            oos.writeObject(this);
            return true;
        }
    }

    public static EventoUniversitario recuperarEvento(String id) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("evento_" + id + ".dat"))) {
            return (EventoUniversitario) ois.readObject();
        }
    }

    // Getters y Setters
    public static int getCantidadEventos() { return cantidadEventos; }
    public String getId() { return id; }
    public String getTitulo() { return titulo; }

    // Importante: Retorna List con genéricos
    public List<Actividad> getActividades() {
        return actividades;
    }
}