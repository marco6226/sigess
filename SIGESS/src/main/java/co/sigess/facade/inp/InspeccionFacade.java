/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.sigess.facade.inp;

import co.sigess.entities.com.Mensaje;
import co.sigess.entities.com.TipoMensaje;
import co.sigess.entities.conf.RespuestaCampo;
import co.sigess.entities.inp.Calificacion;
import co.sigess.entities.inp.Inspeccion;
import co.sigess.entities.inp.ListaInspeccion;
import co.sigess.entities.inp.Programacion;
import co.sigess.exceptions.UserMessageException;
import co.sigess.facade.com.AbstractFacade;
import co.sigess.facade.conf.RespuestaCampoFacade;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author fmoreno
 */
@Stateless
public class InspeccionFacade extends AbstractFacade<Inspeccion> {

    @EJB
    private ProgramacionFacade programacionFacade;

    @EJB
    private CalificacionFacade calificacionFacade;

    @EJB
    private RespuestaCampoFacade respuestaCampoFacade;

    @PersistenceContext(unitName = "SIGESS_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InspeccionFacade() {
        super(Inspeccion.class);
    }

    @Override
    public Inspeccion create(Inspeccion inspeccion) throws Exception {
        if (inspeccion.getCalificacionList() == null || inspeccion.getCalificacionList().isEmpty()) {
            throw new IllegalArgumentException("La inspección especificada no contiene la lista de calificaciones");
        }
        if (inspeccion.getProgramacion() == null) {
            return this.crearInspeccionNoProgramada(inspeccion);
        } else {
            return this.crearInspeccionProgramada(inspeccion);
        }

    }

    private Inspeccion crearInspeccionNoProgramada(Inspeccion inspeccion) throws Exception {
        if (inspeccion.getListaInspeccion() == null
                || inspeccion.getListaInspeccion().getListaInspeccionPK() == null
                || inspeccion.getListaInspeccion().getListaInspeccionPK().getId() == 0
                || inspeccion.getListaInspeccion().getListaInspeccionPK().getVersion() == 0) {
            throw new IllegalArgumentException("No se ha establecido la lista de inspección");
        }
        if (inspeccion.getArea() == null || inspeccion.getArea().getId() == null) {
            throw new IllegalArgumentException("No se ha establecido el área de la inspección");
        }

        if (!this.validarCalificaciones(inspeccion)) {
            throw new IllegalArgumentException("Error en la calificación recibida: no contiene un elemento de inspección o una opción seleccionada");
        }
        for (RespuestaCampo rc : inspeccion.getRespuestasCampoList()) {
            respuestaCampoFacade.create(rc);
        }
        inspeccion.setFechaRealizada(new Date());
        super.create(inspeccion);
        for (Calificacion calificacion : inspeccion.getCalificacionList()) {
            calificacion.setInspeccion(inspeccion);
            calificacionFacade.create(calificacion);
        }
        return inspeccion;
    }

    private Inspeccion crearInspeccionProgramada(Inspeccion inspeccion) throws Exception {
        Programacion progDB = programacionFacade.find(inspeccion.getProgramacion().getId());
        if (progDB == null) {
            throw new IllegalArgumentException("No se ha establecido la programación a la que pertenece la inspección");
        }
        if (progDB.getNumeroInspecciones() == progDB.getNumeroRealizadas()) {
            Mensaje msg = new Mensaje(
                    "INSPECCIONES COMPLETADAS",
                    "Ya se han realizado todas las inspecciones de la programación [" + progDB.getId() + "]",
                    TipoMensaje.info
            );
            throw new UserMessageException(msg);
        }
        ListaInspeccion listaInp = progDB.getListaInspeccion();

        if (inspeccion.getCalificacionList().size() != listaInp.getNumeroPreguntas()) {
            throw new IllegalArgumentException("El número de preguntas no coincide con el número de respuestas");
        }

        if (!this.validarCalificaciones(inspeccion)) {
            throw new IllegalArgumentException("Error en la calificación recibida: no contiene un elemento de inspección o una opción seleccionada");
        }

        for (RespuestaCampo rc : inspeccion.getRespuestasCampoList()) {
            respuestaCampoFacade.create(rc);
        }

        progDB.setNumeroRealizadas((short) (progDB.getNumeroRealizadas() + 1));
        programacionFacade.edit(progDB);

        inspeccion.setFechaRealizada(new Date());
        super.create(inspeccion); //To change body of generated methods, choose Tools | Templates.

        for (Calificacion calificacion : inspeccion.getCalificacionList()) {
            calificacion.setInspeccion(inspeccion);
            calificacionFacade.create(calificacion);
        }
        return inspeccion;
    }

    private boolean validarCalificaciones(Inspeccion inspeccion) {
        for (Calificacion calificacion : inspeccion.getCalificacionList()) {
            if (calificacion.getElementoInspeccion() == null || calificacion.getOpcionCalificacion() == null) {
                return false;
            }
            if (calificacion.getNivelRiesgo() != null && calificacion.getNivelRiesgo().getId() == null) {
                calificacion.setNivelRiesgo(null);
            }
            if (calificacion.getTipoHallazgo() == null || calificacion.getTipoHallazgo().getId() == null) {
                calificacion.setTipoHallazgo(null);
            }
        }
        return true;
    }

    @Override
    public Inspeccion edit(Inspeccion inspeccion) throws Exception {
        if (inspeccion.getId() == null) {
            throw new IllegalArgumentException("No se ha establecido el id de la inspección a modificar");
        }

        for (Calificacion calificacion : inspeccion.getCalificacionList()) {
            if (calificacion.getId() == null) {
                throw new IllegalArgumentException("No se ha establecido el id de una de las calificaciones de la inspección");
            }
            if (calificacion.getNivelRiesgo() != null && calificacion.getNivelRiesgo().getId() == null) {
                calificacion.setNivelRiesgo(null);
            }
        }
        Inspeccion inspDB = this.find(inspeccion.getId());
        inspDB.setUsuarioModifica(inspeccion.getUsuarioModifica());
        inspDB.setFechaModificacion(new Date());
        inspDB.setDescripcion(inspeccion.getDescripcion());
        inspDB.setEquipo(inspeccion.getEquipo());
        inspDB.setLugar(inspeccion.getLugar());
        inspDB.setMarca(inspeccion.getMarca());
        inspDB.setModelo(inspeccion.getModelo());
        inspDB.setObservacion(inspeccion.getObservacion());
        inspDB.setSerial(inspeccion.getSerial());
        inspDB.setRespuestasCampoList(inspeccion.getRespuestasCampoList());
        for (RespuestaCampo rc : inspDB.getRespuestasCampoList()) {
            if (rc.getId() == null) {
                respuestaCampoFacade.create(rc);
            } else {
                respuestaCampoFacade.edit(rc);
            }
        }

        for (Calificacion calificacion : inspDB.getCalificacionList()) {
            for (Calificacion calfMod : inspeccion.getCalificacionList()) {
                if (calfMod.getId().equals(calificacion.getId())) {
                    calificacion.setNivelRiesgo(calfMod.getNivelRiesgo());
                    calificacion.setOpcionCalificacion(calfMod.getOpcionCalificacion());
                    calificacion.setRecomendacion(calfMod.getRecomendacion());
                    calificacion.setTipoHallazgo(calfMod.getTipoHallazgo() == null ? null : (calfMod.getTipoHallazgo().getId() == null ? null : calfMod.getTipoHallazgo()));
                    break;
                }
            }
            this.calificacionFacade.edit(calificacion);
        }

        return super.edit(inspDB); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Inspeccion> findAllByEmpresa(Integer empresaId) {
        Query query = this.em.createQuery("SELECT inp FROM Inspeccion inp WHERE inp.empresa.id = ?1");
        query.setParameter(1, empresaId);
        List<Inspeccion> list = (List<Inspeccion>) query.getResultList();
        return list;
    }

    public ByteArrayOutputStream consultarConsolidado(Integer empresaId, Date desde, Date hasta, Integer listaId, Integer listaVersion) throws IOException {
        Query q = this.em.createNativeQuery("SELECT data FROM  inp.generar_consolidado_inspecciones(?1, ?2, ?3, ?4, ?5)");
        q.setParameter(1, empresaId);
        q.setParameter(2, listaId);
        q.setParameter(3, listaVersion);
        q.setParameter(4, desde);
        q.setParameter(5, hasta);
        List<String> lines = q.getResultList();
        if (lines.isEmpty()) {
            throw new UserMessageException("Datos no encontrados", "No se hallaron inspecciones en el rango de fechas seleccionado", TipoMensaje.info);
        }

        ByteArrayOutputStream bOutput = new ByteArrayOutputStream(2_000 + (lines.size() * 400));

        for (String line : lines) {
            bOutput.write(line.getBytes());
            bOutput.write("\n".getBytes());
        }
        return bOutput;
    }

}
