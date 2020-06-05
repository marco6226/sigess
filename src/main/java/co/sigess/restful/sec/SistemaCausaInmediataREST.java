/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.sigess.restful.sec;

import co.sigess.entities.sec.SistemaCausaInmediata;
import co.sigess.facade.sec.SistemaCausaInmediataFacade;
import co.sigess.restful.ServiceREST;
import co.sigess.restful.security.Compress;
import co.sigess.restful.security.Secured;
import co.sigess.util.Util;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author fmoreno
 */
@Secured
@Path("sistemaCausaInmediata")
public class SistemaCausaInmediataREST extends ServiceREST {

    @EJB
    private SistemaCausaInmediataFacade sistemaInmediataFacade;

    @GET
    @Compress
    @Path("seleccionado")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response findDefault() {
        try {
            SistemaCausaInmediata scr = sistemaInmediataFacade.findDefault(super.getEmpresaIdRequestContext());
            return Response.ok(scr).build();
        } catch (Exception ex) {
            return Util.manageException(ex, SistemaCausaInmediataREST.class);
        }
    }

}
