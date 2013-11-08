package beta.reporte;

import java.util.HashMap;
import java.util.Map;

import play.data.validation.Constraints.Required;
import models.Contacto;
import models.Departamento;
import models.Provincia;
import ar.com.gemasms.util.Mes;

public class Reporte {

	@Required
	public String anio = null;

	@Required
	public Mes mes;

	public Provincia provincia = null;

	public Departamento departamento = null;

	public Contacto supervisor = null;

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	public Mes getMes() {
		return mes;
	}

	public void setMes(Mes mes) {
		this.mes = mes;
	}

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Contacto getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Contacto supervisor) {
		this.supervisor = supervisor;
	}

	public String getElMes() {
		return this.mes != null ? this.mes.getNumero().toString() : "";
	}

	public void setElMes(String numero) {
		this.mes = Mes.meses().get(Integer.parseInt(numero) - 1);
	}

	public Map<String, Object> obtenerMapaDeFiltros() {
		Map<String, Object> parametros = new HashMap<String, Object>();

		parametros.put("p_anio", anio);
		parametros.put("p_mes", mes);

		if (provincia != null) {
			parametros.put("p_id_provincia", provincia.id.toString());
			parametros.put("p_tabla", "v_informe_campania_por_provincia");
		}

		if (departamento != null) {
			parametros.put("p_id_departamento", departamento.id.toString());
			parametros.put("p_tabla", "v_informe_campania_por_departamento");
		}

		if (supervisor != null) {
			parametros.put("p_id_supervisor", supervisor.getId().toString());
			parametros.put("p_tabla", "v_informe_campania_por_supervisor");
		}

		return parametros;
	}
}
