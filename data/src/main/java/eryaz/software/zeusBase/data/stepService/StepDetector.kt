package eryaz.software.zeusBase.data.stepService

interface StepDetector {

    fun registerListener(stepListener: StepListener): Boolean

    fun unregisterListener()
}