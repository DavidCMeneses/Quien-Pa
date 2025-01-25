package co.edu.unal.qnpa

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SearchScreen(
    goBack: () -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ){padding ->
        Box(modifier = Modifier.padding(padding)){}
        SearchBar(
            query = searchText,
            onQueryChange = {
                searchText = it
                            },
            onSearch = {
                active = false
                       },
            active = active,
            onActiveChange = {
                active = it
                             },
            placeholder = { Text(text = "Buscar parche...")},
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Icon",
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .size(27.dp)
                        .clickable { goBack() }
                )

            },
            trailingIcon = {
                if (active){
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Icon",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(27.dp)
                            .clickable {
                                if (searchText.isNotEmpty()){
                                    searchText = ""
                                }else{
                                    active = false
                                }
                            }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Icon",
                        modifier = Modifier
                            .padding(start = 8.dp, end = 16.dp)
                            .size(27.dp)
                            .clickable {  }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

        }
    }
}