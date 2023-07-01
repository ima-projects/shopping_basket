package com.ibtisam.priceBasket.discounts

import com.ibtisam.priceBasket.commonTypes.Basket
import com.ibtisam.priceBasket.commonTypes.Discount.{Discount, ConditionalDiscount}

object MatchConditionalDiscounts {
  
  private def filterDiscounts(
    numberOfItemsRequired: Map[String, Int],
    numberOfItemsInBasket: Map[String, Int]
  ): Boolean = {
    numberOfItemsRequired
      .filter(
        requiredItem =>
          requiredItem._2 <= numberOfItemsInBasket
            .filter(
                numberOfItem => numberOfItemsRequired.contains(numberOfItem._1)
            )
            .getOrElse(requiredItem._1, 0)
      )
      .equals(numberOfItemsRequired)
  }



  def processConditionalDiscount(basket: Basket, discounts: List[ConditionalDiscount]): List[Discount] = {
    discounts
      .filter { conditionalDiscount =>
        val numberOfItemsRequired: Map[String, Int] = conditionalDiscount.condition.countOccurrencesOfEachItem

        filterDiscounts(numberOfItemsRequired, basket.countQuantityOfEach)
      }
      .map { conditionalDiscount =>
        val numberOfItemsRequired: Map[String, Int] = conditionalDiscount.condition.countOccurrencesOfEachItem

        val minCountOfRequiredItems: Int = numberOfItemsRequired
          .map { case (item, count) =>
            basket.countQuantityOfEach(item) / count
          }
          .min

        Discount(conditionalDiscount.item, conditionalDiscount.discount, minCountOfRequiredItems)
      }
  }

}
