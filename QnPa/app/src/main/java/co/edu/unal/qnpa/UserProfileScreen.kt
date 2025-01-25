package co.edu.unal.qnpa

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun UserProfileScreen(
    goBack: () -> Unit = {}
){
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ){padding ->
        UserProfileContent(
            paddingValues = padding,
            goBack = goBack
        )
    }
}

@Preview
@Composable
fun UserProfileContent(
    paddingValues: PaddingValues = PaddingValues(),
    goBack: () -> Unit = {}
){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 16.dp, bottom = 5.dp)
            ){
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Icon Back",
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .size(27.dp)
                        .clickable { goBack() }
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Rounded.Create,
                    contentDescription = "Icon Edit",
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .size(24.dp)
                        .clickable {  }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 5.dp)
            ){
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Rounded.Share,
                    contentDescription = "Icon Share",
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .size(24.dp)
                        .clickable {  }
                )
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.inversePrimary)
            ){
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Icon Profile",
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxSize()
                        .clickable {  }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Nombre", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(10.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = "Icon Star",
                    modifier = Modifier
                        .padding(start = 36.dp, end = 8.dp)
                        .size(27.dp)
                )
                Text(text = "Reputación", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Icon Person",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(27.dp)
                )
                Text(text = "Seguidores", style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(end = 36.dp)
                )
            }
            HorizontalDivider()
            Spacer(modifier = Modifier.height(4.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 1000.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(top = 16.dp),
            ) {
                items(10){
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .height(200.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.inversePrimary)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}