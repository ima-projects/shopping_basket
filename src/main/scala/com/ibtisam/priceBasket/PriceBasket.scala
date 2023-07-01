package com.ibtisam.priceBasket

import com.ibtisam.priceBasket.commonTypes.{Item, Basket}
import com.ibtisam.priceBasket.commonTypes.Basket.apply

import com.ibtisam.priceBasket.commonTypes.Discount.{Discount, ConditionalDiscount, Condition}
import com.ibtisam.priceBasket.discounts.CalculateDiscountedItems._

import com.ibtisam.priceBasket.utils.Logging._
import com.ibtisam.priceBasket.utils.ReadCSVFile._

/*Start of the application class*/

object PriceBasket {

  def main(args: Array[String]): Unit = {

    try {
      if (args.length == 0) {
        println("Please specify at least one item")
      } else {
        
        val priceMap = readCSVToMap("src/main/resources/prices.csv")
    
        val basket = Basket.apply(args.toList, priceMap)

        val conditionalDiscounts = readCSVtoConditionalDiscount("src/main/resources/conditional_discounts.csv")

        val discounts = readCSVtoDiscount("src/main/resources/discounts.csv", basket.countQuantityOfEach)

        val basketCalculated = calculateDiscountedItems(
          basket,
          priceMap,
          conditionalDiscounts,
          discounts
        )

        outputTotalBasketCost(basketCalculated)

      }
    } catch {
      case ex: NoSuchElementException =>
      // handling the exception
      println("****** An element in basket does not exist. Please try again. *******")
      println(s"Error: ${ex.getMessage}")
    }

  }
}


