package com.ibtisam.priceBasket.utils

import com.ibtisam.priceBasket.commonTypes.{Item, Basket}
import com.ibtisam.priceBasket.commonTypes.Discount.Discount

import com.ibtisam.priceBasket.discounts.CalculateDiscountedItems.{getTotal}

import com.ibtisam.priceBasket.utils.ReadCSVFile._

import scala.math.BigDecimal.RoundingMode


object Logging {

    
    def outputDiscount(discount: Discount, oldPrice: Double, basket: Basket): Unit = {
        
        val savings = oldPrice - (oldPrice * (1 - discount.discount))

        println(
        discount.item + " " + (discount.discount * 100) + "% off: " + (BigDecimal(
            savings
        ) * 100)
            .setScale(0, RoundingMode.HALF_EVEN) + "p"
        )
    }
    


    def outputTotalBasketCost(basket: Basket): Unit = {
    println(s"Subtotal: £${getTotal(basket, discounted = false)}")

    if (getTotal(basket, discounted = false) == getTotal(
            basket,
            discounted = true))
        { println("(No offers available)")
    } else {
        val logDiscount = readCSVtoSingleDiscount("src/main/resources/discounts.csv", basket.countQuantityOfEach)
    
        outputDiscount(logDiscount, getTotal(basket, discounted = false).toDouble, basket)}
    
    println(s"Total price: £${getTotal(basket, discounted = true)}")
    }

}
