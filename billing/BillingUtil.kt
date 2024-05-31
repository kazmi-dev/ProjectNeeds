package com.capra.pdfreader.pdfviewer.alldocuments.wordpptexel.filesreader.freeapp.utils.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams

class BillingUtil(
    context: Context,
    purchasesUpdatedListener: PurchasesUpdatedListener
): BillingClientStateListener {

   companion object{
       private const val BILLING_LOG = "BillingUtil"
   }
    private val enablePendingPurchaseParams = PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
    private var productId: ProductDetails? = null

    private val billingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases(enablePendingPurchaseParams)
        .setListener(purchasesUpdatedListener)
        .build()

    fun startConnection(){
        billingClient.startConnection(this)
    }

    fun terminateConnection(){
        billingClient.endConnection()
    }


    override fun onBillingServiceDisconnected() {
        Log.d(BILLING_LOG, "onBillingServiceDisconnected")
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        if (billingResult.responseCode == BillingResponseCode.OK){
            queryProductDetails()
        }
    }

    private fun queryProductDetails() {

        val queryProductList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("android.test.purchased")
                .setProductType(ProductType.INAPP)
                .build()
        )

        val queryProductDetailsParams = QueryProductDetailsParams
            .newBuilder()
            .setProductList(queryProductList)
            .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams){billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingResponseCode.OK){
                Log.d(BILLING_LOG, "queryProductDetailsAsync response ok")
                if (productDetailsList.isNotEmpty()){
                    Log.d(BILLING_LOG, "product list not empty")
                    productId = productDetailsList[0]
                    productDetailsList.forEach {product->
                       Log.d(BILLING_LOG, "ID: ${product.productId}, TYPE: ${product.productType}, PRICE: ${product.oneTimePurchaseOfferDetails?.formattedPrice}")
                   }
                }else{
                    Log.d(BILLING_LOG, "product list empty")
                }
            }else{
                Log.d(BILLING_LOG, "Error: ${billingResult.debugMessage}")
            }
        }

    }


    fun launchBillingFlow(activity: Activity){

        Log.d(BILLING_LOG, "launchBillingFlow: starting")

        val productDetailsParams = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productId?: return)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParams)
            .build()

        Log.d(BILLING_LOG, "launchBillingFlow: stated")

        billingClient.launchBillingFlow(activity, billingFlowParams)

    }

}