package eryaz.software.zeusBase.core

import eryaz.software.zeusBase.data.stepService.AccelSensorDetector
import eryaz.software.zeusBase.data.stepService.StepListener
import eryaz.software.zeusBase.data.stepService.StepSenorDetector
import android.content.Context
import android.util.Log

object StepCounterManager {

    private var accelSteps = 0
    private var accelSensorDetector: AccelSensorDetector? = null

    fun initialize(context: Context) {
        buildStepDetector(context)
    }

     private fun buildStepDetector(context: Context) {
        accelSensorDetector = AccelSensorDetector(context)
        accelSensorDetector?.registerListener(object : StepListener {
            override fun onStep(count: Int) {
                accelSteps += count

            }
        })
    }

    fun resetSteps() {
        accelSteps = 0
    }

    fun getCurrentStep(): Int {
        return accelSteps
    }
}