
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
        pacientesHeap.add(p);
    }

    public Paciente atenderPaciente() {
        return pacientesHeap.isEmpty() ? null : pacientesHeap.poll();
    }

    public boolean estaSaturada() {
        return capacidadMaxima == pacientesHeap.size();
    }

    public List<Paciente> obtenerPacientesPorHeapSort() {
        return pacientesHeap.stream().sorted().collect(Collectors.toList());
    }
}
