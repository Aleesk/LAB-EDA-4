import java.util.Stack;

public class Paciente {
    private String nombre;
    private String apellido;
    private int id;
    private int categoria;
    private long tiempoLlegada;
    private String estado;
    private String area;
    private final Stack<String> historialCambios;

    public Paciente(String nombre, String apellido, int id, long tiempoLlegada) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = id;
        this.tiempoLlegada = tiempoLlegada;
        this.historialCambios = new Stack<>();
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getId() {
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
    public long tiempoEsperaActual() {
        return (System.currentTimeMillis() / 1000) - tiempoLlegada;
    }

    public void registrarCambio(String descripcion) {
        historialCambios.push(descripcion);
    }

    public String obtenerUltimoCambio() {
        return historialCambios.pop();
    }

    public static void main(String[] args) {
        Paciente p = new Paciente("Eduardo", "Vergara", 100, 3600000);
        System.out.println(p.getNombre() + ":" + p.getTiempoLlegada());
        Paciente p1 = new Paciente("Alexander", "Bravo", 100, 4000000);
        System.out.println(p1.getNombre() + ":" + p1.getTiempoLlegada());
    }
}
