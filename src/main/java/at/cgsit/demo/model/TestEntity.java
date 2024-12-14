package at.cgsit.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "test")
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_id_gen")
    @SequenceGenerator(name = "test_id_gen", sequenceName = "test_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Version
    @Column(name = "version_no", nullable = false)
    private Long versionNo;

    @Size(max = 500)
    @Column(name = "name", length = 500)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(final Long versionNo) {
        this.versionNo = versionNo;
    }

}