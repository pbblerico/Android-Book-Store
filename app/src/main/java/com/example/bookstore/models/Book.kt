package com.example.bookstore.models

class Book{
    var id: String = ""
    var bookName: String = ""
    var desc: String = ""
    var author: String = ""
    var cost: Double = 0.0
    var category: String = ""
    var timestamp: Long = 0
    var uid: String = ""

    constructor()
    constructor(id: String, bookName: String, desc: String, author: String, cost: Double, category: String, timestamp: Long, uid: String) {
        this.id = id
        this.bookName = bookName
        this.desc = desc
        this.author = author
        this.cost = cost
        this.category = category
        this.timestamp = timestamp
        this.uid = uid
    }

//    override fun compareTo(other: Book): Int {
//        return (this.cost - other.cost).toInt()
//    }
}