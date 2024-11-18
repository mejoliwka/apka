package com.example.apka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apka.ui.theme.ApkaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApkaTheme {
                // Nawigacja między ekranami
                val navController = rememberNavController()

                // NavHost - kontener zarządzający ekranami
                // NavController przechodzi między ekranami i przekazuje dane
                // startDestination wskazuje pierwszy ekran, który ma się pojawić
                NavHost(navController = navController, startDestination = "screen1") {
                    // Composable definiuje ekran w systemie nawigacyjnym
                    // "screen1" to identyfikator ekranu
                    // Screen1(navController) to funkcja odpowiedzialna za ten ekran
                    composable("screen1") { Screen1(navController) }

                    // "screen2/{userName}" to ekran, który otrzymuje zmienną userName
                    composable("screen2/{userName}") { backStackEntry ->
                        val userName = backStackEntry.arguments?.getString("userName") ?: ""
                        Screen2(navController, userName)
                    }

                    // "screen3/{userName}/{age}" to ekran, który otrzymuje dwa argumenty: userName i age
                    composable("screen3/{userName}/{age}") { backStackEntry ->
                        val userName = backStackEntry.arguments?.getString("userName") ?: ""
                        val age = backStackEntry.arguments?.getString("age") ?: ""
                        Screen3(navController, userName, age)
                    }
                }
            }
        }
    }
}

@Composable
fun Screen1(navController: NavController) {
    // remember utrzymuje wartość, nawet po odświeżeniu ekranu
    // mutableStateOf przechowuje daną wartość i umożliwia jej zmianę
    var name by remember { mutableStateOf(TextFieldValue()) }

    // Column - kontener, w którym guzik i tekst będą wyświetlane jeden pod drugim
    Column(
        modifier = Modifier.fillMaxSize(),  //  fillMaxSize() cała dostępna przestrzeń kolumny
        horizontalAlignment = Alignment.CenterHorizontally,  // Wyśrodkowanie w poziomie
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center  // Wyśrodkowanie w pionie
    ) {

        Text(
            text = "Ekran 1 - Wprowadź imię",
            color = Color.Black,
            style = TextStyle(fontSize = 24.sp)  // 24 skalowane punkty
        )

        // Pole do wpisania imienia
        // BasicTextField mniej zaawansowane od TextField ramki styl
        BasicTextField(
            // imie przechowuje
            value = name,
            // it nowa wartość wprowadzona w polu tekstowym, przekazywana do onValueChange za każdym razem, gdy użytkownik wpisuje coś w pole tekstowe
            onValueChange = { name = it }
        )

        // Przycisk do ekranu 2
        Button(onClick = {
            if (name.text.isNotEmpty()) {  // Sprawdzamy, czy pole nie jest puste jak jest nie przechodzimy do 2 ekranu
                navController.navigate("screen2/${name.text}")  // Przechodzimy do ekranu 2 z przekazanym imieniem
            }
        }) {
            Text(
                text = "Przejdź do Ekranu 2",
                color = Color.Black,
                style = TextStyle(fontSize = 18.sp)
            )
        }
    }
}

@Composable
fun Screen2(navController: NavController, userName: String) {
    var age by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = "Ekran 2 - Witaj $userName. Wprowadź swój wiek:",
            color = Color.Black,
            style = TextStyle(fontSize = 24.sp)
        )

        BasicTextField(
            value = age,
            onValueChange = { age = it }
        )

        Button(onClick = {
            if (age.isNotEmpty()) {
                navController.navigate("screen3/$userName/$age")
            }
        }) {
            Text(
                text = "Przejdź do Ekranu 3",
                color = Color.Black,
                style = TextStyle(fontSize = 18.sp)
            )
        }
    }
}

@Composable
fun Screen3(navController: NavController, userName: String, age: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = "Ekran 3 - Twoje imię: $userName",
            color = Color.Black,
            style = TextStyle(fontSize = 24.sp)
        )

        if (age.isNotEmpty()) {
            Text(
                text = "Twój wiek: $age",
                color = Color.Black,
                style = TextStyle(fontSize = 24.sp)
            )
        }

        // Przycisk do powrotu do ekranu 1
        // popBackStack pozwala na cofanie się w stosie nawigacyjnym
        // false oznacza, że ekran 1 zostanie w stosie nawigacyjnym
        // true, to ekran 1 zostałby usunięty ze stosu, a aplikacja przeszłaby do następnego ekranu poniżej w stosie
        Button(onClick = {
            navController.popBackStack("screen1", false)
        }) {
            Text(
                text = "Przejdź do Ekranu 1",
                color = Color.Black,
                style = TextStyle(fontSize = 18.sp)
            )
        }
    }
}
