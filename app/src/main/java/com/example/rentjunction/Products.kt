package com.example.rentjunction

class Products {
    var name: String? = null
    var description: String? = null
    var rating: String? = null
    var price: String? = null
    var image: String? = null

    constructor() {}
    constructor(
        name: String?,
        description: String?,
        rating: String?,
        price: String?,
        image: String?
    ) {
        this.name = name
        this.description = description
        this.rating = rating
        this.price = price
        this.image = image
    }
}