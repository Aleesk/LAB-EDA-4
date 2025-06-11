import java.util.*;

public class Hospital {
    private final Map<String, Paciente> pacientesTotales;
    private final PriorityQueue<Paciente> colaAtencion;
    private final Map<String, AreaAtencion> areasAtencion;
    private final List<Paciente> pacientesAtendidos;

    public Hospital() {
        pacientesTotales = new HashMap<>();
        colaAtencion = new PriorityQueue<>(Comparator.comparingInt(Paciente::getCategoria).thenComparingLong(Paciente::getTiempoLlegada));
        areasAtencion = new HashMap<>();
        areasAtencion.put("urgencia_adulto", new AreaAtencion("urgencia_adulto", 1));
        areasAtencion.put("infantil", new AreaAtencion("infantil", 1));
        areasAtencion.put("SAPU", new AreaAtencion("SAPU", 1));
        pacientesAtendidos = new ArrayList<>();
    }

    public void registrarPaciente(Paciente paciente) {
        pacientesTotales.put(paciente.getId(), paciente);
        colaAtencion.offer(paciente);
        AreaAtencion areaAtencion = areasAtencion.get(paciente.getArea());
        areaAtencion.ingresarPaciente(paciente);
    }

    public void reasignarCategoria(String id, int nuevaCategoria) {
        Paciente paciente = pacientesTotales.get(id);
        if (paciente == null) return;

        colaAtencion.remove(paciente);
        paciente.setCategoria(nuevaCategoria);
        paciente.registrarCambio("Cambio a categoria C" + nuevaCategoria);
        colaAtencion.offer(paciente);
    }

    public Paciente atenderSiguiente() {
        if (colaAtencion.isEmpty()) return null;
        Paciente paciente = colaAtencion.peek();
        if (paciente == null) return null;

        AreaAtencion areaAtencion = obtenerArea(paciente.getArea());
        if (areaAtencion == null || areaAtencion.estaSaturada()) {
            return null;
        }

        paciente = colaAtencion.poll();
        paciente.setEstado("atendido");
        pacientesAtendidos.add(paciente);
        areaAtencion.atenderPaciente();
        return paciente;
    }

    public List<Paciente> obtenerPacientesPorCategoria(int categoria) {
        List<Paciente> pacientes = new ArrayList<>();
        for (Paciente paciente : colaAtencion) if (paciente.getCategoria() == categoria) pacientes.add(paciente);
        return pacientes;
    }

    public AreaAtencion obtenerArea(String area) {
        return areasAtencion.get(area);
    }

    public int getTotalPacientesAtendidos() {
        return pacientesAtendidos.size();
    }

    public static void main(String[] args) {
        Hospital hospital = new Hospital();
        Paciente p1 = new Paciente("Juanito", "Perez", "12345678-9", 3, System.currentTimeMillis() - 50000, "urgencia_adulto");
        Paciente p2 = new Paciente("Martina", "Rodriguez", "98765432-1", 1, System.currentTimeMillis() - 600000, "urgencia_adulto");
        hospital.registrarPaciente(p1);
        hospital.registrarPaciente(p2);
        System.out.println("Paciente registrado: " + p1);
        System.out.println("Paciente registrado: " + p2);
        hospital.reasignarCategoria("12345678-9", 1);
        System.out.println("Paciente tras reasignación de Categoria: " + hospital.pacientesTotales.get("12345678-9"));
        Paciente atendido = hospital.atenderSiguiente();
        System.out.println("Paciente atendido: " + atendido);
    }
}
