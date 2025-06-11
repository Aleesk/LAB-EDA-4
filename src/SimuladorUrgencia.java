import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimuladorUrgencia {
    private Hospital hospital;
    private List<Paciente> pacientes;
    private Map<Integer, List<Long>> tiemposEspera;
    private List<Paciente> excedieronTiempo;
    private long tiempoFinalSimulacion;

    public SimuladorUrgencia(Hospital hospital, List<Paciente> pacientes) {
        this.hospital = hospital;
        this.pacientes = pacientes;
        this.tiemposEspera = new HashMap<>();
        this.excedieronTiempo = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            tiemposEspera.put(i, new ArrayList<>());
        }
    }

    public void simular(int pacientesPorDia) {
        long timestampInicio = pacientes.get(0).getTiempoLlegada();
        long tiempoActual;
        int minutosDia = 24 * 60;
        int pacienteIndex = 0;
        int contadorIngresos = 0;

        for (int minuto = 0; minuto < minutosDia; minuto++) {
            tiempoActual = timestampInicio + (minuto * 60 * 1000);

            if (minuto >= 10 && minuto % 10 == 0 && pacienteIndex < pacientesPorDia) {
                hospital.registrarPaciente(pacientes.get(pacienteIndex));
                contadorIngresos++;
                pacienteIndex++;
            }
            if (minuto > 0 && minuto % 15 == 0) {
                Paciente p = hospital.atenderSiguiente();
                if (p != null) {
                    registrarTiempoEspera(p, tiempoActual);
                }
            }

            if (contadorIngresos >= 3) {
                for (int i = 0; i < 1; i++) {
                    Paciente p = hospital.atenderSiguiente();
                    if (p != null) {
                        registrarTiempoEspera(p, tiempoActual);
                    }
                }
                contadorIngresos = 0;
            }
            verificarExcedidos(tiempoActual);
        }
        this.tiempoFinalSimulacion = timestampInicio + ((minutosDia - 1) * 60 * 1000);
    }

    private void verificarExcedidos(long tiempoActual) {
        verificarCategoriaExcedida(1, 0, tiempoActual);
        verificarCategoriaExcedida(2, 30, tiempoActual);
        verificarCategoriaExcedida(3, 90, tiempoActual);
        verificarCategoriaExcedida(4, 120, tiempoActual);
    }

    private void verificarCategoriaExcedida(int categoria, int tiempoMaximo, long tiempoActual) {
        for (Paciente p : hospital.obtenerPacientesPorCategoria(categoria))
            if (p.tiempoEsperaActual(tiempoActual) > tiempoMaximo && !excedieronTiempo.contains(p))
                excedieronTiempo.add(p);
    }

    private void registrarTiempoEspera(Paciente p, long tiempoActual) {
        long espera = (tiempoActual - p.getTiempoLlegada()) / 1000 / 60;
        tiemposEspera.get(p.getCategoria()).add(espera);
    }

    public Map<Integer, Double> obtenerPromediosEspera() {
        Map<Integer, Double> promedios = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            List<Long> tiempos = tiemposEspera.get(i);
            double avg = tiempos.isEmpty() ? 0 : tiempos.stream().mapToLong(Long::longValue).average().orElse(0);
            promedios.put(i, avg);
        }
        return promedios;
    }

    public List<Paciente> obtenerExcedieronTiempo() {
        return excedieronTiempo;
    }

    public long getTiempoFinalSimulacion() {
        return tiempoFinalSimulacion;
    }

    public static void main(String[] args) {
        Hospital hospital = new Hospital();
        GeneradorPacientes generador = new GeneradorPacientes();
        List<Paciente> pacientes = generador.generarPacientes(100, System.currentTimeMillis());
        SimuladorUrgencia simulador = new SimuladorUrgencia(hospital, pacientes);
        simulador.simular(20);
        System.out.println("Promedios de espera: " + simulador.obtenerPromediosEspera());
        System.out.println("Pacientes que excedieron tiempo: " + simulador.obtenerExcedieronTiempo().size());
    }
}
