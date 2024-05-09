package org.khasanof.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.khasanof.domain.enumeration.OptionStatus;

/**
 * A Option.
 */
@Entity
@Table(name = "option")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Option implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OptionStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "option")
    @JsonIgnoreProperties(value = { "price", "option" }, allowSetters = true)
    private Set<OptionVariant> variants = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "options", "images", "tags", "gifts", "category" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Option id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Option name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OptionStatus getStatus() {
        return this.status;
    }

    public Option status(OptionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OptionStatus status) {
        this.status = status;
    }

    public Set<OptionVariant> getVariants() {
        return this.variants;
    }

    public void setVariants(Set<OptionVariant> optionVariants) {
        if (this.variants != null) {
            this.variants.forEach(i -> i.setOption(null));
        }
        if (optionVariants != null) {
            optionVariants.forEach(i -> i.setOption(this));
        }
        this.variants = optionVariants;
    }

    public Option variants(Set<OptionVariant> optionVariants) {
        this.setVariants(optionVariants);
        return this;
    }

    public Option addVariants(OptionVariant optionVariant) {
        this.variants.add(optionVariant);
        optionVariant.setOption(this);
        return this;
    }

    public Option removeVariants(OptionVariant optionVariant) {
        this.variants.remove(optionVariant);
        optionVariant.setOption(null);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Option product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Option)) {
            return false;
        }
        return getId() != null && getId().equals(((Option) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Option{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
