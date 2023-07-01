package com.ibtisam.priceBasket

import com.ibtisam.priceBasket.commonTypes.{Item, Basket}
import com.ibtisam.priceBasket.commonTypes.Discount.{Discount, ConditionalDiscount, Condition}

import com.ibtisam.priceBasket.discounts.ApplyDiscount._
import com.ibtisam.priceBasket.discounts.MatchConditionalDiscounts.processConditionalDiscount


import munit.*


class TestMatchDiscount extends munit.FunSuite{

    // dummy tests
    test("should return a basket where multiple conditional discounts has been applied"){
        val itemsInBasket = Basket(List(
        Item("Apples", 1.00, 1.00),
        Item("Apples", 1.00, 1.00),
        Item("Milk", 1.30, 1.30),
        Item("Banana", 1.80, 1.80),
        Item("Watermelon", 2.00, 2.0)
        ))

        val expectedItems = Basket(List(
        Item("Apples", 1.00, 1.00),
        Item("Apples", 1.00, 1.00),
        Item("Milk", 1.30, 0.65),
        Item("Banana", 1.80, 1.80),
        Item("Watermelon", 2.00, 1.60)
        ))

        val conditionalDiscounts = List(
            ConditionalDiscount("Milk", 0.5, Condition(List("Apples", "Apples"))),
            ConditionalDiscount("Watermelon", 0.2, Condition(List("Banana")))
            )

        val allDiscounts = processConditionalDiscount(itemsInBasket, conditionalDiscounts)

        val actual = getBasketWithDiscountsNowApplied(itemsInBasket, allDiscounts)
        
        assert(actual.equals(expectedItems))
    }

    test("should return a basket where the conditional discount has only been applied once"){

        val itemsInBasket = Basket(List(
        Item("Apples", 1.00, 1.00),
        Item("Apples", 1.00, 1.00),
        Item("Milk", 1.30, 1.30),
        Item("Milk", 1.30, 1.30)
        ))

        val expectedItems = Basket(List(
        Item("Apples", 1.00, 1.00),
        Item("Apples", 1.00, 1.00),
        Item("Milk", 1.30, 1.30),
        Item("Milk", 1.30, 0.65)
        ))

        val conditionalDiscount = ConditionalDiscount("Milk", 0.5, Condition(List("Apples", "Apples")))

        val allDiscounts = processConditionalDiscount(itemsInBasket, List(conditionalDiscount))

        val actual = getBasketWithDiscountsNowApplied(itemsInBasket, allDiscounts)
    
        assert(actual.equals(expectedItems))
  }

    test("should return a basket where no conditonal discount (and no regular discount) has been applied"){

        val itemsInBasket = Basket(List(
        Item("Milk", 1.30, 1.30),
        Item("Apples", 1.00, 1.00)
        ))

        val expectedItems = Basket(List(
        Item("Milk", 1.30, 1.30),
        Item("Apples", 1.00, 1.00)
        ))

        val conditionalDiscount = ConditionalDiscount("Milk", 0.5, Condition(List("Apples", "Apples")))

        val noDiscount = processConditionalDiscount(itemsInBasket, List(conditionalDiscount))

        val actual = getBasketWithDiscountsNowApplied(itemsInBasket, noDiscount)
    
        assert(actual.equals(expectedItems))
  }

      test("should be able to infer the required number of discounts for the basket"){

        val itemsInBasket = Basket(List(
        Item("Bread", 0.80, 0.80),
        Item("Bread", 0.80, 0.80),
        Item("Soup", 0.65, 0.65),
        Item("Soup", 0.65, 0.65),
        Item("Soup", 0.65, 0.65),
        Item("Soup", 0.65, 0.65),
        Item("Soup", 0.65, 0.65)
        ))

        val conditionalDiscount = ConditionalDiscount("Bread", 0.5, Condition(List("Soup", "Soup")))

        val allDiscounts: List[Discount] = processConditionalDiscount(itemsInBasket, List(conditionalDiscount))
    
        assert(allDiscounts.head.numberOfTimesToApply == 2)
  }
}
