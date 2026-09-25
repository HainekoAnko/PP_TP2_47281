package hilos;

import actividades.Actividad;
import inscripciones.Inscripcion;
import modelo.EventoUniversitario;

public class EnvioTicketsThread extends Thread {
    private EventoUniversitario evento;

    public EnvioTicketsThread(EventoUniversitario evento) {
        this.evento = evento;
    }

    @Override
    public void run() {
        System.out.println("\n>>> [HILO SECUNDARIO] Iniciando procesamiento de tickets para: " + evento.getTitulo());

        // Recorre la lista de actividades
        for (Actividad act : evento.getActividades()) {
            // Recorre la lista de inscripciones
            for (Inscripcion ins : act.getInscripciones()) {
                if ("CONFIRMADA".equals(ins.getEstado()) && ins.getTicket() != null) {
                    ins.getTicket().enviarTicket();
                }
            }
        }

        System.out.println(">>> [HILO SECUNDARIO] Finalizó el envío masivo de tickets.\n");
    }
}