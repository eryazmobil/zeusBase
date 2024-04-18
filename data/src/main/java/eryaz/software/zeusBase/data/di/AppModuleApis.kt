package eryaz.software.zeusBase.data.di

import eryaz.software.zeusBase.data.api.client.ZeusClient
import org.koin.dsl.module

val appModuleApis = module {

    single { ZeusClient.provideApi() }

    single { ZeusClient.provideUserApi() }

    single { ZeusClient.provideWorkActivityApi() }

    single { ZeusClient.provideBarcodeApi() }

    single { ZeusClient.providePlacementApi() }

    single { ZeusClient.provideOrderApi() }

    single { ZeusClient.provideCountingApi() }
}