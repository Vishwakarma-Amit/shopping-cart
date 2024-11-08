package com.dreamshops.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Category {

	@Id
	private Long categoryId;
	private String name;
	
	@OneToMany(mappedBy = "category")
	private List<Product> product;

    public Category(String name) {
		this.name = name;
    }
}
