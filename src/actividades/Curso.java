package actividades;

import certificacion.Certificable;
import inscripciones.Estudiante;

public class Curso extends Actividad implements Certificable {

    public Curso(int id, String titulo, int cupoMaximo) {
        super(id, titulo, cupoMaximo);
    }

    @Override
    public double calcularCostoMateriales() {
        return 6000.0;
    }

    @Override
    public String getTipo() {
        return "Curso";
    }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "CERTIFICADO DE APROBACIÓN: " + ENTIDAD_EMISORA +
                "\nSe otorga a: " + estudiante.getNombre() +
                " por completar el Curso '" + getTitulo() + "'.";
    }
}