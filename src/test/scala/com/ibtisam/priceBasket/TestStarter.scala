package com.ibtisam.priceBasket

import com.ibtisam.priceBasket.commonTypes.{Item, Basket}
import com.ibtisam.priceBasket.commonTypes.StatrerCalc.{calculateTotalPrice}
import com.ibtisam.priceBasket.commonTypes.Discount.{Discount, ConditionalDiscount, Condition}



import com.ibtisam.priceBasket.discounts.ApplyDiscount._
import com.ibtisam.priceBasket.discounts.CalculateDiscountedItems.{getTotal, calculateDiscountedItems}
import com.ibtisam.priceBasket.discounts.MatchConditionalDiscounts.processConditionalDiscount

import munit.*
import java.awt.ItemSelectable


class TestStarter extends munit.FunSuite {

  // simply testing to see if an item could be added to a basket containing an list of items
  test("should be able to add an item to a basket and return an updated basket"){
    val basket = Basket(List( Item("Milk", 1.30, 1.30), Item("Bread", 0.80, 1.30)))
    val item = Item("Apples", 1.00, 1.00)
    

    val updatedBasket = basket.addItem(item)

    val updatedTotal = calculateTotalPrice(updatedBasket)

    //assertEquals(updatedBasket.getItems.length, 1)
    //assertEquals(updatedBasket.getItems.head, item)
    assert(updatedTotal == 3.10) //item.price
  }


  // only discounted items. 10% off Apples is the simplest discount to test for at the start
  test("should return an item where a discount has been applied"){
    val basket = Basket(List(Item("Apples", 1.00, 1.00), Item("Milk", 1.30, 1.30), Item("Bread", 0.80, 0.80)))
    
    val discountPercent = Discount("Apples", 0.10, 1) // 10% discount

    val discountedApple = filterDiscount(basket, List(discountPercent))

    val expectedDiscountedItem = Basket(List(Item("Apples", 1.00, 0.90)))

    assertEquals(discountedApple, expectedDiscountedItem)
    
  }

  // only non-discounted items
  test("should return all the items not discounted"){
    // reminder that the price in the each tuple is not being used for the test but is needed for the Item constructor
    val basket = Basket(List(Item("Apples", 1.00, 1.00), Item("Milk", 1.30, 1.30), Item("Bread", 0.80, 0.80)))

    // just extracting the name parameter from the Items object not the price or discount to just get the size of list
    val discountPercent = Basket(List(Item("Apples", 1.00, 0.10)))

    val nonDiscountedItems = filterForItemsNotDiscounted(basket, discountPercent)

    val expectedItems = Map("Milk" -> 1, "Bread" -> 1)

    assertEquals(nonDiscountedItems, expectedItems)

  }

  // testing most minimal discount application implementation on the whole basket (includes discounts and not discounted items)
  test("should return the whole basket where the necessary discounts are applied"){
    val basket = Basket(List(Item("Apples", 1.00, 1.00), Item("Milk", 1.30, 1.30), Item("Bread", 0.80, 0.80)))

    val discountPercent = Discount("Apples", 0.10, 1)

    val updatedItemsWithDiscount = getBasketWithDiscountsNowApplied(basket, List(discountPercent))

    val expectedBasket = Basket(List(Item("Apples", 1.00, 0.90), Item("Milk", 1.30, 1.30), Item("Bread", 0.80, 0.80)))

    assertEquals(updatedItemsWithDiscount, expectedBasket)
  }


  // most minimal test implementation to calculate the total price
  test("should return the total price with one discounted price included") {
    val priceMap = 
      Map("Apples" -> 1.00, "Milk" -> 1.30, "Bread" -> 0.80)
    
    val basket = List("Apples", "Milk", "Bread")

    val discountedPercent = Discount("Apples", 0.10, 1)

    val itemsCalculated = calculateDiscountedItems(
      Basket.apply(basket, priceMap),
      priceMap,
      List.empty,
      List(discountedPercent)
    )

    val sumTotalWithDiscount = getTotal(itemsCalculated, discounted = true)
    val sumtotalWithoutDiscount = getTotal(itemsCalculated, discounted = false)


    assert(sumTotalWithDiscount == 3.00)
  }



  // dummy conditional test to see what would happen if two apples present in basket then milk will be half off (condition)
  test("should return a basket with discount that requires a condition to be satisfied"){

    val itemsInBasket = Basket(List(
      Item("Apples", 1.00, 1.00),
      Item("Apples", 1.00, 1.00),
      Item("Milk", 1.30, 1.30)
    ))

    val expectedItems = Basket(List(
      Item("Apples", 1.00, 1.00),
      Item("Apples", 1.00, 1.00),
      Item("Milk", 1.30, 0.65)
    ))

    val conditionalDiscount = ConditionalDiscount("Milk", 0.5, Condition(List("Apples", "Apples")))

    val Discount = processConditionalDiscount(itemsInBasket, List(conditionalDiscount))

    val actual = getBasketWithDiscountsNowApplied(itemsInBasket, Discount)
    
    assert(actual.equals(expectedItems))
  }


  
}
