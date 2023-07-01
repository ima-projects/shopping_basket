package com.ibtisam.priceBasket.commonTypes

import com.ibtisam.priceBasket.commonTypes.Discount
import com.ibtisam.priceBasket.commonTypes.Discount.Condition

object Discount{

    abstract class DiscountDefaultProperties {
        val item: String
        val discount: Double
    }

   case class ConditionalDiscount(item: String, discount: Double, condition: Condition)
        extends DiscountDefaultProperties

    
    case class Discount(item: String, discount: Double, numberOfTimesToApply: Int)
      extends DiscountDefaultProperties
    
    case class Condition(itemsRequired: List[String]) {
        def countOccurrencesOfEachItem: Map[String, Int] =
            this.itemsRequired.groupBy(identity).mapValues(_.size).toMap
    }

}
