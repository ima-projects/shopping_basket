package com.ibtisam.priceBasket.discounts

import com.ibtisam.priceBasket.commonTypes.{Basket, Item}
import com.ibtisam.priceBasket.commonTypes.Discount.Discount
import com.ibtisam.priceBasket.discounts.ApplyDiscount._

import scala.math.BigDecimal.RoundingMode
import com.ibtisam.priceBasket.commonTypes.Discount.ConditionalDiscount
import com.ibtisam.priceBasket.discounts.MatchConditionalDiscounts.processConditionalDiscount

object CalculateDiscountedItems {

  def getTotal(basket: Basket, discounted: Boolean): BigDecimal = {
      BigDecimal(
        basket.items
          .map(item => item.getCost(discounted))
          .sum)
        .setScale(2, RoundingMode.HALF_EVEN)   
        }

  
  def getMaxNumberOfDiscounts(discounts: List[Discount], countOfItems: Map[String, Int]): List[Discount] = {
    
    discounts.map(discount =>
      if (discount.numberOfTimesToApply > countOfItems(discount.item))
        {discount.copy(numberOfTimesToApply = countOfItems(discount.item))
        } else {
          discount
        })
  }
  

  def generateCorrectNumberOfDiscount(
    discounts: List[Discount],
    mapOfPrice: Map[String, Double],
    countOfItems: Map[String, Int])
      : List[Discount] = {

        val allDiscounts = for {
          dis <- getMaxNumberOfDiscounts(discounts, countOfItems)
          _ <- 0 until dis.numberOfTimesToApply
        } yield Discount (dis.item, dis.discount, dis.numberOfTimesToApply)
      
        allDiscounts
      }
  
  
  def calculateDiscountedItems(initialBasket: Basket, 
                              priceMap: Map[String, Double], 
                              conditionalDiscounts: List[ConditionalDiscount], 
                              discounts: List[Discount]): Basket = {
  
  val allDiscounts = (discounts ::: processConditionalDiscount(
    initialBasket,
    conditionalDiscounts
    )) filter (d => initialBasket.items.map(i => i.name).contains(d.item))
  
  getBasketWithDiscountsNowApplied(
    initialBasket,
    generateCorrectNumberOfDiscount(
      allDiscounts,
      priceMap,
      initialBasket.items.map(g => g.name).groupBy(identity).mapValues(_.size).toMap
    )
  )
  }
}
