package cl.duoc.visso.utils

object Constants {
    // Cambia por tu IP local si usas dispositivo físico
    // Emulador: 10.0.2.2
    // Dispositivo físico: IP de tu PC (ej: 192.168.1.100)
    const val BASE_URL = "http://192.168.0.15:8081/"

    // DataStore
    const val DATASTORE_NAME = "visso_preferences"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_EMAIL = "user_email"
    const val KEY_USER_NAME = "user_name"
    const val KEY_USER_ROLE = "user_role"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
}