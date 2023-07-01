package com.ibtisam.priceBasket

import com.ibtisam.priceBasket.commonTypes.{Item, Basket}
import com.ibtisam.priceBasket.commonTypes.Discount.{Discount, ConditionalDiscount, Condition}

import com.ibtisam.priceBasket.discounts.CalculateDiscountedItems.{getTotal, calculateDiscountedItems}



import munit.*

class TestPriceBasket extends munit.FunSuite{
  

// testing an unconditional discount (regular discount multiple times)
test("should return total price of a basket where the same discount can be applied multiple times") {
    val priceMap = Map("Apples" -> 1.00, "Bread" -> 0.80)

    val basket = List("Apples", "Apples", "Apples", "Bread", "Bread")

    val discounts = Discount("Apples", 0.1, 3)

    val itemsCalculated = calculateDiscountedItems(
        Basket.apply(basket, priceMap),
        priceMap,
        List.empty,
        List(discounts)
    )

    val sumTotalWithDiscount = getTotal(itemsCalculated, discounted = true)
    val sumTotalWithoutDiscount = getTotal(itemsCalculated, discounted = false)


    assert(sumTotalWithDiscount == 4.30)
    assert(sumTotalWithoutDiscount == 4.60)
  }


  // testing the different ways to apply conditional discounts starting with the Soup special offer from assignment 

  test("should return a basket where the soup conditional offer as been applied") {
    val priceMap = Map("Soup" -> 0.65, "Bread" -> 0.80)

    val basket = List("Soup", "Soup", "Bread", "Soup", "Soup", "Bread")

    val twoTinsOfSoupAndBreadDiscount = ConditionalDiscount("Bread", 0.5, Condition(List("Soup", "Soup")))

    // val discounts = Discount("Bread", 0.5, 2)

    
    val itemsCalculated = calculateDiscountedItems(
        Basket.apply(basket, priceMap),
        priceMap,
        List(twoTinsOfSoupAndBreadDiscount),
        List.empty
    )

    val sumTotalWithDiscount = getTotal(itemsCalculated, discounted = true)
    val sumTotalWithoutDiscount = getTotal(itemsCalculated, discounted = false)

    assertEquals(sumTotalWithDiscount, BigDecimal(3.40))
    assert(sumTotalWithoutDiscount == 4.20)

  }

  // testing regular discount and conditional discount to calculate total price correctly
  test("should calculate the total price correctly even if extra discounts are applied to items that don't exist in basket"){
    val priceMap = Map("Soup" -> 0.65, "Bread" -> 0.80, "Milk" -> 1.30, "Apples" -> 1.0)

    val basket = List("Apples", "Milk", "Bread")

    val twoTinsOfSoupAndBreadDiscount = ConditionalDiscount("Bread", 0.5, Condition(List("Soup", "Soup")))

    val discounts = Discount("Apples", 0.1, 1)

    val itemsCalculated = calculateDiscountedItems(
        Basket.apply(basket, priceMap),
        priceMap,
        List(twoTinsOfSoupAndBreadDiscount),
        List(discounts)
    )

    val sumTotalWithDiscount = getTotal(itemsCalculated, discounted = true)
    val sumTotalWithoutDiscount = getTotal(itemsCalculated, discounted = false)

    assert(sumTotalWithDiscount == 3.00)
    assert(sumTotalWithoutDiscount == 3.10)
  }

  test("should be able to apply multiple regular discounts (unconditional discounts) and multiple conditional discounts"){
    val priceMap = Map("Apples" -> 1.00, "Bread" -> 0.80, "Soup" -> 0.65, "Milk" -> 1.30)

    val basket = List("Apples", "Apples", "Apples", "Bread", "Bread", "Soup", "Soup")

     val discounts: List[Discount] = List(Discount("Apples", 0.1, 3))

    val twoTinsOfSoupAndBreadDiscount =
      ConditionalDiscount("Bread", 0.5, Condition(List("Soup", "Soup")))

    val itemsCalculated = calculateDiscountedItems(
        Basket.apply(basket, priceMap),
        priceMap,
        List(twoTinsOfSoupAndBreadDiscount),
        discounts
    )

    val sumTotalWithDiscount = getTotal(itemsCalculated, discounted = true)
    val sumTotalWithoutDiscount = getTotal(itemsCalculated, discounted = false)

    assertEquals(sumTotalWithDiscount, BigDecimal(5.20))
    assert(sumTotalWithoutDiscount == 5.90)

  }
}


