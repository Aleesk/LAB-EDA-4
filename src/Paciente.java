import java.util.Stack;

public class Paciente {
    private String nombre;
    private String apellido;
    private String id;
    private int categoria;
    private long tiempoLlegada;
    private String estado;
    private String area;
    private final Stack<String> historialCambios;

    public Paciente(String nombre, String apellido, String id, int categoria, long tiempoLlegada, String area) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = id;
        this.categoria = categoria;
        this.tiempoLlegada = tiempoLlegada;
        this.estado = "en_espera";
        this.area = area;
        this.historialCambios = new Stack<>();
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getId() {
        return id;
    }

    public int getCategoria() {
        return categoria;
    }

    public long getTiempoLlegada() {
        return tiempoLlegada;
    }

    public String getEstado() {
        return estado;
    }

    public String getArea() {
        return area;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public void setTiempoLlegada(long tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setArea(String area) {
        this.area = area;
    }

    // Métodos
    public long tiempoEsperaActual(long tiempoActual) {
        return (tiempoActual - tiempoLlegada) / 1000 / 60;
    }

    public void registrarCambio(String descripcion) {
        historialCambios.push(descripcion);
    }

    public String obtenerUltimoCambio() {
        return historialCambios.isEmpty() ? null : historialCambios.pop();
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " -> [id:" + id + ", categoria: " + categoria + ", estado: " + estado + ", area: " + area + "]";
    }

    // Test main
    public static void main(String[] args) {
        Paciente paciente = new Paciente(
                "Juanito",
                "Perez",
                "12345678-9",
                3,
                System.currentTimeMillis() - 600000,
                "urgencia_adulto"); // Hace 10 minutos
        System.out.println("Paciente: " + paciente);
        System.out.println("Tiempo de espera: " + paciente.tiempoEsperaActual(System.currentTimeMillis()) + " minutos");
        paciente.registrarCambio("Cambio de categoria a C2");
        System.out.println("Último cambio: " + paciente.obtenerUltimoCambio());
    }
}
