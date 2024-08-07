package eryaz.software.zeusBase.data.stepService

import android.content.Context

class StepDetectorFactory {

    companion object {

        @JvmStatic
        fun create(context: Context): StepDetector {
            return StepDetectorImpl(context)
        }
    }
}