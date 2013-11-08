package controllers;

import static play.data.Form.form;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import play.Logger;
import play.Play;
import play.data.Form;
import play.db.DB;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import seguridad.models.Usuario;
import ar.com.gemasms.configuracion.GemaProperties;
import beta.reporte.Reporte;

@Security.Authenticated(Secured.class)
public class ReporteController extends Controller {

	private static String INFORME_UNO_JRXML = "info1.jrxml";

	private static String INFORME_UNO_JASPER = "info1.jasper";

	private static String INFORME_DOS_JRXML = "info2.jrxml";

	private static String INFORME_DOS_JASPER = "info2.jasper";

	private static String INFORME_TRES_JRXML = "info3.jrxml";

	private static String INFORME_TRES_JASPER = "info3.jasper";

	private static String INFORME_CUATRO_JRXML = "info4.jrxml";

	private static String INFORME_CUATRO_JASPER = "info4.jasper";

	private static String INFORME_CINCO_JRXML = "info5.jrxml";

	private static String INFORME_CINCO_JASPER = "info5.jasper";

	private static String INFORME_SEIS_JRXML = "info6.jrxml";

	private static String INFORME_SEIS_JASPER = "info6.jasper";

	@Security.Authenticated(Secured.class)
	public static Result infografiaUno(Long mes, String anio,
			String idProvincia, String idDepartamento, String idSupervisor) {
		return obtenerResultadoDelInforme(mes, anio, idProvincia,
				idDepartamento, idSupervisor, INFORME_UNO_JRXML,
				INFORME_UNO_JASPER);
	}

	@Security.Authenticated(Secured.class)
	public static Result infografiaDos(Long mes, String anio,
			String idProvincia, String idDepartamento, String idSupervisor) {
		return obtenerResultadoDelInforme(mes, anio, idProvincia,
				idDepartamento, idSupervisor, INFORME_DOS_JRXML,
				INFORME_DOS_JASPER);
	}

	@Security.Authenticated(Secured.class)
	public static Result infografiaTres(Long mes, String anio,
			String idProvincia, String idDepartamento, String idSupervisor) {
		return obtenerResultadoDelInforme(mes, anio, idProvincia,
				idDepartamento, idSupervisor, INFORME_TRES_JRXML,
				INFORME_TRES_JASPER);
	}

	@Security.Authenticated(Secured.class)
	public static Result infografiaCuatro(Long mes, String anio,
			String idProvincia, String idDepartamento, String idSupervisor) {
		return obtenerResultadoDelInforme(mes, anio, idProvincia,
				idDepartamento, idSupervisor, INFORME_CUATRO_JRXML,
				INFORME_CUATRO_JASPER);
	}

	@Security.Authenticated(Secured.class)
	public static Result infografiaCinco(Long mes, String anio,
			String idProvincia, String idDepartamento, String idSupervisor) {
		return obtenerResultadoDelInforme(mes, anio, idProvincia,
				idDepartamento, idSupervisor, INFORME_CINCO_JRXML,
				INFORME_CINCO_JASPER);
	}

	@Security.Authenticated(Secured.class)
	public static Result infografiaSeis(Long mes, String anio,
			String idProvincia, String idDepartamento, String idSupervisor) {
		return obtenerResultadoDelInforme(mes, anio, idProvincia,
				idDepartamento, idSupervisor, INFORME_SEIS_JRXML,
				INFORME_SEIS_JASPER);
	}

	@Security.Authenticated(Secured.class)
	public static Result generar() {
		Form<Reporte> reporteForm = form(Reporte.class).bindFromRequest();

		if (reporteForm.hasErrors()) {
			return badRequest(views.html.reporte.reporte.render(reporteForm,
					Usuario.findByEmail(session("dni"))));
		}

		Reporte reporte = reporteForm.get();

		return ok(views.html.reporte.listar
				.render(reporte.mes.getNumero(),
						reporte.anio,
						reporte.provincia != null
								&& reporte.provincia.id != null ? reporte.provincia.id
								.toString() : "",
						reporte.departamento != null
								&& reporte.departamento.id != null ? reporte.departamento.id
								.toString() : "",
						reporte.supervisor != null
								&& reporte.supervisor.getId() != null ? reporte.supervisor
								.getId().toString() : "", Usuario
								.findByEmail(session("dni"))));
	}

	protected static Status obtenerResultadoDelInforme(Long mes, String anio,
			String idProvincia, String idDepartamento, String idSupervisor,
			String jrxml, String jasper) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		String pathRaizReportes = Play.application().path() + "/"
				+ Play.application().configuration().getString("informe.path");

		Map<String, Object> parametros = new HashMap<String, Object>();

		parametros.put("p_anio", anio);
		parametros.put("p_mes", mes);

		parametros.put("p_tabla", "v_informe_campania_por_provincia");
		if (!"".equals(idProvincia)) {
			parametros.put("p_id_provincia", idProvincia);
			parametros.put("p_tabla", "v_informe_campania_por_provincia");
		}

		if (!"".equals(idDepartamento)) {
			parametros.put("p_id_departamento", idDepartamento);
			parametros.put("p_tabla", "v_informe_campania_por_departamento");
		}

		if (!"".equals(idSupervisor)) {
			parametros.put("p_id_supervisor", idSupervisor);
			parametros.put("p_tabla", "v_informe_campania_por_supervisor");
		}

		try {
			JasperCompileManager.compileReportToFile(pathRaizReportes + jrxml,
					pathRaizReportes + jasper);

			ImageIO.write(obtenerBufferedImage(JasperPrintManager
					.printPageToImage((JasperPrint) JasperFillManager
							.fillReport(
									pathRaizReportes + jasper,
									parametros,
									DB.getConnection(Usuario.findByEmail(
											session("dni")).getServer())), 0,
							1f)), "JPEG", stream);
		} catch (JRException e) {
			Logger.of(ReporteController.class).error(
					"Ocurrio un error al crear el reporte", e);
		} catch (IOException e) {
			Logger.of(ReporteController.class).error(
					"Ocurrio un error al crear el reporte", e);
		}

		Status as = ok(stream.toByteArray()).as("image/jpeg");
		return as;
	}

	private static BufferedImage obtenerBufferedImage(Image image) {
		BufferedImage bi = new BufferedImage(image.getWidth(null),
				image.getHeight(null), BufferedImage.TYPE_INT_RGB);

		bi.createGraphics().drawImage(image, 0, 0, null);

		return bi;
	}
}
