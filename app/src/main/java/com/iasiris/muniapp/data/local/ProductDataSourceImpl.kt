package com.iasiris.muniapp.data.local

import com.iasiris.muniapp.data.model.Product
import jakarta.inject.Inject

class ProductDataSourceImpl @Inject constructor() : ProductDataSource {

    override fun getProducts(): List<Product> = products

    override fun getProductById(productId: String): Product? {
        return products.find { it.id == productId }
    }

    private val products = listOf(
        Product(
            "1",
            "Tacos al pastor",
            "Tortillas con carne de cerdo marinada, pi\u00f1a y cebolla.",
            "https://comedera.com/wp-content/uploads/sites/9/2017/08/tacos-al-pastor-receta.jpg",
            45.0,
            true,
            "Tacos",
            2
        ),
        Product(
            "2",
            "Hamburguesa clásica",
            "Pan artesanal, carne de res, lechuga, tomate y mayonesa.",
            "https://imag.bonviveur.com/hamburguesa-clasica.jpg",
            70.0,
            false,
            "Hamburguesas"
        ),
        Product(
            "3",
            "Pizza margarita",
            "Salsa de tomate, mozzarella y albahaca fresca.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDx46gfGPL3XKmoiXU_pQzvINxjjOFsXLoAA&s",
            120.0,
            false,
            "Pizzas"
        ),
        Product(
            "4",
            "Hot dog especial",
            "Salchicha jumbo, tocino, cebolla caramelizada y mostaza.",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQaKqDVCWtGQzA2-d1WvRmTfPi0krczx2pwzQ&s",
            50.0,
            true,
            "Hot Dogs"
        ),
        Product(
            "5",
            "Empanadas de carne",
            "Empanadas rellenas de carne molida y especias.",
            "https://assets.unileversolutions.com/recipes-v3/237001-default.jpg",
            60.0,
            false,
            "Empanadas",
            5
        )
    )
}