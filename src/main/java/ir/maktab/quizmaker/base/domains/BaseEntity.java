package ir.maktab.quizmaker.base.domains;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public class BaseEntity<PK extends Serializable> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private PK id;

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }
}
