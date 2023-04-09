package com.example.composequadrant

import android.os.Bundle
import android.support.v4.os.IResultReceiver.Default
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composequadrant.ui.theme.ComposeQuadrantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeQuadrantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    QuadrantApp()
                }
            }
        }
    }
}

@Composable
fun BoxWithLayout (content: @Composable RowScope.()->Unit) {
    Row () {
        content()
    }
}

@Composable
fun QuadrantApp() {
    BoxWithLayout () {
        Row() {
            Column(Modifier.weight(1f)) {
                Surface(color = androidx.compose.ui.graphics.Color.Green,
                    modifier = Modifier.fillMaxHeight(0.5f)) {
                    QuadrantText(
                        quadrantName = stringResource(R.string.first_title),
                        quadrantText = stringResource(R.string.first_description))
                }
                Surface(
                    color = androidx.compose.ui.graphics.Color.Cyan,
                    modifier = Modifier.fillMaxHeight()) {
                    QuadrantText(
                        quadrantName = stringResource(R.string.second_title),
                        quadrantText = stringResource(R.string.second_description))
                }
            }
            Column(Modifier.weight(1f)) {
                Surface(color = androidx.compose.ui.graphics.Color.Yellow,
                    modifier = Modifier.fillMaxHeight(0.5f)) {
                    QuadrantText(
                        quadrantName = stringResource(R.string.third_title),
                        quadrantText = stringResource(R.string.third_description))
                }
                Surface(
                    color = androidx.compose.ui.graphics.Color.Gray,
                    modifier = Modifier.fillMaxHeight()) {
                    QuadrantText(
                        quadrantName = stringResource(R.string.fourth_title),
                        quadrantText = stringResource(R.string.fourth_description))
                }
            }
        }
    }
}

@Composable
fun QuadrantText(
    quadrantName: String,
    quadrantText: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally ) {
        Text(text = quadrantName,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 16.dp)
                )
        Text(text = quadrantText, textAlign = TextAlign.Justify)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeQuadrantTheme {
            Surface(modifier = Modifier.fillMaxWidth()) {
                QuadrantApp()
            }
    }
}
