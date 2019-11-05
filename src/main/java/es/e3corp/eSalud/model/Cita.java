package es.e3corp.eSalud.model;

import java.util.UUID;

import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "citas")

public class Cita {

	@Id
    private String id;
    @NotNull
    private String paciente;
    private String tipo;
    private String fecha;
    private String centro;
    private String médico;
    private String hora;
    
	public Cita(@NotNull String paciente, String tipo, String fecha, String centro, String médico,
			String hora) {
		super();
		this.id = UUID.randomUUID().toString();
		this.paciente = paciente;
		this.tipo = tipo;
		this.fecha = fecha;
		this.centro = centro;
		this.médico = médico;
		this.hora = hora;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPaciente() {
		return paciente;
	}

	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getCentro() {
		return centro;
	}

	public void setCentro(String centro) {
		this.centro = centro;
	}

	public String getMédico() {
		return médico;
	}

	public void setMédico(String médico) {
		this.médico = médico;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((centro == null) ? 0 : centro.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((hora == null) ? 0 : hora.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((médico == null) ? 0 : médico.hashCode());
		result = prime * result + ((paciente == null) ? 0 : paciente.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cita other = (Cita) obj;
		if (centro == null) {
			if (other.centro != null)
				return false;
		} else if (!centro.equals(other.centro))
			return false;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (hora == null) {
			if (other.hora != null)
				return false;
		} else if (!hora.equals(other.hora))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (médico == null) {
			if (other.médico != null)
				return false;
		} else if (!médico.equals(other.médico))
			return false;
		if (paciente == null) {
			if (other.paciente != null)
				return false;
		} else if (!paciente.equals(other.paciente))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cita [id=" + id + ", paciente=" + paciente + ", tipo=" + tipo + ", fecha=" + fecha + ", centro="
				+ centro + ", médico=" + médico + ", hora=" + hora + "]";
	}
	
	
   
	
    
    
}