package edu.kpi.fict.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "constructors")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Constructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String constructorId;

    @Column
    private String name;

    @Column
    private String nationality;

    @Column
    private boolean uploaded;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConstructorId() {
        return constructorId;
    }

    public void setConstructorId(String constructorId) {
        this.constructorId = constructorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(final boolean uploaded) {
        this.uploaded = uploaded;
    }
}
