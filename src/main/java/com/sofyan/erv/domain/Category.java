package com.sofyan.erv.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "M_CATEGORY")
public class Category extends BaseEntity implements Serializable {

    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY)
    private Set<Product> listProduct;

    public Set<Product> getListProduct() {
        return listProduct;
    }

    public void setListProduct(Set<Product> listProduct) {
        this.listProduct = listProduct;
    }

}
