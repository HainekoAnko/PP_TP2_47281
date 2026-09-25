package inscripciones;

import java.io.Serializable;
import java.time.LocalDate;

public class Inscripcion implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate fecha;
    private String estado; // "PENDIENTE", "CONFIRMADA"
    private Estudiante estudiante;
    private TicketDeAcceso ticket;

    public Inscripcion(Estudiante estudiante) {
        this.fecha = LocalDate.now();
        this.estado = "PENDIENTE";
        this.estudiante = estudiante;
    }

    public void confirmarInscripcion() {
        this.estado = "CONFIRMADA";
        this.ticket = new TicketDeAcceso(); // Genera el ticket al confirmar
    }

    public LocalDate getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public Estudiante getEstudiante() { return estudiante; }
    public TicketDeAcceso getTicket() { return ticket; }

    // Clase anidada miembro (Ejercicio 4)
    public class TicketDeAcceso implements Serializable {
        private String codigoTicket;

        public TicketDeAcceso() {
            this.codigoTicket = "TICKET-" + estudiante.getLegajo() + "-" + (int)(Math.random() * 9000 + 1000);
        }

        public void enviarTicket() {
            System.out.println("[HILO ENVÍO] Enviando " + codigoTicket + " al estudiante: " + estudiante.getNombre());
            try {
                Thread.sleep(800); // Simula el tiempo de envío por red
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("[HILO ENVÍO] ✓ " + codigoTicket + " enviado con éxito.");
        }

        public String getCodigoTicket() { return codigoTicket; }
    }
}