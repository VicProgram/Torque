import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.torque.Usuario
import com.example.torque.database.TorqueDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VistaUsuario(applicationContext: Context) : ViewModel() {
    private val dbHelper = TorqueDatabaseHelper(applicationContext)

    fun registrarUsuario(nombre: String, email: String, passwordHash: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newRowId = dbHelper.insertarUsuario(nombre, email, passwordHash)
            withContext(Dispatchers.Main) {
                if (newRowId > 0) {
                    // Usuario registrado con éxito
                } else {
                    // Error al registrar el usuario
                }
            }
        }
    }

    suspend fun obtenerUsuarioPorId(id: Int): Usuario? = withContext(Dispatchers.IO) {
        dbHelper.obtenerUsuarioPorId(id)
    }



    override fun onCleared() {
        super.onCleared()
        dbHelper.close()
    }
}