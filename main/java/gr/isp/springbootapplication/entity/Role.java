package gr.isp.springbootapplication.entity;

import javax.persistence.*;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String authority) {
        this.role = authority;
    }
}
