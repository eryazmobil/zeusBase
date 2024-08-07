package eryaz.software.zeusBase.data.stepService

import kotlin.math.sqrt

object SensorFusionMath {
    @JvmStatic
    fun sum(array: FloatArray): Float {
        var retval = 0f
        for (i in array.indices) {
            retval += array[i]
        }
        return retval
    }

    @JvmStatic
    fun cross(arrayA: FloatArray, arrayB: FloatArray): FloatArray {
        val retArray = FloatArray(3)
        retArray[0] = arrayA[1] * arrayB[2] - arrayA[2] * arrayB[1]
        retArray[1] = arrayA[2] * arrayB[0] - arrayA[0] * arrayB[2]
        retArray[2] = arrayA[0] * arrayB[1] - arrayA[1] * arrayB[0]
        return retArray
    }

    @JvmStatic
    fun norm(array: FloatArray): Float {
        var retval = 0f
        for (i in array.indices) {
            retval += array[i] * array[i]
        }
        return sqrt(retval.toDouble()).toFloat()
    }

    @JvmStatic
    fun dot(a: FloatArray, b: FloatArray): Float {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2]
    }

    @JvmStatic
    fun normalize(a: FloatArray): FloatArray {
        val retval = FloatArray(a.size)
        val norm = norm(a)
        for (i in a.indices) {
            retval[i] = a[i] / norm
        }
        return retval
    }
}