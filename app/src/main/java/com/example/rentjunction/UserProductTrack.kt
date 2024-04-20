package com.example.rentjunction

class UserProductTrack {
    var head: String? = null
    var name: String? = null
    var rating: String? = null
    var description: String? = null
    var type: String? = null
    var image: String? = null
    var address: String? = null

    constructor() {}

    constructor(head: String?, name: String?, rating: String?, description: String?, type: String?, image: String?, address: String?) {
        this.head = head
        this.name = name
        this.rating = rating
        this.description = description
        this.type = type
        this.image = image
        this.address = address
    }
}
