package com.cooptech.pdfreader.billingLibrary

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase

interface BillingClientListeners {
    fun onNoProductsAvailable()
    fun onProductsAvailable(products: List<ProductDetails>)
    fun onPurchaseSuccess()
    fun onPurchaseAcknowledged(purchase: Purchase)
    fun onPurchaseNotAcknowledged(purchase: Purchase)
    fun onPurchaseUnSpecifiedState(purchase: Purchase)
    fun onPurchasePending(purchase: Purchase)
    fun onPurchaseFailed(debugMessage: String)
}