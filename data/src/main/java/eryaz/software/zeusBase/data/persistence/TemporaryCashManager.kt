package eryaz.software.zeusBase.data.persistence

import eryaz.software.zeusBase.data.models.dto.WorkActionDto
import eryaz.software.zeusBase.data.models.dto.WorkActivityDto
import eryaz.software.zeusBase.data.models.remote.response.WorkActionTypeResponse

class TemporaryCashManager private constructor() {
    var workActionTypeList:List<WorkActionTypeResponse>? = null
    var workActivity: WorkActivityDto? = null
    var workAction: WorkActionDto? = null

    companion object {
        @Volatile
        private var instance: TemporaryCashManager? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: TemporaryCashManager().also { instance = it }
            }
    }
}