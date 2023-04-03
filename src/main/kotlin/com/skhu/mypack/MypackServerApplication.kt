package com.skhu.mypack

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MypackServerApplication

fun main(args: Array<String>) {
    runApplication<MypackServerApplication>(*args)
}
