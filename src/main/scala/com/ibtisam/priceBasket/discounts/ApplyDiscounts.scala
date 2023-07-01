package com.ibtisam.priceBasket.discounts

import com.ibtisam.priceBasket.commonTypes.{Item, Basket, Discount}
import com.ibtisam.priceBasket.commonTypes.Discount.Discount



object ApplyDiscount {
  def filterDiscount(basket: Basket, discounts: List[Discount]): Basket = {
    // checking to see if discount applies

    Basket(
      discounts
        .filter(d => basket.items.map(i => i.name).contains(d.item))
        .map(dis => {
          val priceItem = basket.items
            .find(b => { b.name == dis.item && b.price == b.discountedPrice })
            .get
            .price
          Item(dis.item, priceItem, priceItem * (1 - dis.discount))
        })
    )
       
  }


  def filterForItemsNotDiscounted(basket: Basket, discountedItems: Basket) : Map[String, Int] = {
    def countOfDiscounted(item: String): Int =
      discountedItems
                    .countQuantityOfEach
                    .getOrElse(item, 0)
    
    basket
          .countQuantityOfEach
          .flatMap(q => Map(q._1 -> (q._2 - countOfDiscounted(q._1))))
          .filter(m => m._2 > 0)

  }

    def getBasketWithDiscountsNowApplied(basket: Basket, discounts: List[Discount]): Basket = {
      val discountedItems = filterDiscount(basket, discounts)
       
      val itemsNotDiscounted: Map[String, Int] =
        filterForItemsNotDiscounted(basket, discountedItems)
      
      val OriginalPriceForAndCountOfItemsWithoutDiscountsApplied = (for {
      itemNotDiscounted <- itemsNotDiscounted
      priceItem = basket.items
                    .find(item => item.name == itemNotDiscounted._1)
                    .get
                    .price
                  _ <- 0 until itemNotDiscounted._2
                } yield Item(itemNotDiscounted._1, priceItem, priceItem)).toList

      // concatenating items with discounts and items with no discount(normal price) and putting them all in one basket
      discountedItems.add(Basket(OriginalPriceForAndCountOfItemsWithoutDiscountsApplied))

    }

}