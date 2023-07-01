package com.ibtisam.priceBasket.commonTypes

import scala.math.BigDecimal.RoundingMode

case class Item (name: String, price: Double, discountedPrice: Double){

  def getCost(discounted: Boolean): Double = 
    if (discounted) this.discountedPrice
    else this.price
}


case class Basket (items: List[Item]){

  def addItem(item: Item): Basket = {
    Basket(item :: items)
  }

  def getItems: List[Item] = items

  //def getTotalPrice: Double = Basket.calculateTotalPrice(items)

  def countQuantityOfEach: Map[String, Int] = 
    items.map(i => i.name).groupBy(identity).mapValues(_.size).toMap
  
  def add(basket:Basket): Basket = Basket(this.items ::: basket.items)

  def equals(basket: Basket): Boolean ={
    this.items.toSet == basket.items.toSet && this.items.size == basket.items.size
  }

}


// used during testing cases to be more expicit
object Basket {

  def apply(items: List[String], pricesMap: Map[String, Double]): Basket =
    Basket(items.map(item => Item(item, pricesMap(item), pricesMap(item))))
}

object StatrerCalc {
  def calculateTotalPrice(basket: Basket): Double = {
          BigDecimal(basket.items.map(_.price).sum)
          .setScale(2, RoundingMode.HALF_EVEN)
          .toDouble
  }  

}


    