/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.sigess.entities.inp;

import co.sigess.entities.emp.Area;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author fmoreno
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "programacion", schema = "inp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Programacion.findAll", query = "SELECT p FROM Programacion p")})
public class Programacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name = "programacion_id_seq", schema = "inp", sequenceName = "programacion_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "programacion_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero_inspecciones")
    private short numeroInspecciones;
        
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero_realizadas")
    private short numeroRealizadas;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @NotNull
    @JoinColumn(name = "fk_area_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Area area;
    
    @OneToMany(mappedBy = "programacion")
    private List<Inspeccion> inspeccionList;
    
    @JoinColumns({
        @JoinColumn(name = "fk_lista_inspeccion_id", referencedColumnName = "id")
        , @JoinColumn(name = "fk_lista_inspeccion_version", referencedColumnName = "version")})
    @ManyToOne(optional = false)
    private ListaInspeccion listaInspeccion;

    public Programacion() {
    }

    public Programacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getNumeroInspecciones() {
        return numeroInspecciones;
    }

    public void setNumeroInspecciones(short numeroInspecciones) {
        this.numeroInspecciones = numeroInspecciones;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @XmlTransient
    @JsonIgnore
    public List<Inspeccion> getInspeccionList() {
        return inspeccionList;
    }

    public void setInspeccionList(List<Inspeccion> inspeccionList) {
        this.inspeccionList = inspeccionList;
    }

    public ListaInspeccion getListaInspeccion() {
        return listaInspeccion;
    }

    public void setListaInspeccion(ListaInspeccion listaInspeccion) {
        this.listaInspeccion = listaInspeccion;
    }

    public short getNumeroRealizadas() {
        return numeroRealizadas;
    }

    public void setNumeroRealizadas(short numeroRealizadas) {
        this.numeroRealizadas = numeroRealizadas;
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
        if (!(object instanceof Programacion)) {
            return false;
        }
        Programacion other = (Programacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.sigess.entities.inp.Programacion[ id=" + id + " ]";
    }
    
}
