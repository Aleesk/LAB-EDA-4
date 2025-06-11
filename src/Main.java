import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Prueba 1: Seguimiento individual (C4 paciente)
        Hospital hospital = new Hospital();
        GeneradorPacientes generador = new GeneradorPacientes();
        long timestampInicio = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        List<Paciente> pacientes = generador.generarPacientes(100, timestampInicio);
        SimuladorUrgencia simulador = new SimuladorUrgencia(hospital, pacientes);
        simulador.simular(100);
        Paciente c4Paciente = pacientes.stream().filter(p -> p.getCategoria() == 4).findFirst().orElse(null);
        if (c4Paciente != null) {
            long tiempoFinal = simulador.getTiempoFinalSimulacion();
            System.out.println("Tiempo de espera C4: " + c4Paciente.tiempoEsperaActual(tiempoFinal) + " minutos");
        }

        // Prueba 2: Promedio por categoría (Realizar 15 simulaciones)
        Map<Integer, Double> promedios = simulador.obtenerPromediosEspera();
        System.out.println("Promedios de espera por categoría (100 pacientes): " + promedios);
        System.out.println("Total de pacientes atendidos: " + hospital.getTotalPacientesAtendidos());

        // Prueba 3: Saturación del sistema (200 pacientes)
        hospital = new Hospital();
        pacientes = generador.generarPacientes(200, timestampInicio);
        simulador = new SimuladorUrgencia(hospital, pacientes);
        simulador.simular(200);

        Map<Integer, Integer> categoriasExcedidas = new HashMap<>();
        for (int i = 1; i <= 5; i++) categoriasExcedidas.put(i, 0);

        for (Paciente paciente : simulador.obtenerExcedieronTiempo())
            categoriasExcedidas.put(paciente.getCategoria(), categoriasExcedidas.get(paciente.getCategoria()) + 1);

        System.out.println("Categorías más afectadas por demora (200 pacientes): " + categoriasExcedidas);
        System.out.println("Se atendieron " + hospital.getTotalPacientesAtendidos() + " pacientes de 200 posibles.");

        // Prueba 4: Cambio de categoría (C3 a C1)
        hospital = new Hospital();
        Paciente p = new Paciente("Test", "Paciente", "TEST123", 3, System.currentTimeMillis(), "urgencia_adulto");
        hospital.registrarPaciente(p);
        hospital.reasignarCategoria("TEST123", 1);
        System.out.println("Último cambio de TEST123: " + p.obtenerUltimoCambio());
    }
}
