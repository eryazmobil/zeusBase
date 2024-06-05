package eryaz.software.zeusBase.ui.dashboard.settings

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import eryaz.software.zeusBase.BuildConfig
import eryaz.software.zeusBase.R
import eryaz.software.zeusBase.data.api.utils.asUiState
import eryaz.software.zeusBase.data.api.utils.onSuccess
import eryaz.software.zeusBase.data.enums.LanguageType
import eryaz.software.zeusBase.data.enums.UiState
import eryaz.software.zeusBase.data.models.dto.CompanyDto
import eryaz.software.zeusBase.data.models.dto.CurrentUserDto
import eryaz.software.zeusBase.data.models.dto.PdaVersionDto
import eryaz.software.zeusBase.data.models.dto.WarehouseDto
import eryaz.software.zeusBase.data.models.remote.models.PdaVersionModel
import eryaz.software.zeusBase.data.persistence.SessionManager
import eryaz.software.zeusBase.data.repositories.AuthRepo
import eryaz.software.zeusBase.data.repositories.UserRepo
import eryaz.software.zeusBase.ui.base.BaseViewModel
import eryaz.software.zeusBase.util.extensions.toIntOrZero
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class SettingsViewModel(
    private val repo: UserRepo,
    private val authRepo: AuthRepo) : BaseViewModel() {

    private val _currentUserDto = MutableStateFlow<CurrentUserDto?>(null)
    val currentUserDto = _currentUserDto.asStateFlow()

    private val _warehouseList = MutableStateFlow(listOf<WarehouseDto>())
    val warehouseList = _warehouseList.asLiveData()

    private val _companyList = MutableStateFlow(listOf<CompanyDto>())
    val companyList = _companyList.asStateFlow()

    val checkWarehouse = MutableStateFlow(false)
    val checkCompany = MutableStateFlow(false)

    private val warehouseFromApiId = MutableStateFlow(0)
    private val companyFromApiId = MutableStateFlow(0)

    private val _warehouseName = MutableStateFlow("")
    val warehouseName = _warehouseName.asStateFlow()

    private val _companyName = MutableStateFlow("")
    val companyName = _companyName.asStateFlow()

    private val _currentLanguage = MutableStateFlow("")
    val currentLanguage = _currentLanguage.asStateFlow()

    private val _currentVersion = MutableStateFlow(BuildConfig.VERSION_NAME)
    val currentVersion = _currentVersion.asStateFlow()

    private val _pdaVersionModel = MutableStateFlow<PdaVersionDto?>(null)
    val pdaVersionModel = _pdaVersionModel.asStateFlow()


    val isUpToDate = MutableStateFlow("")
    val updateClickable = MutableStateFlow(false)


    init {
        fetchData()
        fetchWarehouseList()
        fetchCompanyList()
        getPdaVersion()
    }

    fun fetchData() {
        viewModelScope.launch {
            _uiState.emit(UiState.LOADING)

            val loginInfo = async { getCurrentLoginInformation() }
            val uiState = if (loginInfo.await().asUiState() == UiState.SUCCESS) {
                UiState.SUCCESS
            } else {
                UiState.ERROR
            }
            _uiState.emit(uiState)
        }
    }

    private suspend fun getCurrentLoginInformation() =
        repo.getCurrentLoginInformations().onSuccess {
            _currentUserDto.emit(it)
            warehouseFromApiId.emit(it.warehouseId)
            companyFromApiId.emit(it.companyId)
            SessionManager.userId = it.userId
        }

    fun setCompany(dto: CompanyDto) {
        SessionManager.companyId = dto.id
        viewModelScope.launch {
            _companyName.emit(dto.code)
        }
    }

    fun setWarehouse(dto: WarehouseDto) {
        SessionManager.warehouseId = dto.id
        viewModelScope.launch {
            _warehouseName.emit(dto.name)
        }
    }

    private fun getPdaVersion(){
        executeInBackground(
            showProgressDialog = true
        ) {
            authRepo.getPdaVersion()
                .onSuccess {
                    _pdaVersionModel.emit(it)
                    checkIsUpToDate(it.version)
                }
        }
    }

    private suspend fun checkIsUpToDate(version: String?){
        version?.let { pdaVersion ->
            val newVersion = versionToInt(pdaVersion)
            val currentVersion = versionToInt(BuildConfig.VERSION_NAME)
            if (newVersion > currentVersion) {
                isUpToDate.emit(version + " " + stringProvider.invoke(R.string.app_get_update))
                updateClickable.emit(true)
            }
            else {
                isUpToDate.emit(stringProvider.invoke(R.string.app_is_up_to_date))
            }
        }
    }

    private fun versionToInt(version: String) : Int{
        val list = version.split(".")
        val sb = StringBuilder()
        list.forEach {
            sb.append(it)
        }
        return sb.toString().toIntOrZero()
    }

    private fun fetchWarehouseList() = executeInBackground(_uiState) {
        repo.getWarehouseList(SessionManager.companyId).onSuccess {
            _warehouseList.emit(it)
            checkSelectedWarehouse()
            checkWarehouseData()
        }
    }

    private fun fetchCompanyList() = executeInBackground(_uiState) {
        repo.getCompanyList().onSuccess {
            _companyList.emit(it)
            checkSelectedCompany()
            checkCompanyData()
            checkSelectedLanguage()
        }
    }

    private fun checkWarehouseData() {
        if (warehouseFromApiId.value > 0) {
            SessionManager.warehouseId = warehouseFromApiId.value
            _warehouseList.value.filter {
                it.id == warehouseFromApiId.value
            }.map {
                viewModelScope.launch {
                    _warehouseName.emit(it.code)
                    checkWarehouse.emit(true)
                }
            }
        }
    }

    private fun checkCompanyData() {
        if (companyFromApiId.value > 0) {
            SessionManager.companyId = companyFromApiId.value
            _companyList.value.filter {
                it.id == companyFromApiId.value
            }.map {
                viewModelScope.launch {
                    _companyName.emit(it.code)
                    checkCompany.emit(true)
                }
            }
        }
    }

    private fun checkSelectedWarehouse() {
        if (SessionManager.warehouseId > 0) {
            _warehouseList.value.filter {
                it.id == SessionManager.warehouseId
            }.map {
                viewModelScope.launch {
                    _warehouseName.emit(it.name)
                }
            }
        }
    }

    private fun checkSelectedCompany() {
        if (SessionManager.companyId > 0) {
            val companyWithId = _companyList.value.find { it.id == SessionManager.companyId }
            companyWithId?.let {
                viewModelScope.launch {
                    _companyName.emit(it.code)
                }
            }
        }
    }

    private fun checkSelectedLanguage() {
        viewModelScope.launch {
            _currentLanguage.emit(SessionManager.language.fullName)
        }

    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            _currentLanguage.emit(LanguageType.find(lang).fullName)
        }
    }
}
