package eryaz.software.zeusBase.data.persistence

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import eryaz.software.zeusBase.data.enums.Language
import eryaz.software.zeusBase.data.enums.LanguageType

object SessionManager {
    private const val KEY_TOKEN = "token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_LANGUAGE = "language"
    private const val KEY_COMPANY_ID = "companyId"
    private const val KEY_COMPANY_NAME = "companyName"
    private const val KEY_WAREHOUSE_NAME = "warehouseName"
    private const val KEY_WAREHOUSE_ID = "warehouseId"
    private const val KEY_APP_IS_LOCK = "appLock"
    private const val KEY_ALL_PERMISSION_ACCEPTED = "appLock"

    private lateinit var sharedPref: SharedPreferences

    fun init(context: Context) {
        sharedPref =
            EncryptedSharedPreferences.create(
                context, "secret_shared_prefs",
                createMasterKey(context),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
    }

    var companyId
        get() = sharedPref.getInt(KEY_COMPANY_ID, 4)
        set(value) {
            sharedPref.edit { putInt(KEY_COMPANY_ID, value) }
        }

    var warehouseId
        get() = sharedPref.getInt(KEY_WAREHOUSE_ID, 7)
        set(value) {
            sharedPref.edit { putInt(KEY_WAREHOUSE_ID, value) }
        }

    var companyName
        get() = sharedPref.getString(KEY_COMPANY_NAME, "") ?: "Şirket Seçiniz"
        set(value) {
            sharedPref.edit { putString(KEY_COMPANY_NAME, value) }
        }

    var warehouseName
        get() = sharedPref.getString(KEY_WAREHOUSE_NAME, "") ?: "Depo Seçiniz"
        set(value) {
            sharedPref.edit { putString(KEY_WAREHOUSE_NAME, value) }
        }

    var token
        get() = sharedPref.getString(KEY_TOKEN, "") ?: ""
        set(value) {
            val bearer = "Bearer "
            sharedPref.edit { putString(KEY_TOKEN, bearer + value) }
        }

    var userId
        get() = sharedPref.getInt(KEY_USER_ID, 0)
        set(value) {
            sharedPref.edit { putInt(KEY_USER_ID, value) }
        }

    var language
        get() = LanguageType.find(sharedPref.getString(KEY_LANGUAGE, Language.TR.name))
        set(value) {
            sharedPref.edit { putString(KEY_LANGUAGE, value.name) }
        }

    var appIsLocked
        get() = sharedPref.getBoolean(KEY_APP_IS_LOCK, true)
        set(value) {
            sharedPref.edit { putBoolean(KEY_APP_IS_LOCK, value) }
        }

    var allPermissionAccepted
        get() = sharedPref.getBoolean(KEY_ALL_PERMISSION_ACCEPTED, false)
        set(value) {
            sharedPref.edit { putBoolean(KEY_ALL_PERMISSION_ACCEPTED, value) }
        }

    fun clearData() {
        sharedPref.edit().clear().apply()
    }

    private fun createMasterKey(context: Context) =
        MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
}