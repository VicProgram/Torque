import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


class MotoDetalle : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperar el idMoto que pasaste en el Intent
        val idMoto = intent.getIntExtra("idMoto", -1) // Valor por defecto -1 si no se encuentra

        if (idMoto != -1) {
            // Aquí puedes cargar los detalles de la moto usando el idMoto
        }
    }
}

@Composable
fun MotoDetalleView(idMoto: String?) {
    // Aquí puedes mostrar más detalles de la moto usando el id
    Text(text = "Detalles de la moto: $idMoto")
}
