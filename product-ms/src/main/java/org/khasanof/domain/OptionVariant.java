package org.khasanof.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.khasanof.domain.enumeration.OptionVariantStatus;

/**
 * A OptionVariant.
 */
@Entity
@Table(name = "option_variant")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OptionVariant implements Serializable {

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
    private OptionVariantStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "variants" }, allowSetters = true)
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "variants", "product" }, allowSetters = true)
    private Option option;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OptionVariant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public OptionVariant name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OptionVariantStatus getStatus() {
        return this.status;
    }

    public OptionVariant status(OptionVariantStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OptionVariantStatus status) {
        this.status = status;
    }

    public Price getPrice() {
        return this.price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public OptionVariant price(Price price) {
        this.setPrice(price);
        return this;
    }

    public Option getOption() {
        return this.option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public OptionVariant option(Option option) {
        this.setOption(option);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionVariant)) {
            return false;
        }
        return getId() != null && getId().equals(((OptionVariant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OptionVariant{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
