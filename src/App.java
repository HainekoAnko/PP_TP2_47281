import actividades.*;
import certificacion.Certificable;
import excepciones.CupoExcedidoException;
import hilos.EnvioTicketsThread;
import inscripciones.*;
import modelo.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class App {
    public static void main(String[] args) {
        System.out.println("=== TRABAJO PRÁCTICO 2: PROGRAMACIÓN ORIENTADA A OBJETOS ===");

        // --- DATOS DE PRUEBA ---
        Estudiante e1 = new Estudiante("E101", "Juan Pérez");
        Estudiante e2 = new Estudiante("E102", "María Gómez");
        Estudiante e3 = new Estudiante("E103", "Carlos López");

        EventoUniversitario evento = new EventoUniversitario("EV-2026", "Jornadas de Tecnología UTN", 10000.0, false);
        Sala salaA = new Sala(1, "Auditorio Central");
        evento.asignarSala(salaA);

        Charla charla = new Charla(1, "IA en la Educación", 2, "Dr. Roberto Rossi");
        Taller taller = new Taller(2, "Desarrollo con Spring Boot", 2, true);
        Curso curso = new Curso(3, "Arquitectura de Software", 30);

        evento.agregarActividad(charla);
        evento.agregarActividad(taller);
        evento.agregarActividad(curso);

        // --- EJERCICIO 1: Manejo de Excepciones y Persistencia ---
        System.out.println("\n--- EJERCICIO 1: Excepciones de Cupo y Persistencia ---");
        try {
            System.out.println("Inscribiendo alumnos en 'IA en la Educación' (Cupo max: 2)...");
            charla.inscribir(e1);
            charla.inscribir(e2);

            // Intento con cupo superado (Provoca CupoExcedidoException)
            System.out.println("Intentando inscribir a un 3er alumno...");
            charla.inscribir(e3);
        } catch (CupoExcedidoException e) {
            System.out.println(" [CATCH CONTROLADO]: " + e.getMessage());
        }

        // Flujo try-catch-finally completo de Persistencia
        try {
            System.out.println("\nPersistiendo el evento...");
            evento.persistirEvento();
            System.out.println("Evento guardado exitosamente.");

            System.out.println("Recuperando evento persistido...");
            EventoUniversitario evRecuperado = EventoUniversitario.recuperarEvento("EV-2026");
            System.out.println("Evento recuperado con título: " + evRecuperado.getTitulo());

        } catch (FileNotFoundException e) {
            System.err.println(" [CATCH PERSISTENCIA]: Archivo no encontrado.");
        } catch (ClassNotFoundException e) {
            System.err.println(" [CATCH PERSISTENCIA]: Clase del objeto no encontrada.");
        } catch (IOException e) {
            System.err.println(" [CATCH PERSISTENCIA]: Error de E/S al procesar archivo: " + e.getMessage());
        } finally {
            System.out.println(" [FINALLY]: Proceso de verificación de persistencia finalizado.");
        }

        // --- EJERCICIO 2: Interfaces y Certificados ---
        System.out.println("\n--- EJERCICIO 2: Interfaces y Certificados ---");
        try {
            taller.inscribir(e1);
            curso.inscribir(e2);
        } catch (CupoExcedidoException ignored) {}

        System.out.println("Emitiendo certificados para actividades certificables:");
        for (Actividad act : evento.getActividades()) {
            if (act instanceof Certificable) {
                Certificable certificable = (Certificable) act;
                for (Inscripcion ins : act.getInscripciones()) {
                    System.out.println("--------------------------------------------------");
                    System.out.println(certificable.generarCertificado(ins.getEstudiante()));
                }
            } else {
                System.out.println("La actividad '" + act.getTitulo() + "' (" + act.getTipo() + ") NO emite certificados.");
            }
        }

        // --- EJERCICIO 3: Métodos Genéricos Acotados y Wildcards ---
        System.out.println("\n--- EJERCICIO 3: Generics & Wildcards ---");
        List charlas = evento.filtrarActividadesPorTipo(Charla.class);
        List talleres = evento.filtrarActividadesPorTipo(Taller.class);
        List cursos = evento.filtrarActividadesPorTipo(Curso.class);

        System.out.println("Cantidad de Charlas: " + charlas.size());
        System.out.println("Cantidad de Talleres: " + talleres.size());
        System.out.println("Cantidad de Cursos: " + cursos.size());

        System.out.println("Costo total de materiales en Cursos: $" + evento.calcularCostoMateriales(cursos));
        System.out.println("Costo total de materiales en Talleres: $" + evento.calcularCostoMateriales(talleres));

        // --- EJERCICIO 4: Concurrencia (Hilos) y Clases Anidadas ---
        System.out.println("\n--- EJERCICIO 4: Programación Concurrente e Inscripciones ---");
        // Confirmar inscripciones para generar tickets
        for (Actividad act : evento.getActividades()) {
            for (Inscripcion ins : act.getInscripciones()) {
                ins.confirmarInscripcion(); // Genera el TicketDeAcceso (Clase Anidada)
            }
        }

        // Iniciar el hilo de envío de tickets
        EnvioTicketsThread hiloEnvio = new EnvioTicketsThread(evento);
        hiloEnvio.start();

        // El hilo principal continúa de manera concurrente
        for (int i = 1; i <= 3; i++) {
            System.out.println("[HILO PRINCIPAL] Renderizando interfaz del evento... Paso " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            hiloEnvio.join(); // Esperar fin del hilo para cerrar ordenadamente
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== FIN DE LA EJECUCIÓN DEL TP2 ===");
    }
}