package eryaz.software.zeusBase.data.di

import eryaz.software.zeusBase.data.repositories.AuthRepo
import eryaz.software.zeusBase.data.repositories.BarcodeRepo
import eryaz.software.zeusBase.data.repositories.CountingRepo
import eryaz.software.zeusBase.data.repositories.OrderRepo
import eryaz.software.zeusBase.data.repositories.PlacementRepo
import eryaz.software.zeusBase.data.repositories.UserRepo
import eryaz.software.zeusBase.data.repositories.WorkActivityRepo
import org.koin.dsl.module

val appModuleRepos = module {

    factory { AuthRepo(get()) }

    factory { UserRepo(get()) }

    factory { WorkActivityRepo(get()) }

    factory { BarcodeRepo(get()) }

    factory { PlacementRepo(get()) }

    factory { OrderRepo(get()) }

    factory { CountingRepo(get()) }

}