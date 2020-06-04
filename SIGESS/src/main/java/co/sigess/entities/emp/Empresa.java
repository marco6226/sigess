/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.sigess.entities.emp;

import co.sigess.entities.com.Arl;
import co.sigess.entities.com.Ciiu;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author fmoreno
 */
@Entity
@Table(name = "empresa", schema = "emp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e")})
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name = "empresa_id_seq", schema = "emp", sequenceName = "empresa_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empresa_id_seq")
    @Column(name = "id")
    private Integer id;

    @Size(max = 45)
    @Column(name = "nit")
    private String nit;

    @Column(name = "activo")
    private Boolean activo;

    @Size(max = 100)
    @Column(name = "nombre_comercial")
    private String nombreComercial;

    @Size(max = 100)
    @Column(name = "razon_social")
    private String razonSocial;
    
    @Column(name = "logo")
    private String logo;

    @JoinColumn(name = "fk_arl_id", referencedColumnName = "id")
    @ManyToOne
    private Arl arl;

    @JoinColumn(name = "fk_grupo_empresarial_id", referencedColumnName = "id")
    @ManyToOne
    private GrupoEmpresarial grupoEmpresarial;

    @OneToMany(mappedBy = "empresa")
    private List<UsuarioEmpresa> usuarioEmpresaList;

    @JsonIgnore
    @JoinTable(name = "contratista_empresa", schema = "emp", joinColumns = {
        @JoinColumn(name = "fk_empresa_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "fk_empresa_contratista_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Empresa> empresasContratistasList;

    @JoinColumn(name = "fk_ciiu_id", referencedColumnName = "id")
    @ManyToOne
    private Ciiu ciiu;

    public Empresa() {
    }
    
    @JsonIgnore
    public String getAsJSON() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Empresa(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Arl getArl() {
        return arl;
    }

    public void setArl(Arl arl) {
        this.arl = arl;
    }

    public GrupoEmpresarial getGrupoEmpresarial() {
        return grupoEmpresarial;
    }

    public void setGrupoEmpresarial(GrupoEmpresarial grupoEmpresarial) {
        this.grupoEmpresarial = grupoEmpresarial;
    }

    public List<Empresa> getEmpresasContratistasList() {
        return empresasContratistasList;
    }

    public void setEmpresasContratistasList(List<Empresa> empresasContratistasList) {
        this.empresasContratistasList = empresasContratistasList;
    }

    public Ciiu getCiiu() {
        return ciiu;
    }

    public void setCiiu(Ciiu ciiu) {
        this.ciiu = ciiu;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.sigess.entities.empEmpresa[ id=" + id + " ]";
    }

    @XmlTransient
    public List<UsuarioEmpresa> getUsuarioEmpresaList() {
        return usuarioEmpresaList;
    }

    public void setUsuarioEmpresaList(List<UsuarioEmpresa> usuarioEmpresaList) {
        this.usuarioEmpresaList = usuarioEmpresaList;
    }

//    @XmlTransient
//    public List<Perfil> getPerfilList() {
//        return perfilList;
//    }
//
//    public void setPerfilList(List<Perfil> perfilList) {
//        this.perfilList = perfilList;
//    }
}
