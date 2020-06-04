/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.sigess.entities.emp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author fmoreno
 */
@Entity
@Table(name = "hht", schema = "emp")
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hht.findAll", query = "SELECT h FROM Hht h")})
public class Hht implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "hht_id_seq", schema = "emp", sequenceName = "hht_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hht_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "anio")
    private Short anio;
    
    @Column(name = "mes")
    private Short mes;
    
    @Column(name = "valor")
    private Integer valor;
        
    @JoinColumn(name = "fk_empresa_id", referencedColumnName = "id")
    private Empresa empresa;

    public Hht() {
    }

    public Hht(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getAnio() {
        return anio;
    }

    public void setAnio(Short anio) {
        this.anio = anio;
    }

    public Short getMes() {
        return mes;
    }

    public void setMes(Short mes) {
        this.mes = mes;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    @JsonIgnore
    public Empresa getEmpresa() {
        return empresa;
    }

    @JsonProperty("empresa")
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof Hht)) {
            return false;
        }
        Hht other = (Hht) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.sigess.entities.emp.Hht[ id=" + id + " ]";
    }
    
}
