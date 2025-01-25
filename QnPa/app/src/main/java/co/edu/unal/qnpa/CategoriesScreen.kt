package co.edu.unal.qnpa

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.unal.qnpa.data.CategoryItem
import co.edu.unal.qnpa.ui.theme.Pink80
import co.edu.unal.qnpa.ui.theme.Purple40
import co.edu.unal.qnpa.ui.theme.Purple80

@Preview
@Composable
fun CategoriesScreen(){
    // Acá genero las categorias pero en realidad se deben pedir al backend
    var categories by remember {
        mutableStateOf(
            (1..100).map {
                CategoryItem(
                    name = "Category $it",
                    isSelected = false,
                    id = it
                )
            }
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        //.padding(28.dp)
    ){

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier
                .height(170.dp)
                .fillMaxWidth()
                .padding(10.dp)

                .clip(RoundedCornerShape(20.dp))
                .background(Pink80.copy(alpha = 0.3f)),

            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center
                        
                ) {
                    Text(text = "Inicio Rápido", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Gustos", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Texto del figma xdddddddddddddddddddddddddddddddddddddd asdljhaksd aushgdkuahskdl kajshdkjas", fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier
                .height(400.dp)
                .verticalScroll(rememberScrollState())
            ){
                LazyVerticalGrid(
                    modifier = Modifier.heightIn(max = 1000.dp),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(20.dp)) {
                    items(categories.size){ index: Int ->

                        Button(
                            modifier = Modifier.padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!categories[index].isSelected) {
                                    Purple80
                                } else {
                                    Purple40
                                }
                            ),
                            onClick = {
                                categories = categories.mapIndexed { i, item ->
                                    if (i == index) {
                                        item.copy(isSelected = !item.isSelected)
                                    } else {
                                        item
                                    }
                                }
                            } ) {
                            Text(text = categories[index].name, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(5.dp))
                            if (categories[index].isSelected){
                                Icon(imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.Green,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Icon(imageVector = Icons.Default.Add,
                                    contentDescription = "Selected",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {

            }){
                Text(text = "Guardar")
            }
        }
    }

}