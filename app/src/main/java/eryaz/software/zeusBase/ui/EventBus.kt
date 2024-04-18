package eryaz.software.zeusBase.ui

import eryaz.software.zeusBase.util.SingleLiveEvent

object EventBus {
    val navigateToSplash = SingleLiveEvent<Boolean>()
}
