package com.ibtisam.priceBasket.utils

import scala.io.Source
import com.ibtisam.priceBasket.commonTypes.Discount.{ConditionalDiscount, Condition, Discount}


object ReadCSVFile {
  
    def readCSVToMap(path: String): Map[String, Double] = {
        val file = 
            Source
                .fromFile(path)
        
        file.getLines()
            .map(line => {
                val columns = line.split(",").map(_.trim)
                Map(columns(0) -> columns(1).toDouble)
            })
            .toList
            .reduce(_++_)
    }

    def readCSVtoDiscount(path: String, countOfItems: Map[String, Int]): List[Discount] = {
        val file = 
            Source
                .fromFile(path)

        file.getLines()
            .map(line => {
                val columns: Array[String] = line.split(",").map(_.trim)
                Discount(columns(0), columns(1).toDouble, countOfItems.getOrElse(columns(0), 0))

            })
            .toList
    }

    // def readCSVtoSingleDiscount(path: String, countOfItems: Map[String, Int]): Discount = {
    //     val file = 
    //         Source
    //             .fromFile(path)
        
    //     file.getLines()
    //         .map(line => {
    //             val columns: Array[String] = line.split(",").map(_.trim)
    //             Discount(columns(0), columns(1).toDouble, countOfItems.getOrElse(columns(0), 0))

    //         })        
    // }

    def readCSVtoSingleDiscount(path: String, countOfItems: Map[String, Int]): Discount = {
        val file = Source.fromFile(path)
  
        val discounts = file.getLines().map(line => {
            val columns: Array[String] = line.split(",").map(_.trim)
            Discount(columns(0), columns(1).toDouble, countOfItems.getOrElse(columns(0), 0))
        }).toList
        
        discounts.head
        }


    def readCSVtoConditionalDiscount(path: String): List[ConditionalDiscount] = {
        val file = 
            Source
                .fromFile(path)
        
        file.getLines()
            .map(line => {
                val columns: Array[String] = line.split(",").map(_.trim)
                ConditionalDiscount(
                    columns(0),
                    columns(1).toDouble,
                    Condition(columns(2).split("/").map(_.trim).toList)
                )
            })
            .toList
    }
}
