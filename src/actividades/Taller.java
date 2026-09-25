package actividades;

import certificacion.Certificable;
import inscripciones.Estudiante;

public class Taller extends Actividad implements Certificable {
    private boolean requiereNotebook;

    public Taller(int id, String titulo, int cupoMaximo, boolean requiereNotebook) {
        super(id, titulo, cupoMaximo);
        this.requiereNotebook = requiereNotebook;
    }

    @Override
    public double calcularCostoMateriales() {
        return requiereNotebook ? 3000.0 : 4500.0;
    }

    @Override
    public String getTipo() {
        return "Taller";
    }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "CERTIFICADO DE ASISTENCIA: " + ENTIDAD_EMISORA +
                "\nSe otorga a: " + estudiante.getNombre() +
                " por participar del Taller '" + getTitulo() + "'.";
    }

    public boolean isRequiereNotebook() { return requiereNotebook; }
}