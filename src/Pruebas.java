import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pruebas {
    public static void main(String[] args) {
        // Prueba 1: Seguimiento individual (C4 paciente)
        System.out.println("------ Prueba 1 -------");
        Hospital hospital = new Hospital(50);
        GeneradorPacientes generador = new GeneradorPacientes();
        long timestampInicio = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        List<Paciente> pacientes = generador.generarPacientes(100, timestampInicio);
        SimuladorUrgencia simulador = new SimuladorUrgencia(hospital, pacientes);
        simulador.simular(100);
        Paciente c4Paciente = pacientes.stream().filter(p -> p.getCategoria() == 4).findFirst().orElse(null);
        if (c4Paciente != null) {
            Long tiempoAtencion = simulador.getTiempoAtencionDe(c4Paciente);
            if (tiempoAtencion != null) {
                long espera = (tiempoAtencion - c4Paciente.getTiempoLlegada()) / 1000 / 60;
                System.out.println("Tiempo de espera real de paciente C4: " + espera + " minutos");
            } else {
                System.out.println("El paciente C4 no fue atendido.");
            }
        }

        // Prueba 2: Promedio por categoría (Realizar 15 simulaciones)
        System.out.println("------ Prueba 2 -------");
        Map<Integer, List<Long>> tiemposTotales = new HashMap<>();
        for (int i = 0; i < 15; i++) {
            Hospital h = new Hospital(50);
            List<Paciente> ps = generador.generarPacientes(100, timestampInicio + (i * 10000));
            SimuladorUrgencia sim = new SimuladorUrgencia(h, ps);
            sim.simular(100);
            Map<Integer, Double> promedioSim = sim.obtenerPromediosEspera();

            for (Map.Entry<Integer, Double> e : promedioSim.entrySet()) {
                tiemposTotales.putIfAbsent(e.getKey(), new ArrayList<>());
                tiemposTotales.get(e.getKey()).add(e.getValue().longValue());
            }
        }
        Map<Integer, Double> promedioFinal = new HashMap<>();
        for (int cat = 1; cat <= 5; cat++) {
            List<Long> tiempos = tiemposTotales.getOrDefault(cat, new ArrayList<>());
            double prom = tiempos.stream().mapToLong(Long::longValue).average().orElse(0.0);
            promedioFinal.put(cat, prom);
        }
        System.out.println("Promedios de espera por categoría (15 simulaciones): " + promedioFinal);

        // Prueba 3: Saturación del sistema (200 pacientes)
        System.out.println("------ Prueba 3 -------");
        hospital = new Hospital(100);
        pacientes = generador.generarPacientes(200, timestampInicio);
        simulador = new SimuladorUrgencia(hospital, pacientes);
        simulador.simular(200);

        Map<Integer, Integer> pacientesDemorados = new HashMap<>();

        for (int i = 1; i <= 5; i++) {
            pacientesDemorados.put(i, 0);
        }

        for (Paciente paciente : simulador.obtenerExcedieronTiempo()) {
            int categoria = paciente.getCategoria();
            pacientesDemorados.put(categoria, pacientesDemorados.get(categoria) + 1);
        }

        System.out.println("Pacientes que excedieron el tiempo de atención (200 pacientes):");
        pacientesDemorados.forEach((cat, cant) -> System.out.println("C" + cat + ": " + cant));

        long noAtendidos = pacientes.stream().filter(p -> !p.getEstado().equals("atendido")).count();
        System.out.println("No atendidos: " + noAtendidos);

        long atendidos = pacientes.stream().filter(p -> p.getEstado().equals("atendido")).count();
        System.out.println("Atendidos: " + atendidos);

        // Prueba 4: Cambio de categoría (C3 a C1)
        System.out.println("------ Prueba 4 -------");
        hospital = new Hospital(2);
        Paciente p = new Paciente("Test", "Paciente", "TEST123", 3, System.currentTimeMillis(), "urgencia_adulto");
        hospital.registrarPaciente(p);
        hospital.reasignarCategoria("TEST123", 1);
        System.out.println("Último cambio de TEST123: " + p.obtenerUltimoCambio());
    }
}
