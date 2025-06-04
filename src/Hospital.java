import java.util.*;

public class Hospital {
    private final Map<String, Paciente> pacientesTotales;
    private final PriorityQueue<Paciente> colaAtencion;
    private final Map<String, AreaAtencion> areaAtencion;
    private final List<Paciente> pacientesAtendidos;

    public Hospital() {
        pacientesTotales = new HashMap<>();
        colaAtencion = new PriorityQueue<>(Comparator.comparingInt(Paciente::getCategoria).thenComparingLong(Paciente::getTiempoLlegada));
        areaAtencion = new HashMap<>();
        pacientesAtendidos = new ArrayList<>();
    }

    public void registrarPaciente(Paciente paciente, int categoria, String area) {
        pacientesTotales.put(paciente.getNombre(), paciente);
        colaAtencion.add(paciente);
        pacientesAtendidos.add(paciente);
        paciente.setCategoria(categoria);
        paciente.setArea(area);
        areaAtencion.put(area, obtenerArea(area));
    }

    public void reasignarCategoria(String id, int nuevaCategoria) {
        Paciente paciente = pacientesTotales.get(id);
        paciente.registrarCambio(paciente.getCategoria() + "->" + nuevaCategoria);
        paciente.setCategoria(nuevaCategoria);
    }

    public Paciente atenderSiguiente() {
        // Extraer al paciente de mayor prioridad de la cola general
        Paciente paciente = colaAtencion.poll();
        if (paciente == null) return null; // No hay pacientes en la cola

        // Obtener su área asignada (ej: "urgencia_adulto")
        String nombreArea = paciente.getArea();
        AreaAtencion area = areaAtencion.get(nombreArea);

        // Validar si el área existe
        if (area == null) {
            System.out.println("Area no registrada: " + nombreArea);
            return null;
        }

        // Verificar si el área está saturada
        if (area.estaSaturada()) {
            System.out.println("Area " + nombreArea + " saturada. Paciente en espera.");
            return null; // Opcional: reintentar luego o lanzar excepción
        }

        // Ingresar al paciente en su área (ahora estará en su PriorityQueue interna)
        area.ingresarPaciente(paciente);

        // Agregar paciente atendido a la lista
        pacientesAtendidos.add(paciente);
        return paciente;
    }

    public List<Paciente> obtenerPacientesPorCategoria(int categoria) {
        List<Paciente> pacientes = new ArrayList<>();
        for (Paciente paciente : pacientesTotales.values()) {
            if (paciente.getCategoria() == categoria) pacientes.add(paciente);
        }
        return pacientes;
    }

    public AreaAtencion obtenerArea(String area) {
        return areaAtencion.get(area);
    }
}
