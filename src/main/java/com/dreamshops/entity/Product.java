package com.dreamshops.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
	private String name;
	private String brand;
	private BigDecimal price;
	private int inventry;
	private String description;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Image> images;

}
