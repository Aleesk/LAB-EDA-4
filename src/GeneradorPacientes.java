import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradorPacientes {
    public List<Paciente> generarPacientes(int n, long timestampInicio) {
        List<Paciente> pacientes = new ArrayList<>();
        Random random = new Random();
        String[] nombres = {"Juan", "Ana", "Pedro", "Maria", "Jose", "Martina"};
        String[] apellidos = {"Perez", "Gomez", "Lopez", "Rodriguez", "Fernandez", "Ortiz"};
        String[] areas = {"urgencia_adulto", "infantil", "SAPU"};

        for (int i = 0; i < n; i++) {
            String nombre = nombres[random.nextInt(nombres.length)];
            String apellido = apellidos[random.nextInt(apellidos.length)];
            String id = "ID-"+ i;
            int categoria = generarCategoria(random);
            long tiempoLlegada = timestampInicio + (i * 600 * 1000);
            String area = areas[random.nextInt(areas.length)];
            pacientes.add(new Paciente(nombre, apellido, id, categoria, tiempoLlegada, area));
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Pacientes_24h.txt"))) {
            for (Paciente p : pacientes) {
                writer.write(p.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pacientes;
    }

    private int generarCategoria(Random rand) {
        double random = rand.nextDouble();
        double acum = 0;
        double[] probabilidades = {0.10, 0.15, 0.18, 0.27, 0.30};
        for (int i = 0; i < probabilidades.length; i++) {
            acum += probabilidades[i];
            if (random <= acum) return i + 1;
        }
        return 5;
    }

    public static void main(String[] args) {
        GeneradorPacientes generador = new GeneradorPacientes();
        long timestampInicio = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        List<Paciente> pacientes = generador.generarPacientes(100, timestampInicio);
        for (Paciente p : pacientes) {
            System.out.println(p);
        }
    }
}
