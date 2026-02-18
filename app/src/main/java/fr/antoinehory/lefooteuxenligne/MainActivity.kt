package fr.antoinehory.lefooteuxenligne

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.antoinehory.lefooteuxenligne.ui.debug.DeckPreviewScreen
import fr.antoinehory.lefooteuxenligne.ui.game.GameScreen
import fr.antoinehory.lefooteuxenligne.ui.theme.LeFooteuxEnLigneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LeFooteuxEnLigneTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = "home", modifier = Modifier.padding(innerPadding)) {
                        composable("home") {
                            HomeScreen(navController = navController)
                        }
                        composable("game_screen") {
                            GameScreen()
                        }
                        composable("deck_preview") { // Added this route
                            DeckPreviewScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("game_screen") }) {
            Text("Go to Game Screen")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("deck_preview") }) { // Added this button
            Text("Go to Deck Preview")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LeFooteuxEnLigneTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { }) {
                Text("Go to Game Screen")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { }) {
                Text("Go to Deck Preview")
            }
        }
    }
}
