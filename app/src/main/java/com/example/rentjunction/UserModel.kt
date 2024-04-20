package com.example.rentjunction

class UserModel {
    var name: String? = null
    var email: String? = null
    var address: String? = null
    var password: String? = null
    var phone: String? = null

    constructor() {}

    constructor(name: String?, email: String?, address: String?, password: String?, phone: String?) {
        this.name = name
        this.email = email
        this.address = address
        this.password = password
        this.phone = phone
    }
}
