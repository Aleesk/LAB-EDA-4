
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class AreaAtencion {
    private final String nombre;
    private final PriorityQueue<Paciente> pacientesHeap;
    private final int capacidadMaxima;

    public AreaAtencion(String nombre, int capacidadMaxima) {
        this.nombre = nombre;
        this.pacientesHeap = new PriorityQueue<>(Comparator.comparingInt(Paciente::getCategoria).thenComparingLong(Paciente::getTiempoLlegada));
        this.capacidadMaxima = capacidadMaxima;
    }

    public String getNombre() {
        return nombre;
    }

    public void ingresarPaciente(Paciente p) {
        if (!estaSaturada()) {
            pacientesHeap.add(p);
        }
    }

    public Paciente atenderPaciente() {
        return pacientesHeap.isEmpty() ? null : pacientesHeap.remove();
    }

    public boolean estaSaturada() {
        return capacidadMaxima == pacientesHeap.size();
    }

    public List<Paciente> obtenerPacientesPorHeapSort() {
        return pacientesHeap.stream().sorted(Comparator.comparingInt(Paciente::getCategoria).thenComparingLong(Paciente::getTiempoLlegada)).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        AreaAtencion area = new AreaAtencion("urgencia_adulto", 1);
        Paciente p1 = new Paciente("Juanito", "Perez", "12345678-9", 3, System.currentTimeMillis(), "urgencia_adulto");
        Paciente p2 = new Paciente("Martina", "Rodriguez", "98765432-1", 1, System.currentTimeMillis() - 600000, "urgencia_adulto");
        area.ingresarPaciente(p1);
        area.ingresarPaciente(p2);
        System.out.println("Pacientes en heap: ");
        for (Paciente p : area.obtenerPacientesPorHeapSort()) System.out.println(" - " + p);
        System.out.println("Atender paciente: " + area.atenderPaciente());
        System.out.println("Saturada: " + area.estaSaturada());
    }
}
