import java.util.*;

public class SimuladorUrgencia {
    private Hospital hospital;
    private List<Paciente> pacientes;
    private Map<Integer, List<Long>> tiemposEspera;
    private List<Paciente> excedieronTiempo;
    private long tiempoFinalSimulacion;
    private Map<Paciente, Long> tiempoAtencionReal;

    public SimuladorUrgencia(Hospital hospital, List<Paciente> pacientes) {
        this.hospital = hospital;
        this.pacientes = pacientes;
        this.tiemposEspera = new HashMap<>();
        this.excedieronTiempo = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            tiemposEspera.put(i, new ArrayList<>());
        }
        this.tiempoAtencionReal = new HashMap<>();
    }

    public void simular(int pacientesPorDia) {
        long timestampInicio = pacientes.get(0).getTiempoLlegada();
        long tiempoActual;
        int minutosDia = 24 * 60;
        int pacienteIndex = 0;
        int contadorIngresos = 0;

        for (int minuto = 0; minuto < minutosDia; minuto++) {
            tiempoActual = timestampInicio + (minuto * 60 * 1000);

            // Ingreso de pacientes cada 10 minutos
            if (minuto % 10 == 0 && pacienteIndex < pacientesPorDia) {
                hospital.registrarPaciente(pacientes.get(pacienteIndex));
                contadorIngresos++;
                pacienteIndex++;
            }

            // Atención cada 15 minutos
            if (minuto % 15 == 0) {
                Paciente p = hospital.atenderSiguiente();
                if (p != null) registrarTiempoEspera(p, tiempoActual);
            }

            // Atender dos pacientes cuando se acumulan tres ingresos
            if (contadorIngresos >= 3) {
                for (int i = 0; i < 2; i++) {
                    Paciente p = hospital.atenderSiguiente();
                    if (p != null) registrarTiempoEspera(p, tiempoActual);
                }
                contadorIngresos = 0;
            }

            verificarExcedidos(tiempoActual);
        }

        this.tiempoFinalSimulacion = timestampInicio + ((minutosDia - 1) * 60 * 1000);
    }

    private void registrarTiempoEspera(Paciente p, long tiempoActual) {
        long espera = (tiempoActual - p.getTiempoLlegada()) / 1000 / 60;
        tiemposEspera.get(p.getCategoria()).add(espera);
        tiempoAtencionReal.put(p, tiempoActual);
        p.setEstado("atendido");
    }

    private void verificarExcedidos(long tiempoActual) {
        int[] tiemposMaximos = {0, 30, 90, 180}; // Para C1–C4

        for (int categoria = 1; categoria <= 4; categoria++) {
            for (Paciente p : hospital.obtenerPacientesPorCategoria(categoria)) {
                if (p.getEstado().equals("en_espera") &&
                        p.tiempoEsperaActual(tiempoActual) > tiemposMaximos[categoria - 1] &&
                        !excedieronTiempo.contains(p)) {
                    excedieronTiempo.add(p);
                }
            }
        }
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

    public Long getTiempoAtencionDe(Paciente p) {
        return tiempoAtencionReal.get(p);
    }

    public static void main(String[] args) {
        Hospital hospital = new Hospital(10);
        GeneradorPacientes generador = new GeneradorPacientes();
        List<Paciente> pacientes = generador.generarPacientes(100, System.currentTimeMillis());
        SimuladorUrgencia simulador = new SimuladorUrgencia(hospital, pacientes);
        simulador.simular(100);
        System.out.println("Promedios de espera: " + simulador.obtenerPromediosEspera());
        System.out.println("Pacientes que excedieron tiempo: " + simulador.obtenerExcedieronTiempo().size());
        Map<Integer, Integer> categoriasExcedidas = new HashMap<>();
        for (int i = 1; i <= 5; i++) categoriasExcedidas.put(i, 0);

        for (Paciente paciente : simulador.obtenerExcedieronTiempo())
            categoriasExcedidas.put(paciente.getCategoria(), categoriasExcedidas.get(paciente.getCategoria()) + 1);
        System.out.println("Categorías más afectadas por demora (200 pacientes): " + categoriasExcedidas);
    }
}
