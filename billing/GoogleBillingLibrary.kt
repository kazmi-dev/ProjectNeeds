import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsParams.Product
import com.android.billingclient.api.QueryPurchasesParams

class GoogleBillingLibrary(
    appContext: Context,
) : BillingClientStateListener, PurchasesUpdatedListener {

    private val billingClient =  BillingClient.newBuilder(appContext)
        .setListener(this)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .build()

    private val productIds = mutableListOf<String>()
    private var billingClientListener: BillingClientListeners? = null

    private fun startConnection() {
        billingClient.startConnection(this)
    }

    fun setProductIds(productIds: List<String>) {
        this.productIds.addAll(productIds)
    }

    fun setBillingClientListeners(billingClientListeners: BillingClientListeners) {
        this.billingClientListener = billingClientListeners
    }

    override fun onBillingServiceDisconnected() {
        startConnection()
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        if (billingResult.responseCode == BillingResponseCode.OK) {
            if (productIds.isNotEmpty()) {
                queryAvailableProducts(productIds)
            } else {
                billingClientListener?.onNoProductsAvailable()
            }
        }
    }

    private fun queryAvailableProducts(productIds: List<String>) {
        val productList = mutableListOf<Product>()

        productIds.forEach { productId ->
            productList.add(
                Product.newBuilder()
                    .setProductId(productId)
                    .setProductType(ProductType.INAPP)
                    .build()
            )
        }
        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingResponseCode.OK) {

                if (productDetailsList.isNotEmpty()) {
                    billingClientListener?.onProductsAvailable(productDetailsList.toList())
                } else {
                    billingClientListener?.onNoProductsAvailable()
                }

            }
        }

    }

    fun initiatedPurchaseFlow(activity: Activity, productDetails: ProductDetails) {

        val productDetailsParams = ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .build()

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)

    }


    override fun onPurchasesUpdated(
        billinfResult: BillingResult,
        purchaseList: MutableList<Purchase>?,
    ) {
        //handle purchase
        if (billinfResult.responseCode == BillingResponseCode.OK && purchaseList != null){
            for (purchase in purchaseList){
                acknowledgePurchase(purchase)
            }
        }else{
            billingClientListener?.onPurchaseFailed(billinfResult.debugMessage)
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        when(purchase.purchaseState){
            Purchase.PurchaseState.PURCHASED->{
                if (!purchase.isAcknowledged){
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    billingClient.acknowledgePurchase(acknowledgePurchaseParams){
                        if (it.responseCode == BillingResponseCode.OK){
                            billingClientListener?.onPurchaseAcknowledged(purchase)
                        }else{
                            billingClientListener?.onPurchaseNotAcknowledged(purchase)
                        }
                    }

                }else{
                    billingClientListener?.onPurchaseAcknowledged(purchase)
                }
            }
            Purchase.PurchaseState.UNSPECIFIED_STATE->{
                billingClientListener?.onPurchaseUnSpecifiedState(purchase)
            }
            Purchase.PurchaseState.PENDING->{
                billingClientListener?.onPurchasePending(purchase)
            }
        }
    }


    fun queryPurchases(){

        val purchaseParams = QueryPurchasesParams.newBuilder()
            .setProductType(ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(purchaseParams){ billingResult, purchaseList ->
            if (billingResult.responseCode == BillingResponseCode.OK) {
                if (purchaseList.isNotEmpty()) {
                    purchaseList.forEach {
                        acknowledgePurchase(it)
                    }
                } else {
                    billingClientListener?.onNoProductsAvailable()
                }
            }
        }

    }

}
