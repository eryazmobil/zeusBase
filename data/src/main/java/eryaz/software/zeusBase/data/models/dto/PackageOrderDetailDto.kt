package eryaz.software.zeusBase.data.models.dto

data class PackageOrderDetailDto(

    val id: Int,
    val product: ProductDto,
    val quantity: Int,
    val quantityShipped: Int,
    val quantityCollected: Int
)